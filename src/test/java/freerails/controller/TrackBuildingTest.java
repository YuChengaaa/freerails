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

import freerails.client.common.ModelRootImpl;
import freerails.move.MoveStatus;
import freerails.server.MapFixtureFactory2;
import freerails.util.Point2D;
import freerails.world.terrain.TileTransition;
import freerails.world.World;
import freerails.world.player.FreerailsPrincipal;
import freerails.world.terrain.FullTerrainTile;
import freerails.world.track.NullTrackType;
import freerails.world.track.TrackPiece;
import junit.framework.TestCase;

import java.util.Arrays;

/**
 *
 */
public class TrackBuildingTest extends TestCase {

    World w;

    TrackMoveProducer producer;

    TrackPathFinder pathFinder;

    StationBuilder stationBuilder;

    BuildTrackStrategy bts;

    /**
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        w = MapFixtureFactory2.getCopy();
        MoveExecutor me = new SimpleMoveExecutor(w, 0);
        ModelRoot mr = new ModelRootImpl();
        producer = new TrackMoveProducer(me, w, mr);
        FreerailsPrincipal principle = w.getPlayer(0).getPrincipal();
        pathFinder = new TrackPathFinder(w, principle);
        stationBuilder = new StationBuilder(me);
        bts = BuildTrackStrategy.getDefault(w);
    }

    /**
     * Tests building track from 5,5 to 10,5
     */
    public void testBuildingStraight() {

        Point2D from = new Point2D(5, 5);
        Point2D to = new Point2D(10, 5);
        try {
            // Check there is no track before we build it.
            for (int x = 5; x <= 10; x++) {
                TrackPiece tp = ((FullTerrainTile) w.getTile(x, 5))
                        .getTrackPiece();
                assertEquals(NullTrackType.NULL_TRACK_TYPE_RULE_NUMBER, tp
                        .getTrackTypeID());
            }
            pathFinder.setupSearch(from, to, bts);
            pathFinder.search(-1);
            assertEquals(pathFinder.getStatus(),
                    IncrementalPathFinder.PATH_FOUND);
            TileTransition[] path = pathFinder.pathAsVectors();
            assertEquals(path.length, 5);
            for (int i = 0; i < 5; i++) {
                assertEquals(TileTransition.EAST, path[i]);
            }
            MoveStatus ms = producer.buildTrack(from, path);
            assertTrue(ms.message, ms.ok);
            // Check track has been built.
            for (int x = 5; x <= 10; x++) {
                TrackPiece tp = ((FullTerrainTile) w.getTile(x, 5))
                        .getTrackPiece();
                assertEquals(0, tp.getTrackTypeID());
            }
        } catch (PathNotFoundException e) {
            fail();
        }

    }

    /**
     * Tests building track from 5,5 to 6,5
     */
    public void testBuildingOneTrackPiece() {

        Point2D from = new Point2D(5, 5);
        Point2D to = new Point2D(6, 5);
        try {
            // Check there is no track before we build it.

            TrackPiece tp1 = ((FullTerrainTile) w.getTile(5, 5)).getTrackPiece();
            assertEquals(NullTrackType.NULL_TRACK_TYPE_RULE_NUMBER, tp1
                    .getTrackTypeID());

            TrackPiece tp2 = ((FullTerrainTile) w.getTile(6, 5)).getTrackPiece();
            assertEquals(NullTrackType.NULL_TRACK_TYPE_RULE_NUMBER, tp2
                    .getTrackTypeID());

            pathFinder.setupSearch(from, to, bts);
            pathFinder.search(-1);
            assertEquals(pathFinder.getStatus(),
                    IncrementalPathFinder.PATH_FOUND);
            TileTransition[] path = pathFinder.pathAsVectors();
            assertEquals(path.length, 1);

            assertEquals(TileTransition.EAST, path[0]);

            MoveStatus ms = producer.buildTrack(from, path);
            assertTrue(ms.message, ms.ok);
            // Check track has been built.
            tp1 = ((FullTerrainTile) w.getTile(5, 5)).getTrackPiece();
            assertEquals(0, tp1.getTrackTypeID());

            tp2 = ((FullTerrainTile) w.getTile(6, 5)).getTrackPiece();
            assertEquals(0, tp2.getTrackTypeID());
        } catch (PathNotFoundException e) {
            fail();
        }

    }

    /**
     * There is a bug where if a section of track has a terminal on the end, you
     * cannot extend the track through the terminal. Instead, the track path
     * finder finds a route that misses out the terminal.
     */
    public void testTerminalProblem() {
        try {
            Point2D from = new Point2D(5, 5);
            TileTransition[] path = {TileTransition.EAST, TileTransition.EAST, TileTransition.EAST};
            MoveStatus ms = producer.buildTrack(from, path);
            assertTrue(ms.ok);
            int terminalStationType = stationBuilder.getTrackTypeID("terminal");
            stationBuilder.setStationType(terminalStationType);
            ms = stationBuilder.buildStation(new Point2D(8, 5));
            assertTrue(ms.ok);
            pathFinder.setupSearch(new Point2D(7, 5), new Point2D(9, 5), bts);
            pathFinder.search(-1);
            path = pathFinder.pathAsVectors();
            assertEquals(2, path.length);
            TileTransition[] expectedPath = {TileTransition.EAST, TileTransition.EAST};
            assertTrue(Arrays.equals(expectedPath, path));
        } catch (PathNotFoundException e) {
            fail();
        }
    }

    /**
     * There is a bug where if you build a straight section of double track
     * going E, then move the cursor to the end and attempt to build more double
     * track going SE, the track path finder builds a loop rather than just
     * building track going SE
     */
    public void testDoubleTrackProblem() {
        try {

            int trackTypeID = stationBuilder.getTrackTypeID("double track");
            bts = BuildTrackStrategy.getSingleRuleInstance(trackTypeID, w);
            producer.setBuildTrackStrategy(bts);
            Point2D a = new Point2D(5, 5);
            Point2D b = new Point2D(6, 5);
            Point2D c = new Point2D(7, 6);

            pathFinder.setupSearch(a, b, bts);
            pathFinder.search(-1);
            TileTransition[] path = pathFinder.pathAsVectors();
            TileTransition[] expectedPath = {TileTransition.EAST};
            assertTrue(Arrays.equals(expectedPath, path));
            MoveStatus ms = producer.buildTrack(a, path);
            assertTrue(ms.ok);

            TrackPiece tp = ((FullTerrainTile) w.getTile(b.x, b.y))
                    .getTrackPiece();
            assertEquals("We just build double track here.", trackTypeID, tp
                    .getTrackTypeID());

            pathFinder.setupSearch(b, c, bts);
            pathFinder.search(-1);
            path = pathFinder.pathAsVectors();
            assertEquals(1, path.length);

            expectedPath = new TileTransition[]{TileTransition.SOUTH_EAST};
            assertTrue(Arrays.equals(expectedPath, path));
        } catch (PathNotFoundException e) {
            fail();
        }
    }

    /**
     * There is a bug where if you try to start building track on a 90 degree
     * bend, no track path is found even when one should exist.
     */
    public void testStartSearchOnSharpCurve() {
        try {
            Point2D from = new Point2D(5, 5);
            TileTransition[] path = {TileTransition.EAST, TileTransition.SOUTH};
            MoveStatus ms = producer.buildTrack(from, path);
            assertTrue(ms.ok);
            pathFinder.setupSearch(new Point2D(6, 5), new Point2D(6, 7), bts);
            pathFinder.search(-1);
            path = pathFinder.pathAsVectors();
            assertEquals(2, path.length);
            assertEquals(TileTransition.SOUTH, path[0]);
            assertEquals(TileTransition.SOUTH, path[1]);
        } catch (PathNotFoundException e) {
            fail();
        }
    }

}
