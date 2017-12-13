/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.railz.client.top;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.railz.client.model.ModelRoot;
import org.railz.client.renderer.ViewLists;
import org.railz.client.view.GUIRoot;
import org.railz.config.LogManager;
import org.railz.controller.AddPlayerCommand;
import org.railz.controller.AddPlayerResponseCommand;
import org.railz.controller.ConnectionListener;
import org.railz.controller.ConnectionToServer;
import org.railz.controller.MoveReceiver;
import org.railz.controller.ResourceBundleManager;
import org.railz.controller.ServerCommand;
import org.railz.controller.ServerMessageCommand;
import org.railz.controller.SourcedMoveReceiver;
import org.railz.controller.UncommittedMoveReceiver;
import org.railz.controller.UntriedMoveReceiver;
import org.railz.controller.WorldChangedCommand;
import org.railz.move.Move;
import org.railz.move.MoveStatus;
import org.railz.move.TimeTickMove;
import org.railz.util.FreerailsProgressMonitor;
import org.railz.util.Resources;
import org.railz.world.player.Player;
import org.railz.world.player.PlayerPrincipal;
import org.railz.world.top.KEY;
import org.railz.world.top.World;

/**
 * This class receives moves from the client. This class tries out moves on the
 * world if necessary, and passes them to the connection.
 */
public class ConnectionAdapter implements UntriedMoveReceiver,
		ConnectionListener {
	private NonAuthoritativeMoveExecuter moveExecuter;
	private final ModelRoot modelRoot;
	private final Player player;
	ConnectionToServer connection;
	private final Object authMutex = new Integer(1);
	private boolean authenticated;
	private final GUIClient guiClient;
	private static final String CLASS_NAME = ConnectionAdapter.class.getName();
	private static final Logger logger = LogManager.getLogger(CLASS_NAME);

	/**
	 * The GameLoop providing the move execution thread for this
	 * ConnectionAdapter's Move Executer
	 */
	private GameLoop gameLoop;

	/**
	 * we forward outbound moves from the client to this.
	 */
	UncommittedMoveReceiver uncommittedReceiver;
	MoveReceiver moveReceiver;
	World world;
	private final FreerailsProgressMonitor progressMonitor;
	private final GUIRoot guiRoot;

	public int getNumBlockedMoves() {
		return ((NonAuthoritativeMoveExecuter.PendingQueue) moveExecuter
				.getUncommittedMoveReceiver()).getNumBlockedMoves();
	}

	public ConnectionAdapter(ModelRoot mr, GUIRoot gr, Player player,
			FreerailsProgressMonitor pm, GUIClient gc) {
		modelRoot = mr;
		this.player = player;
		this.progressMonitor = pm;
		guiClient = gc;
		guiRoot = gr;
	}

	/**
	 * This class receives moves from the connection and passes them on to a
	 * MoveReceiver.
	 */
	public class WorldUpdater implements SourcedMoveReceiver {
		private MoveReceiver moveReceiver;

		/**
		 * TODO get rid of this
		 */
		@Override
		public synchronized void undoLastMove() {
			// do nothing
		}

		@Override
		public synchronized void processMove(Move move, ConnectionToServer c) {
			processMove(move);
		}

		/**
		 * Processes inbound moves from the server
		 */
		@Override
		public synchronized void processMove(Move move) {
			if (moveReceiver == null)
				return;

			if (move instanceof TimeTickMove) {
				/*
				 * flush our outgoing moves prior to receiving next tick TODO
				 * improve our buffering strategy
				 */
				connection.flush();
			}

			moveReceiver.processMove(move);
		}

		public synchronized void setMoveReceiver(MoveReceiver moveReceiver) {
			this.moveReceiver = moveReceiver;
		}
	}

	private final WorldUpdater worldUpdater = new WorldUpdater();

	/**
	 * Processes outbound moves to the server
	 */
	@Override
	public synchronized void processMove(Move move) {
		if (uncommittedReceiver != null) {
			uncommittedReceiver.processMove(move);
		}
	}

	@Override
	public synchronized void undoLastMove() {
		if (uncommittedReceiver != null) {
			uncommittedReceiver.undoLastMove();
		}
	}

	@Override
	public synchronized MoveStatus tryDoMove(Move move) {
		return move.tryDoMove(world, move.getPrincipal());
	}

	@Override
	public synchronized MoveStatus tryUndoMove(Move move) {
		return move.tryUndoMove(world, move.getPrincipal());
	}

	private void closeConnection() {
		connection.close();
		connection.removeMoveReceiver(worldUpdater);
		modelRoot.getUserMessageLogger().println(
				Resources.get("Connection to server closed"));
	}

	public synchronized void setConnection(ConnectionToServer c)
			throws IOException, GeneralSecurityException {
		setConnectionImpl(c);
	}

	/**
	 * This function may be entered from either the AWT event handler thread
	 * (via a local connection when the user clicks on something), or from the
	 * network connection thread, or from the initialisation thread of the
	 * launcher.
	 */
	private synchronized void setConnectionImpl(ConnectionToServer c)
			throws IOException, GeneralSecurityException {
		if (gameLoop != null) {
			gameLoop.stop();
		}

		if (connection != null) {
			closeConnection();
			connection.removeMoveReceiver(worldUpdater);
			connection.removeConnectionListener(this);
		}

		connection = c;
		connection.open();

		connection.addMoveReceiver(worldUpdater);
		connection.addConnectionListener(this);

		/* attempt to authenticate the player */
		modelRoot.getUserMessageLogger().println(
				Resources.get("Attempting to authenticate player: ")
						+ player.getName());
		authenticated = false;
		connection.sendCommand(new AddPlayerCommand(player, player.sign()));
		synchronized (authMutex) {
			if (!authenticated) {
				modelRoot.getUserMessageLogger().println(
						Resources.get("Waiting for authentication"));

				try {
					authMutex.wait();
				} catch (InterruptedException e) {
					// ignore
				}

				if (!authenticated) {
					throw new GeneralSecurityException("Server rejected "
							+ "attempt to authenticate");
				}
			}
		} // synchronized (authMutex)

		/*
		 * grab the lock on the WorldUpdater in order to prevent any moves from
		 * the server being lost whilst we plumb it in
		 */
		synchronized (worldUpdater) {
			world = connection.loadWorldFromServer();

			/* plumb in a new Move Executer */
			moveExecuter = new NonAuthoritativeMoveExecuter(world,
					moveReceiver, modelRoot);
			worldUpdater.setMoveReceiver(moveExecuter);
			uncommittedReceiver = moveExecuter.getUncommittedMoveReceiver();
			((NonAuthoritativeMoveExecuter.PendingQueue) uncommittedReceiver)
					.addMoveReceiver(connection);
		}

		/* start a new game loop */
		gameLoop = new GameLoop(guiRoot.getScreenHandler(), moveExecuter);

		/* send a command to set up server-specific resources */
		connection.sendCommand(new ResourceBundleManager.GetResourceCommand(
				"org.railz.data.l10n.server", Locale.getDefault()));

		playerConfirmed();
	}

	private void playerConfirmed() {
		try {
			/* create the models */
			assert world != null;

			modelRoot.setWorld(world);
			ViewLists viewLists = new ViewListsImpl(modelRoot, guiRoot,
					progressMonitor);

			if (!viewLists.validate(world)) {
				/*
				 * most likely reason for failure is that the server's object
				 * set is different to what the client is expecting
				 */
				modelRoot.getUserMessageLogger().println(
						Resources.get("Your client is not compatible with "
								+ "the server."));
			}

			/*
			 * wait until the player the client represents has been created in
			 * the model (this may not occur until we process the move creating
			 * the player from the server
			 */
			while (!world.boundsContain(KEY.PLAYERS,
					((PlayerPrincipal) modelRoot.getPlayerPrincipal()).getId(),
					modelRoot.getPlayerPrincipal())) {
				moveExecuter.update();
			}

			modelRoot.setWorld(this, viewLists);

			/* start the game loop */
			String threadName = "Railz client: " + guiClient.getTitle();
			Thread t = new Thread(gameLoop, threadName);
			t.start();
		} catch (IOException e) {
			String s = Resources
					.get("There was a problem reading in the graphics "
							+ "data");
			modelRoot.getUserMessageLogger().println(s);
			logger.log(Level.WARNING, s, e);
		}
	}

	public void setMoveReceiver(MoveReceiver m) {
		// moveReceiver = new CompositeMoveSplitter(m);
		// I don't want moves split at this stage since I want to be able
		// to listen for composite moves.
		moveReceiver = m;
	}

	@Override
	public void connectionClosed(ConnectionToServer c) {
		// ignore
	}

	@Override
	public void connectionOpened(ConnectionToServer c) {
		// ignore
	}

	@Override
	public void connectionStateChanged(ConnectionToServer c) {
		// ignore
	}

	@Override
	public void processServerCommand(ConnectionToServer c, ServerCommand s) {
		if (s instanceof AddPlayerResponseCommand) {
			synchronized (authMutex) {
				authenticated = !((AddPlayerResponseCommand) s).isRejected();

				if (authenticated) {
					modelRoot
							.getUserMessageLogger()
							.println(
									Resources
											.get("Player was successfully authenticated"));
					modelRoot.setPlayerPrincipal(((AddPlayerResponseCommand) s)
							.getPrincipal());
				} else {
					modelRoot.getUserMessageLogger().println(
							Resources.get("Authentication was rejected"));
				}

				authMutex.notify();
			}
		} else if (s instanceof WorldChangedCommand) {
			Runnable r = new ConnectionHelper(c);
			(new Thread(r)).start();
		} else if (s instanceof ServerMessageCommand) {
			modelRoot.getUserMessageLogger().println(
					Resources.get(((ServerMessageCommand) s).getMessage()));
		} else if (s instanceof ResourceBundleManager.GetResourceResponseCommand) {
			ResourceBundleManager.GetResourceResponseCommand response = (ResourceBundleManager.GetResourceResponseCommand) s;
			if (response.isSuccessful()) {
				Resources.setExternalResourceBundle(response
						.getResourceBundle());
			} else {
				logger.log(Level.WARNING, "Couldn't get resource bundle from "
						+ "server");
			}
		}
	}

	private class ConnectionHelper implements Runnable {
		ConnectionToServer c;

		public ConnectionHelper(ConnectionToServer c) {
			this.c = c;
		}

		@Override
		public void run() {
			try {
				setConnectionImpl(c);
			} catch (IOException e) {
				modelRoot.getUserMessageLogger().println(
						Resources.get("Unable to open remote connection"));
				closeConnection();
			} catch (GeneralSecurityException e) {
				modelRoot.getUserMessageLogger().println(
						Resources.get("Unable to authenticate with server: ")
								+ e.toString());
			}
		}
	}
}
