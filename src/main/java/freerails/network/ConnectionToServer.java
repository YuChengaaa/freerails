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

package freerails.network;

import java.io.IOException;
import java.io.Serializable;

/**
 * Defines the methods a client can use to send messages to the server.
 */
public interface ConnectionToServer {
    /**
     * Returns true if this connection is open.
     */
    boolean isOpen();

    /**
     * Returns an array containing all the objects read from the server since
     * the last time this method or waitForObjectFromServer() was called, if no
     * objects have been received, it returns an empty array rather than
     * blocking.
     */
    Serializable[] readFromServer() throws IOException;

    /**
     * Returns the next object read from the server, blocking if none is available.
     */
    Serializable waitForObjectFromServer() throws IOException, InterruptedException;

    /**
     * Sends the specified object to the server.
     */
    void writeToServer(Serializable object) throws IOException;

    /**
     * Disconnect from the server. When this method returns, calling isOpen() on
     * this object returns false <b>and</b> calling isOpen() on the
     * corresponding ConnectionToClient held by the server also returns false.
     */
    void disconnect() throws IOException;

    /**
     * @return
     */
    String getServerDetails();
}