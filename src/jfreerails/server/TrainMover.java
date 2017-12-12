package jfreerails.server;

import java.util.Random;
import jfreerails.controller.FreerailsServerSerializable;
import jfreerails.move.ChangeTrainPositionMove;
import jfreerails.move.InitialiseTrainPositionMove;
import jfreerails.move.Move;
import jfreerails.world.common.FreerailsPathIterator;
import jfreerails.world.top.KEY;
import jfreerails.world.top.ReadOnlyWorld;
import jfreerails.world.train.PathWalker;
import jfreerails.world.train.PathWalkerImpl;
import jfreerails.world.train.TrainModel;
import jfreerails.world.train.TrainPathIterator;
import jfreerails.world.train.TrainPositionOnMap;


/**
 * Responsible for moving the trains.
 *
 * @author Luke Lindsay 27-Oct-2002
 *
 */
public class TrainMover implements FreerailsServerSerializable {
    //final FreerailsPathIterator path;
    static Random rand = new Random(System.currentTimeMillis());
    final PathWalker walker;
    final int trainNumber;
    final ReadOnlyWorld w;
    final TrainPathFinder trainPathFinder;

    /**
     * Constructor.
     * @param from TrainPathIterator describing initial train position.
     * @param to TrainPathIterator - not sure why we need this second
     * parameter since it is initialised with the same info as the 1st....
     */
    public TrainMover(FreerailsPathIterator to, ReadOnlyWorld world, int trainNo) {
        this.trainNumber = trainNo;
        this.w = world;
        walker = new PathWalkerImpl(to);
        trainPathFinder = null;
    }

    public TrainMover(TrainPathFinder pathFinder, ReadOnlyWorld world,
        int trainNo) {
        this.trainNumber = trainNo;
        this.w = world;

        FreerailsPathIterator to = new TrainPathIterator(pathFinder);
        walker = new PathWalkerImpl(to);
        this.trainPathFinder = pathFinder;
    }

    public Move setInitialTrainPosition(TrainModel train,
        FreerailsPathIterator from) {
        int trainLength = train.getLength();
        PathWalker fromPathWalker = new PathWalkerImpl(from);
        fromPathWalker.stepForward(trainLength);

        TrainPositionOnMap initialPosition = TrainPositionOnMap.createInSameDirectionAsPath(fromPathWalker);

        return new InitialiseTrainPositionMove(trainNumber, initialPosition);
    }

    public PathWalker getWalker() {
        return walker;
    }

    public ChangeTrainPositionMove update(int distanceTravelled) {
        double distanceTravelledAsDouble = distanceTravelled;
        double distance = distanceTravelledAsDouble * getTrainSpeed();
        walker.stepForward(distance);

        return ChangeTrainPositionMove.generate(w, walker, trainNumber);
    }

    public double getTrainSpeed() {
        TrainModel train = (TrainModel)w.get(KEY.TRAINS, trainNumber);
        int trainLength = train.getNumberOfWagons();

        //For now train speeds are hard coded.
        switch (trainLength) {
        case 0:
            return 1;

        case 1:
            return 0.8;

        case 2:
            return 0.6;

        case 3:
            return 0.4;

        case 4:
            return 0.3;

        default:
            return 0.2;
        }
    }

    public TrainPathFinder getTrainPathFinder() {
        return trainPathFinder;
    }
}