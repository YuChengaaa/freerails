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
 * Created on 02-Jul-2005
 *
 */
package freerails.move;

import freerails.world.common.Activity;
import freerails.world.common.ActivityIterator;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.top.World;

/**
 * A move that adds an active entity. An active entity is something whose state
 * may be continually changing. An example is a train - it is an active entity
 * since while it is moving its position is continually changing.
 *
 * @see NextActivityMove
 */

public class AddActiveEntityMove implements Move {

    private static final long serialVersionUID = 8732702087937675013L;

    private final Activity activity;

    private final FreerailsPrincipal principal;

    private final int index;

    /**
     *
     * @param activity
     * @param index
     * @param principal
     */
    public AddActiveEntityMove(Activity activity, int index,
                               FreerailsPrincipal principal) {
        this.activity = activity;
        this.index = index;

        this.principal = principal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AddActiveEntityMove))
            return false;

        final AddActiveEntityMove addActiveEntityMove = (AddActiveEntityMove) o;

        if (index != addActiveEntityMove.index)
            return false;
        if (!activity.equals(addActiveEntityMove.activity))
            return false;

        return principal.equals(addActiveEntityMove.principal);
    }

    @Override
    public int hashCode() {
        int result;
        result = activity.hashCode();
        result = 29 * result + principal.hashCode();

        result = 29 * result + index;
        return result;
    }

    public MoveStatus tryDoMove(World w, FreerailsPrincipal p) {
        if (index != w.size(principal))
            return MoveStatus.moveFailed("index != w.size(listKey, p)");

        return MoveStatus.MOVE_OK;
    }

    public MoveStatus tryUndoMove(World w, FreerailsPrincipal p) {
        int expectedSize = index + 1;
        if (expectedSize != w.size(principal))
            return MoveStatus
                    .moveFailed("(index + 1) != w.size(listKey, principal)");

        ActivityIterator ai = w.getActivities(principal, index);
        if (ai.hasNext())
            return MoveStatus
                    .moveFailed("There should be exactly one activity!");

        Activity act = ai.getActivity();

        if (!act.equals(activity))
            return MoveStatus.moveFailed("Expected " + activity.toString()
                    + " but found " + act.toString());

        return MoveStatus.MOVE_OK;
    }

    public MoveStatus doMove(World w, FreerailsPrincipal p) {
        MoveStatus ms = tryDoMove(w, p);
        if (ms.ok)
            w.addActiveEntity(principal, activity);

        return ms;
    }

    public MoveStatus undoMove(World w, FreerailsPrincipal p) {
        MoveStatus ms = tryUndoMove(w, p);
        if (ms.ok)
            w.removeLastActiveEntity(principal);

        return ms;
    }

}
