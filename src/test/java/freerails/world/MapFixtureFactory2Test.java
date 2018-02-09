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

package freerails.world;

import freerails.world.finances.ItemTransaction;
import freerails.world.finances.Money;
import freerails.world.finances.Transaction;
import freerails.world.finances.TransactionCategory;
import freerails.world.player.FreerailsPrincipal;
import junit.framework.TestCase;

/**
 */
public class MapFixtureFactory2Test extends TestCase {

    private World world;

    /**
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        world = MapFixtureFactory2.getCopy();
    }

    /**
     *
     */
    public void testGetCopy() {
        World w2;
        world = MapFixtureFactory2.getCopy();
        assertNotNull(world);
        w2 = MapFixtureFactory2.getCopy();
        assertNotNull(w2);
        assertNotSame(world, w2);
        assertEquals(world, w2);

    }

    /**
     *
     */
    public void testLists() {

        assertTrue(world.size(SKEY.CARGO_TYPES) > 0);
        assertTrue(world.size(SKEY.TRACK_RULES) > 0);
        assertTrue(world.size(SKEY.TERRAIN_TYPES) > 0);

    }

    /**
     *
     */
    public void testMap() {
        assertEquals(world.getMapWidth(), 50);
        assertEquals(world.getMapWidth(), 50);
    }

    /**
     *
     */
    public void testPlayers() {
        assertEquals(4, world.getNumberOfPlayers());
    }

    /**
     *
     */
    public void testThatStockIsIssued() {
        FreerailsPrincipal principal = world.getPlayer(0).getPrincipal();
        int stock = 0;
        Money cash = world.getCurrentBalance(principal);
        assertEquals(new Money(1000000), cash);
        int numberOfTransactions = world.getNumberOfTransactions(principal);
        assertTrue(numberOfTransactions > 0);
        for (int i = 0; i < numberOfTransactions; i++) {
            Transaction transaction = world.getTransaction(principal, i);
            if (transaction.getCategory() == TransactionCategory.ISSUE_STOCK) {
                ItemTransaction ait = (ItemTransaction) transaction;
                stock += ait.getQuantity();
            }
        }
        assertEquals(100000, stock);
    }
}