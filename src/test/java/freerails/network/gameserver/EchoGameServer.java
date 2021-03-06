/*
 * FreeRails
 * Copyright (C) 2000-2018 The FreeRails Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package freerails.network.gameserver;

import freerails.network.ConnectionToClient;
import freerails.server.GameServer;
import freerails.util.SynchronizedFlag;
import freerails.util.Utils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Implementation of GameServer that simply echoes whatever clients send it.
 */
public class EchoGameServer implements GameServer, Runnable {

    private static final Logger logger = Logger.getLogger(EchoGameServer.class.getName());
    private final Collection<ConnectionToClient> clientConnections = new ArrayList();
    private final SynchronizedFlag status = new SynchronizedFlag(false);
    private final Collection<Serializable> messagesToSend = new LinkedList<>();

    public EchoGameServer() {}

    /**
     * @param connection
     */
    public synchronized void addConnection(ConnectionToClient connection) {
        if (!status.isSet()) {
            throw new IllegalStateException();
        }
        clientConnections.add(Utils.verifyNotNull(connection));
    }

    /**
     * @return
     */
    public synchronized int getNumberOpenConnections() {
        clientConnections.removeIf(connection -> !connection.isOpen());
        return clientConnections.size();
    }

    public void run() {

        status.set();
        while (status.isSet()) {

            update();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {}
        }
    }

    private synchronized void sendMessage(Serializable m) {
        // Send messages.
        for (ConnectionToClient connection : clientConnections) {
            try {
                connection.writeToClient(m);
                logger.debug("Sent success: " + m);
            } catch (IOException e) {
                try {
                    if (connection.isOpen()) {
                        connection.disconnect();
                    }
                } catch (IOException e1) {
                    // hope this doesn't happen.
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     *
     */
    public void update() {

        synchronized (this) {
            // Read messages.
            for (ConnectionToClient connection : clientConnections) {
                try {
                    Serializable[] messages = connection.readFromClient();

                    Collections.addAll(messagesToSend, messages);
                } catch (IOException e) {
                    try {
                        if (connection.isOpen()) {
                            connection.disconnect();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            // Send messages.
            for (Serializable message : messagesToSend) {
                sendMessage(message);
            }
        }
    }

    public SynchronizedFlag getStatus() {
        return status;
    }
}