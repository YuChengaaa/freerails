package jfreerails.world.track;

import jfreerails.world.misc.TrackSectionVector;
import java.awt.Dimension;
import java.awt.Point;
import jfreerails.world.misc.PointOnTile;
public class CompositeTrackSectionVectorImpl implements TrackSectionVector {
	public PointOnTile getEnd() {
		return null;
	}

	public PointOnTile getStart() {
		return null;
	}

	public int getLength() {
		return 0;
	}

    public Point getPosition(int distanceTravelled, Dimension tileSize
        ){ 
      throw new java.lang.UnsupportedOperationException("Method not implemented yet!");}

	private TrackSectionVector[] vectors;
}