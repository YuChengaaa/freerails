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
 *
 */
package freerails.move;

import freerails.model.*;
import freerails.model.game.GameTime;
import freerails.model.player.FreerailsPrincipal;
import freerails.model.world.TestActivity;
import freerails.model.world.World;

/**
 *
 */
public class NextActivityMoveTest extends AbstractMoveTestCase {

    /**
     *
     */
    public void testMove() {
        World world = getWorld();
        FreerailsPrincipal principal = getPrincipal();
        Activity act = new TestActivity(50);
        world.addActiveEntity(principal, act);

        Activity act2 = new TestActivity(60);
        Move move = new NextActivityMove(act2, 0, principal);
        assertSurvivesSerialisation(move);
        assertOkAndRepeatable(move);
    }

    /**
     *
     */
    public void testMove2() {
        World world = getWorld();
        FreerailsPrincipal principal = getPrincipal();
        Activity act = new TestActivity(50);
        world.addActiveEntity(principal, act);

        Activity act2 = new TestActivity(60);
        Move move = new NextActivityMove(act2, 0, principal);
        assertDoThenUndoLeavesWorldUnchanged(move);
    }

    /**
     *
     */
    public void testStackingOfActivities() {
        World world = getWorld();
        FreerailsPrincipal principal = getPrincipal();
        Activity act = new TestActivity(50);
        world.addActiveEntity(principal, act);

        Activity act2 = new TestActivity(60);
        Move move = new NextActivityMove(act2, 0, principal);
        assertDoMoveIsOk(move);

        GameTime currentTime = new GameTime(0);
        assertEquals(currentTime, world.currentTime());
        ActivityIterator it = world.getActivities(principal, 0);

        assertEquals(it.getActivity(), act);
        assertEquals(it.getStartTime(), currentTime.getTicks(), 0.00001);
        assertEquals(50.0d, it.getDuration(), 0.00001);
        assertEquals(50.0d, it.getFinishTime(), 0.00001);

        assertTrue(it.hasNext());
        it.nextActivity();
        assertEquals(it.getActivity(), act2);
        assertEquals(50, it.getStartTime(), 0.00001);
        assertEquals(60, it.getDuration(), 0.0001d);
        assertEquals(110, it.getFinishTime(), 0.00001);
    }

}
