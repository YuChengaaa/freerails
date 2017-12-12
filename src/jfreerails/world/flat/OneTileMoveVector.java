
/*
* OneTileMoveVector.java
*
* Created on 11 July 2001, 12:09
*/
package jfreerails.world.flat;

import java.awt.Dimension;
import java.awt.Point;
import java.io.ObjectStreamException;

import jfreerails.world.std_track.TrackSectionVector;

/**
*
* @author  Luke Lindsay
* @version 
*/

/** This class encapsulates movements from a tile to any one of the surrounding eight tiles.
*/

final public class OneTileMoveVector
	implements FlatTrackTemplate, TrackSectionVector  {

	/** North   */
	public static final OneTileMoveVector NORTH;

	/** West    */
	public static final OneTileMoveVector WEST;

	/** South East    */
	public static final OneTileMoveVector SOUTH_EAST;

	/** North-East    */
	public static final OneTileMoveVector NORTH_EAST;

	/** East    */
	public static final OneTileMoveVector EAST;

	/** South    */
	public static final OneTileMoveVector SOUTH;

	/** South West    */
	public static final OneTileMoveVector SOUTH_WEST;

	/** North West    */
	public static final OneTileMoveVector NORTH_WEST;

	private static OneTileMoveVector[][] vectors;

	private static OneTileMoveVector[] list;

	static {
		int t = 1;

		vectors = new OneTileMoveVector[3][3];
		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				if ((0 != x) || (0 != y)) {
					vectors[x + 1][y + 1] = new OneTileMoveVector(x, y, t);
				}
				t = t << 1;
			}
		}
		NORTH = getInstance(0, -1);
		WEST = getInstance(-1, 0);
		SOUTH_EAST = getInstance(1, 1);
		NORTH_EAST = getInstance(1, -1);
		EAST = getInstance(1, 0);
		SOUTH = getInstance(0, 1);
		SOUTH_WEST = getInstance(-1, 1);
		NORTH_WEST = getInstance(-1, -1);

		list = new OneTileMoveVector[8];
		list[0] = NORTH;
		list[1] = NORTH_EAST;
		list[2] = EAST;
		list[3] = SOUTH_EAST;

		list[4] = SOUTH;
		list[5] = SOUTH_WEST;
		list[6] = WEST;
		list[7] = NORTH_WEST;
	}

	/** The X and Y components of the vector.    */
	public final int deltaX, deltaY;
	private final int template;
	private final int length;

	/** Returns the X component of the vector.    */

	public int getX() {
		return deltaX;
	}

	/** Returns the Y component of the vector.    */

	public int getY() {
		return deltaY;
	}

	/** Returns a new oneTileMoveVector whose direction is
	* opposite to that the current one.
	* @return A oneTileMoveVector.
	*/

	public OneTileMoveVector getOpposite() {
		return getInstance(this.deltaX * -1, this.deltaY * -1);
	}

	public FlatTrackTemplate getRotatedInstance(Rotation r) {
		for (int i = 0; i < 8; i++) {
			if (this == list[i]) {
				return list[(i + r.i) % 8];
			}
		}
		return null;
	}

	/** Returns the name of the vector.  E.g. "north-east"
	* @return the name.
	*/

	public String getVectorName() {
		String name;
		switch (deltaY) {
			case 1 :
				name = " south";
				break;

			case -1 :
				name = " north";
				break;

			default :
				name = "";
				break;
		}
		switch (deltaX) {
			case 1 :
				name += " east";
				break;

			case -1 :
				name += " west";
				break;

			default :
				break;
		}
		return name;
	}

	/** Create a new OneTileMoveVector.  
	 *N.B Private constuctor to enforce enum property, use getInstance(x,y) instead.
	 *Pass values for delta X and Y: they must be in the range -1 to 1 and cannot both be equal to 0.
	* @param x Tile coordinate.
	* @param y Tile coordinate    
	*/
	private OneTileMoveVector(int x, int y, int t) {

		deltaX = x;
		deltaY = y;
		template = t;
		int sumOfSquares = (x * x * 100 * 100 + y * y * 100 * 100);
		length = (int) Math.sqrt((double) sumOfSquares);
	}

	public static OneTileMoveVector getInstance(int x, int y) {

		if ((((x < -1) || (x > 1)) || ((y < -1) || (y > 1)))
			|| ((x == 0) && (y == 0))) {
			throw new IllegalArgumentException("The values passed both must be integers in the range -1 to 1, and not both equal 0.");
		} else {

			return vectors[x + 1][y + 1];
		}
	}
	/** Returns true if the values passed could be used to create a valid vector.
	*/

	public static boolean checkValidity(int x, int y) {
		if ((((x < -1) || (x > 1)) || ((y < -1) || (y > 1)))
			|| ((x == 0) && (y == 0))) {
			return false;
		} else {
			return true;
		}
	}
	public Point createRelocatedPoint(Point from) {
		return new Point(from.x + deltaX, from.y + deltaY);
	}
	public boolean contains(FlatTrackTemplate ftt) {
		if (ftt.getTemplate() == this.template) {
			return true;
		} else {
			return false;
		}
	}

	public int getTemplate() {
		return template;
	}
	public static OneTileMoveVector[] getList() {
		return (OneTileMoveVector[]) list.clone(); //defensive copy.
	}
	public int getLength() {
		return length;
	}
	public Point getPosition(int distanceTravelled, Dimension tileSize) {
		return null;
	}
	public double getDirection() {

		int i = 0;
		
		while (this != list[i]) {
			i++;
		}
		return 2 * Math.PI / 8 * i;
	}
	
	public int getNumber(){
		int i=0;
		while(this!=list[i]){
			i++;
		}
		return i;		
	}
	
	private Object readResolve() throws ObjectStreamException {		
    	return OneTileMoveVector.getInstance(this.deltaX, this.deltaY);	
    }
    
    public static OneTileMoveVector getNearestVector(int dx, int dy){
        if(0==dx*dy){
            if(dx>0){
                return EAST;
            }else if(dx<0){
                return WEST;
            }else if(dy>0){
                return SOUTH;
            }else{
                return NORTH;
            }               
        }
        
        double gradient = dy;
        gradient=gradient/dx;        
        double B = 2;
        double A = 0.5;
        double C = -2;
        double D = -0.5;
        if(gradient>B){
            if(dy<0){
                return NORTH;
            }else{
                return SOUTH;
            }
        }else if(gradient > A){
            if(dy>0){
                return SOUTH_EAST;
            }else{
                return NORTH_WEST;
            }
        }
        
        
        else if(gradient > D){
            if(dx>0){
                return EAST;
            }else{
                return WEST;
            }
        }else if(gradient > C){
            if(dx<0){
                return SOUTH_WEST;
            }else{
                return NORTH_EAST;
            }
        }else{
            if(dy>0){
                return SOUTH;
            }else{
                return NORTH;
            }
        }
        
    }

}
