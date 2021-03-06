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
package freerails.model.train;

import freerails.model.world.SharedKey;
import freerails.model.world.World;
import freerails.model.finances.Money;

/**
 * Adds hard coded wagon and engine types to the World. Later the
 * wagon and engine types will be defined in an xml file, but this will do for
 * now.
 */
public class WagonAndEngineTypesFactory {

    /**
     * @param world
     */
    public static void addTypesToWorld(World world) {
        // Wagon types
        WagonType[] wagonTypes = new WagonType[]{new WagonType("Mail", WagonType.MAIL), new WagonType("Passengers", WagonType.PASSENGER), new WagonType("Livestock", WagonType.FAST_FREIGHT), new WagonType("Coffee", WagonType.SLOW_FREIGHT), new WagonType("Wood", WagonType.BULK_FREIGHT),};

        for (WagonType wagonType : wagonTypes) {
            world.add(SharedKey.WagonTypes, wagonType);
        }

        // Engine types
        EngineType[] engineTypes = new EngineType[]{new EngineType("Grasshopper", 1000, new Money(10000), 10, new Money(100)), new EngineType("Norris", 1000, new Money(10000), 15, new Money(100)),};

        for (EngineType engineType : engineTypes) {
            world.add(SharedKey.EngineTypes, engineType);
        }
    }
}