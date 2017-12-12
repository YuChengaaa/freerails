package jfreerails.controller;

import java.util.LinkedList;

import jfreerails.move.CompositeMove;
import jfreerails.move.Move;
import jfreerails.move.MoveStatus;
import jfreerails.world.top.World;

/**
 * @version 	1.0
 * 
 * XXX How many MoveExecuters should there be?
 * * Have only one:
 * + Moves from clients are in definite order
 * - No current way to determine which move came from which client
 * - No way to enforce undos from originator client only
 * - Undos from a busy client can purge moves from a quiet one.
 * Have one for each client:
 * + Moves come from known source
 * - Overall order of moves across different clients is not stored.
 *
 * Perhaps "undo move" should be provided by client rather than server.
 *
 * Requirements for Undo Move:
 * *Move must already have been performed
 * *Undo must not succeed if circumstances have changed such that the undo is
 * not possible.
 * *Undo cannot be performed more than once
 * *Client must receive notification of change.
 * *Order of composite undo must be reversed wrt forwards move.
 * *Undo must only be executable by client which performed the move.
 * *Undos must remain in stack for sufficient time for human to manually perform
 * them
 *
 * TODO for network operation, this class must obtain a mutex on the World
 * whilst it processes moves. Due to the potentially large number of moves,
 * perhaps they should be queued and processed in bunches?
 */
final public class MoveExecuter implements UncommittedMoveReceiver {

	private static final int MAX_UNDOS = 10;
	
	private final World world;

	private final MoveReceiver moveReceiver;	

	private static final LinkedList moveStack = new LinkedList();

	private static MoveExecuter moveExecuter = null;
	
	private MoveExecuter(World w, MoveReceiver mr) {
		world = w;
		moveReceiver = mr;
	}

	/**
	 * Call this once before using this class
	 */
	public static void init(World w, MoveReceiver mr) {
		moveExecuter = new MoveExecuter(w, mr);
	}

	/**
	 * Submit Moves to the MoveExecuter returned from this method for
	 * execution, rather than calling the doMove() method directly.
	 * @return a publicly available MoveExecuter.
	 */
	public static MoveExecuter getMoveExecuter() {
	    return moveExecuter;
	}

	/*
	 * @see MoveReceiver#processMove(Move)
	 */
	public void processMove(Move move) {
	    moveStack.add(move);
	    if (moveStack.size() > MAX_UNDOS) {
		moveStack.removeFirst();
	    }
	    MoveStatus ms = move.doMove(world);
	    if (ms == MoveStatus.MOVE_OK) {
		forwardMove(move);
	    } else {
		System.out.println("Couldn't commit move: "+ms.message);
	    }
	}

	private void forwardMove(Move move) {
	    moveReceiver.processMove(move);
	    if (move instanceof CompositeMove) {
		Move[] moves = ((CompositeMove) move).getMoves();
		for (int i = 0; i < moves.length; i++) {
		    forwardMove(moves[i]);
		}
	    }
	}

	/**
	 * FIXME clients can undo each others moves
	 */
	public void undoLastMove() {
		if (moveStack.size() > 0) {
			Move m = (Move) moveStack.removeLast();
			MoveStatus ms = m.undoMove(world);
			if (ms != MoveStatus.MOVE_OK) {
			    System.out.println("Couldn't undo move!");
			    /* push it back on the stack to prevent further
			     * out-of-order undos */
			    moveStack.add(m);
			}

			forwardMove(m);

		} else {
			System.out.println("No moves on stack.");
		}
	}

}
