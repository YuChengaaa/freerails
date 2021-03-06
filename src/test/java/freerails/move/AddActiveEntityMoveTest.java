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

import freerails.model.Activity;
import freerails.model.world.TestActivity;
import freerails.model.player.FreerailsPrincipal;

/**
 *
 */
public class AddActiveEntityMoveTest extends AbstractMoveTestCase {

    /**
     *
     */
    public void testMove() {
        FreerailsPrincipal principal = getPrincipal();
        Activity a = new TestActivity(50);
        AddActiveEntityMove move = new AddActiveEntityMove(a, 0, principal);
        assertSurvivesSerialisation(move);
        assertOkButNotRepeatable(move);
        AddActiveEntityMove move2 = new AddActiveEntityMove(a, 2, principal);
        assertTryMoveFails(move2);
    }

}
