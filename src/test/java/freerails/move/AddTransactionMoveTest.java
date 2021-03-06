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

import freerails.model.finances.*;
import freerails.model.player.Player;
import freerails.model.MapFixtureFactory;

/**
 *
 */
public class AddTransactionMoveTest extends AbstractMoveTestCase {

    /**
     *
     */
    public void testMove() {
        Money currentBalance = getWorld().getCurrentBalance(MapFixtureFactory.TEST_PRINCIPAL);
        assertEquals(new Money(0), currentBalance);

        Transaction transaction = new MoneyTransaction(new Money(100), TransactionCategory.MISC_INCOME);
        Move move1 = new AddTransactionMove(MapFixtureFactory.TEST_PRINCIPAL, transaction);
        assertTryMoveIsOk(move1);
        assertTryUndoMoveFails(move1);
        assertDoMoveIsOk(move1);
        currentBalance = getWorld().getCurrentBalance(MapFixtureFactory.TEST_PRINCIPAL);
        assertEquals(new Money(100), currentBalance);

        final Player PLAYER_WITHOUT_ACCOUNT = new Player("PLAYER_WITHOUT_ACCOUNT", 4);

        assertSurvivesSerialisation(move1);

        Move move2 = new AddTransactionMove(PLAYER_WITHOUT_ACCOUNT.getPrincipal(), transaction);
        assertTryMoveFails(move2);
        assertOkAndRepeatable(move1);
    }

    /**
     *
     */
    public void testConstrainedMove() {
        Money currentBalance = getWorld().getCurrentBalance(MapFixtureFactory.TEST_PRINCIPAL);
        assertEquals(new Money(0), currentBalance);

        Transaction transaction = new MoneyTransaction(new Money(-100), TransactionCategory.MISC_INCOME);
        Move move = new AddTransactionMove(MapFixtureFactory.TEST_PRINCIPAL, transaction, true);

        // This move should fail since there is no money in the account and it is constrained is set to true.
        assertTryMoveFails(move);
    }
}