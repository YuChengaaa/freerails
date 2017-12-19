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

package freerails.controller;

import freerails.world.FreerailsSerializable;

/**
 * An instance of this class is returned to the client (the server) when a
 * MessageToServer (MessageToClient) is executed by the server (the client).
 *
 * @see MessageToClient
 * @see MessageToServer
 */
public class MessageStatus implements FreerailsSerializable {
    private static final long serialVersionUID = 3257285842216103987L;

    private final int id;
    private final String reason;
    private final boolean successful;

    /**
     *
     * @param id
     * @param successful
     * @param reason
     */
    public MessageStatus(int id, boolean successful, String reason) {
        this.id = id;
        this.reason = reason;
        this.successful = successful;
    }

    /**
     *
     * @param id
     * @param successful
     */
    public MessageStatus(int id, boolean successful) {
        this.id = id;
        this.reason = null;
        this.successful = successful;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MessageStatus))
            return false;

        final MessageStatus messageStatus = (MessageStatus) o;

        if (id != messageStatus.id)
            return false;
        if (successful != messageStatus.successful)
            return false;
        return reason != null ? reason.equals(messageStatus.reason) : messageStatus.reason == null;
    }

    @Override
    public int hashCode() {
        int result;
        result = id;
        result = 29 * result + (reason != null ? reason.hashCode() : 0);
        result = 29 * result + (successful ? 1 : 0);
        return result;
    }

    /**
     * Returns the id of the command whose status this object stores.
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the reason the command failed, may be null.
     * @return 
     */
    public String getReason() {
        return reason;
    }

    /**
     * True if the command was successfully executed.
     * @return 
     */
    public boolean isSuccessful() {
        return successful;
    }
}