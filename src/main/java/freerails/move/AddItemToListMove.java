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

/*
 * Created on 13-Apr-2003
 *
 */
package freerails.move;

import freerails.world.FreerailsSerializable;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.top.KEY;
import freerails.world.top.World;

/**
 * All moves that add an item to a list should extend this class.
 *
 */
public class AddItemToListMove implements ListMove {
    private static final long serialVersionUID = 3256721779916747824L;

    private final KEY listKey;

    private final int index;

    private final FreerailsPrincipal principal;

    private final FreerailsSerializable item;

    /**
     *
     * @param key
     * @param i
     * @param item
     * @param p
     */
    public AddItemToListMove(KEY key, int i, FreerailsSerializable item,
                             FreerailsPrincipal p) {
        this.listKey = key;
        this.index = i;
        this.item = item;
        this.principal = p;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        int result;
        result = listKey.hashCode();
        result = 29 * result + index;
        result = 29 * result + principal.hashCode();
        result = 29 * result + (item != null ? item.hashCode() : 0);

        return result;
    }

    public KEY getKey() {
        return listKey;
    }

    public MoveStatus tryDoMove(World w, FreerailsPrincipal p) {
        if (w.size(this.principal, listKey) != index) {
            return MoveStatus.moveFailed("Expected size of "
                    + listKey.toString() + " list is " + index
                    + " but actual size is " + w.size(this.principal, listKey));
        }

        return MoveStatus.MOVE_OK;
    }

    public MoveStatus tryUndoMove(World w, FreerailsPrincipal p) {
        int expectListSize = index + 1;

        if (w.size(this.principal, listKey) != expectListSize) {
            return MoveStatus.moveFailed("Expected size of "
                    + listKey.toString() + " list is " + expectListSize
                    + " but actual size is " + w.size(this.principal, listKey));
        }

        return MoveStatus.MOVE_OK;
    }

    public MoveStatus doMove(World w, FreerailsPrincipal p) {
        MoveStatus ms = tryDoMove(w, p);

        if (ms.isOk()) {
            w.add(this.principal, listKey, this.item);
        }

        return ms;
    }

    public MoveStatus undoMove(World w, FreerailsPrincipal p) {
        MoveStatus ms = tryUndoMove(w, p);

        if (ms.isOk()) {
            w.removeLast(this.principal, listKey);
        }

        return ms;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AddItemToListMove) {
            AddItemToListMove test = (AddItemToListMove) o;

            if (null == this.item) {
                if (null != test.item) {
                    return false;
                }
            } else if (!this.item.equals(test.getAfter())) {
                return false;
            }

            if (this.index != test.index) {
                return false;
            }

            return this.listKey == test.listKey;
        }
        return false;
    }

    public FreerailsSerializable getBefore() {
        return null;
    }

    public FreerailsSerializable getAfter() {
        return item;
    }

    @Override
    public String toString() {

        return this.getClass().getName() + "\n list=" +
                listKey.toString() +
                "\n index =" +
                index +
                "\n item =" +
                item;
    }

    /**
     *
     * @return
     */
    public FreerailsPrincipal getPrincipal() {
        return principal;
    }
}