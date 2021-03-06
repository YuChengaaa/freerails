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
package freerails.controller;

import freerails.move.DropOffAndPickupCargoMoveGenerator;
import freerails.move.Move;
import freerails.move.MoveStatus;
import freerails.util.ImmutableList;
import freerails.util.Vec2D;
import freerails.model.world.FullWorld;
import freerails.model.world.PlayerKey;
import freerails.model.world.SharedKey;
import freerails.model.world.World;
import freerails.model.cargo.*;
import freerails.model.player.Player;
import freerails.model.station.StationDemand;
import freerails.model.station.Station;
import freerails.model.MapFixtureFactory;
import freerails.model.train.Train;
import junit.framework.TestCase;

/**
 * This Junit TestCase tests whether a train picks up and drops off the right
 * cargo at a station.
 */
public class DropOffAndPickupCargoMoveGeneratorTest extends TestCase {

    private final CargoBatch cargoType0FromStation2 = new CargoBatch(0, Vec2D.ZERO,0, 2);
    private final CargoBatch cargoType1FromStation2 = new CargoBatch(1, Vec2D.ZERO,0, 2);
    private final CargoBatch cargoType0FromStation0 = new CargoBatch(0,new Vec2D(0,0),0, 0);
    private World world;

    /**
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Set up the world object with three cargo types, one station, and one
        // train.
        world = new FullWorld();

        world.addPlayer(MapFixtureFactory.TEST_PLAYER);

        // set up the cargo types.
        world.add(SharedKey.CargoTypes, new CargoType(0, "Mail", CargoCategory.Mail));
        world.add(SharedKey.CargoTypes, new CargoType(0, "Passengers", CargoCategory.Passengers));
        world.add(SharedKey.CargoTypes, new CargoType(0, "Goods", CargoCategory.Fast_Freight));

        // Set up station
        int x = 10;
        int y = 10;
        int stationCargoBundleId = world.add(MapFixtureFactory.TEST_PRINCIPAL,
                PlayerKey.CargoBundles, ImmutableCargoBatchBundle.EMPTY_CARGO_BATCH_BUNDLE);
        String stationName = "Station 1";
        Station station = new Station(new Vec2D(x, y), stationName, world.size(SharedKey.CargoTypes), stationCargoBundleId);
        world.add(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Stations, station);

        // Set up train
        int trainCargoBundleId = world.add(MapFixtureFactory.TEST_PRINCIPAL,
                PlayerKey.CargoBundles, ImmutableCargoBatchBundle.EMPTY_CARGO_BATCH_BUNDLE);

        // 3 wagons to carry cargo type 0.
        ImmutableList<Integer> wagons = new ImmutableList<>(0, 0, 0);
        Train train = new Train(wagons, trainCargoBundleId);
        world.add(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Trains, train);
    }

    /**
     * Tests picking up cargo from a station.
     */
    public void testPickUpCargo1() {
        // Set up the variables for this test.
        MutableCargoBatchBundle cargoBundleWith2CarloadsOfCargo0 = new MutableCargoBatchBundle();

        // cargoBundleWith2CarloadsOfCargo0.setAmount(cargoType0FromStation2,
        // 2);
        cargoBundleWith2CarloadsOfCargo0.setAmount(cargoType0FromStation2, 80);

        assertEquals("There shouldn't be any cargo at the station yet",
                ImmutableCargoBatchBundle.EMPTY_CARGO_BATCH_BUNDLE, getCargoAtStation());
        assertEquals("There shouldn't be any cargo on the train yet",
                ImmutableCargoBatchBundle.EMPTY_CARGO_BATCH_BUNDLE, getCargoOnTrain());

        // Now add 2 carloads of cargo type 0 to the station.
        // getCargoAtStation().setAmount(cargoType0FromStation2, 2);
        setCargoAtStation(cargoType0FromStation2, 80);

        // The train should pick up this cargo, since it has three wagons
        // capable of carrying cargo type 0.
        stopAtStation();

        // The train should now have the two car loads of cargo and there should
        // be no cargo at the station.
        assertEquals("There should no longer be any cargo at the station",
                ImmutableCargoBatchBundle.EMPTY_CARGO_BATCH_BUNDLE, getCargoAtStation());
        assertEquals("The train should now have the two car loads of cargo",
                cargoBundleWith2CarloadsOfCargo0.toImmutableCargoBundle(), getCargoOnTrain());
    }

    /**
     * Tests picking up cargo when the there is too much cargo at the station
     * for the train to carry.
     */
    public void testPickUpCargo2() {
        setCargoAtStation(this.cargoType0FromStation2, 200);

        stopAtStation();

        // The train has 3 wagons, each wagon carries 40 units of cargo, so
        // the train should pickup 120 units of cargo.
        MutableCargoBatchBundle expectedOnTrain = new MutableCargoBatchBundle();
        expectedOnTrain.setAmount(this.cargoType0FromStation2, 120);

        // The remaining 80 units of cargo should be left at the station.
        MutableCargoBatchBundle expectedAtStation = new MutableCargoBatchBundle();
        expectedAtStation.setAmount(this.cargoType0FromStation2, 80);

        // Test the expected values against the actual values.
        assertEquals(expectedOnTrain.toImmutableCargoBundle(), getCargoOnTrain());
        assertEquals(expectedAtStation.toImmutableCargoBundle(), getCargoAtStation());
    }

    /**
     * Tests that a train takes into account how much cargo it already has and
     * the type of wagons it has when it is picking up cargo.
     */
    public void testPickUpCargo3() {
        ImmutableList<Integer> wagons = new ImmutableList<>(0, 0, 2, 2);

        // 2 wagons for cargo type 0; 2 wagons for cargo type 2.
        addWagons(wagons);

        // Set cargo on train.
        setCargoOnTrain(this.cargoType0FromStation2, 30);

        // Set cargo at station.
        setCargoAtStation(this.cargoType0FromStation0, 110);

        // Check that station does not demand cargo type 0.
        Station station = (Station) world.get(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Stations, 0);
        assertFalse(station.getDemandForCargo().isCargoDemanded(0));

        // Stop at station.
        stopAtStation();

        /*
         * The train has 2 wagons for cargo type 0 but had 30 units of cargo
         * type 0 before stopping so it can only pick up 50 units.
         */
        MutableCargoBatchBundle expectedAtStation = new MutableCargoBatchBundle();
        expectedAtStation.setAmount(cargoType0FromStation0, 60);

        MutableCargoBatchBundle expectedOnTrain = new MutableCargoBatchBundle();
        expectedOnTrain.setAmount(this.cargoType0FromStation2, 30);
        expectedOnTrain.setAmount(this.cargoType0FromStation0, 50);

        assertEquals(expectedAtStation.toImmutableCargoBundle(), getCargoAtStation());
        assertEquals(expectedOnTrain.toImmutableCargoBundle(), getCargoOnTrain());
    }

    /**
     * Tests that a train drops of cargo that a station demands and does not
     * drop off cargo that is not demanded unless it has to.
     */
    public void testDropOffCargo() {
        // Set the station to demand cargo type 0.
        Station station = (Station) world.get(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Stations, 0);
        StationDemand demand = new StationDemand(new boolean[]{true, false, false, false});
        station = new Station(station, demand);
        world.set(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Stations, 0, station);

        // Check that the station demands what we think it does.
        assertTrue("The station should demand cargo type 0.", station
                .getDemandForCargo().isCargoDemanded(0));
        assertFalse("The station shouldn't demand cargo type 1.", station
                .getDemandForCargo().isCargoDemanded(1));

        // Add 2 wagons for cargo type 0 and 1 for cargo type 1 to train.
        ImmutableList<Integer> wagons = new ImmutableList<>(0, 0, 1, 1);
        addWagons(wagons);

        // Add quantities of cargo type 0 and 2 to the train.
        setCargoOnTrain(this.cargoType0FromStation2, 50);
        setCargoOnTrain(this.cargoType1FromStation2, 40);

        stopAtStation();

        /*
         * The train should have dropped of the 50 units cargo of type 0 since
         * the station demands it but not the 40 units of cargo type 1 which is
         * does not demand.
         */
        MutableCargoBatchBundle expectedOnTrain = new MutableCargoBatchBundle();
        expectedOnTrain.setAmount(this.cargoType1FromStation2, 40);

        assertEquals(expectedOnTrain.toImmutableCargoBundle(), getCargoOnTrain());
        assertEquals(ImmutableCargoBatchBundle.EMPTY_CARGO_BATCH_BUNDLE, getCargoAtStation());

        // Now remove the wagons from the train.
        removeAllWagonsFromTrain();
        stopAtStation();

        /*
         * This time the train has no wagons, so has to drop the 40 units of
         * cargo type 1 even though the station does not demand it. Since ths
         * station does not demand it, it is added to the cargo waiting at the
         * station.
         */
        MutableCargoBatchBundle expectedAtStation = new MutableCargoBatchBundle();
        expectedAtStation.setAmount(this.cargoType1FromStation2, 40);

        assertEquals(expectedAtStation.toImmutableCargoBundle(), getCargoAtStation());
        assertEquals(ImmutableCargoBatchBundle.EMPTY_CARGO_BATCH_BUNDLE, getCargoOnTrain());
    }

    /**
     * Tests that a train does not drop cargo off at its station of origin
     * unless it has to.
     */
    public void testDontDropOffCargo() {
        // Set station to
        setCargoOnTrain(cargoType0FromStation0, 50);
        setCargoOnTrain(cargoType0FromStation2, 50);

        stopAtStation();

        // The train shouldn't have dropped anything off.
        MutableCargoBatchBundle expectedOnTrain = new MutableCargoBatchBundle();
        expectedOnTrain.setAmount(cargoType0FromStation0, 50);
        expectedOnTrain.setAmount(cargoType0FromStation2, 50);

        assertEquals(expectedOnTrain.toImmutableCargoBundle(), getCargoOnTrain());
        assertEquals(ImmutableCargoBatchBundle.EMPTY_CARGO_BATCH_BUNDLE, getCargoAtStation());

        // Now remove the wagons from the train.
        removeAllWagonsFromTrain();

        stopAtStation();

        /*
         * The train now has no wagons, so must drop off the cargo whether the
         * station demands it or not. Since the station does not demand it, the
         * cargo should get added to the cargo waiting at the station.
         */
        MutableCargoBatchBundle expectedAtStation = new MutableCargoBatchBundle();
        expectedAtStation.setAmount(cargoType0FromStation0, 50);
        expectedAtStation.setAmount(cargoType0FromStation2, 50);

        assertEquals(expectedAtStation.toImmutableCargoBundle(), getCargoAtStation());
        assertEquals(ImmutableCargoBatchBundle.EMPTY_CARGO_BATCH_BUNDLE, getCargoOnTrain());
    }

    /**
     * Tests that a train drops off any cargo before picking up cargo.
     */
    public void testPickUpAndDropOffSameCargoType() {
        // Set cargo at station and on train.
        setCargoOnTrain(this.cargoType0FromStation2, 120);
        setCargoAtStation(this.cargoType0FromStation0, 200);

        // Set station to demand cargo 0.
        Station station = (Station) world.get(
                MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Stations, 0);
        StationDemand demand = new StationDemand(new boolean[]{true, false, false, false});
        station = new Station(station, demand);
        world.set(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Stations, 0, station);

        assertTrue(station.getDemandForCargo().isCargoDemanded(0));
        stopAtStation();

        MutableCargoBatchBundle expectedOnTrain = new MutableCargoBatchBundle();
        expectedOnTrain.setAmount(this.cargoType0FromStation0, 120);

        MutableCargoBatchBundle expectedAtStation = new MutableCargoBatchBundle();
        expectedAtStation.setAmount(this.cargoType0FromStation0, 80);

        assertEquals(expectedOnTrain.toImmutableCargoBundle(), getCargoOnTrain());
        assertEquals(expectedAtStation.toImmutableCargoBundle(), getCargoAtStation());
    }

    private void removeAllWagonsFromTrain() {
        addWagons(new ImmutableList<>());
    }

    private void addWagons(ImmutableList<Integer> wagons) {
        Train train = (Train) world.get(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Trains, 0);
        Train newTrain = train.getNewInstance(train.getEngineType(), wagons);
        world.set(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Trains, 0, newTrain);
    }

    private void stopAtStation() {
        DropOffAndPickupCargoMoveGenerator moveGenerator = new DropOffAndPickupCargoMoveGenerator(
                0, 0, world, MapFixtureFactory.TEST_PRINCIPAL, false, false);
        Move move = moveGenerator.generateMove();
        if (null != move) {
            MoveStatus moveStatus = move.doMove(world, Player.AUTHORITATIVE);
            assertEquals(MoveStatus.MOVE_OK, moveStatus);
        }
    }

    /**
     * Retrieves the cargo bundle that is waiting at the station from the world
     * object.
     */
    private ImmutableCargoBatchBundle getCargoAtStation() {
        Station station = (Station) world.get(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Stations, 0);

        return (ImmutableCargoBatchBundle) world.get(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.CargoBundles, station.getCargoBundleID());
    }

    /**
     * Retrieves the cargo bundle that the train is carrying from the world
     * object.
     */
    private ImmutableCargoBatchBundle getCargoOnTrain() {
        Train train = (Train) world.get(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Trains, 0);

        return (ImmutableCargoBatchBundle) world.get(
                MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.CargoBundles, train.getCargoBundleID());
    }

    private void setCargoAtStation(CargoBatch cb, int amount) {
        Station station = (Station) world.get(
                MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Stations, 0);
        MutableCargoBatchBundle bundle = new MutableCargoBatchBundle(getCargoAtStation());
        bundle.setAmount(cb, amount);
        world.set(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.CargoBundles, station
                .getCargoBundleID(), bundle.toImmutableCargoBundle());
    }

    private void setCargoOnTrain(CargoBatch cb, int amount) {
        Train train = (Train) world.get(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.Trains, 0);
        MutableCargoBatchBundle bundle = new MutableCargoBatchBundle(getCargoOnTrain());
        bundle.setAmount(cb, amount);
        world.set(MapFixtureFactory.TEST_PRINCIPAL, PlayerKey.CargoBundles, train.getCargoBundleID(), bundle.toImmutableCargoBundle());
    }
}