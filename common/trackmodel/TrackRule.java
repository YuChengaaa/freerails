
/*
* TrackRule.java
*
* Created on 15 July 2001, 19:53
*/
package jfreerails.common.trackmodel;
import jfreerails.common.OneTileMoveVector;
import jfreerails.common.TileModel;
import jfreerails.common.exception.FreerailsException;
import jfreerails.common.IntPoint;
import jfreerails.common.trackmodel.EightRotationsOfTrackPieceProducer;
import java.util.*;

/**
* This class encapsulates the rules that apply to a type of track node.  
* They concern: the legal routes trains can travel across the node, whether 
*the node's track can be doubled, on which terrain types it can be built, and the
*maximum number of consecutive nodes of this type (used for bridges and tunnels).
*
* @author  Luke Lindsay
* @version 
*/


public class TrackRule extends java.lang.Object {

    public int[][] legalRoutesAcrossNodeTemplates;

    private boolean enableDoubleTrack;

    private boolean station = false;

    private boolean signalTower = false;

    private HashSet canOnlyBuildOnTheseTerrainTypes = null;

    
    /*Track templates are 9 bit values, so there are 512 possible templates.
    *If legalTrackTemplate[x]==true, then x is a legal track-template.
    *Example:
    *000
    *111
    *000
    *This represents a horizontal straight.
    */
    private boolean[] legalTrackTemplates = new boolean[ 512 ];

    private int maximumConsecutivePieces = -1; //-1 signifies no maximum.  

    private HashSet cannotBuildOnTheseTerrainTypes = null;

    private int rGBvalue = 0;

    private String typeName;

    private int ruleNumber;
    
    public void setRGBvalue( int rGBvalue ) {
        this.rGBvalue = rGBvalue;
    }
    
    public OneTileMoveVector[] getLegalRoutes( OneTileMoveVector directionComingFrom ) {
        
        //TODO add code..
        return null;
    }
    
    public boolean isAStation() {
        return station;
    }
    
    public boolean testTrackPieceLegality( int trackTemplateToTest ) throws FreerailsException {
        
        //Check the values we have been passed for errors.
        if( ( trackTemplateToTest > 511 ) || ( trackTemplateToTest < 0 ) ) {
            throw new FreerailsException( "trackTemplate = " + trackTemplateToTest + ", it should be in the range 0-511" );
        }
        return legalTrackTemplates[ trackTemplateToTest ];
    }
    
    public void setTypeName( String name ) {
        this.typeName = name;
    }
    
    public void setEnableDoubleTrack( boolean dt ) {
        this.enableDoubleTrack = dt;
    }
    
    public int getRuleNumber() {
        return this.ruleNumber;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public void setStation( boolean station ) {
        this.station = station;
    }
    
    public int getMaximumConsecutivePieces() {
        return maximumConsecutivePieces;
    }
    
    public void setMaximumConsecutivePieces( int maximumConsecutivePieces ) {
        this.maximumConsecutivePieces = maximumConsecutivePieces;
    }
    
    public boolean isASignalTower() {
        return signalTower;
    }
    
    public boolean canBuildOnThisTerrainType( String TerrainType ) {
        if( null != canOnlyBuildOnTheseTerrainTypes ) {
            if( canOnlyBuildOnTheseTerrainTypes.contains( TerrainType ) ) {
                return true;
            }
            else {
                return false;
            }
        }
        if( null != cannotBuildOnTheseTerrainTypes ) {
            if( cannotBuildOnTheseTerrainTypes.contains( TerrainType ) ) {
                return false;
            }
            else {
                return true;
            }
        }
        System.out.println( "Warning: both cannotBuildOnTheseTerrainTypes and canOnlyBuildOnTheseTerrainTypes equals null for track type " + typeName );
        return true;
    }
    
    /** Creates new TrackRule */
    
    public TrackRule( int[] trackTemplatesPrototypes, int[][] legalRoutesAcrossNodeTemplatePrototypes, int ruleNumber ) throws FreerailsException {
        this.ruleNumber = ruleNumber;
        for( int  i = 0;i < trackTemplatesPrototypes.length;i++ ) {
            
            /* Check for invalid parameters. */
            if( ( trackTemplatesPrototypes[ i ] > 511 ) || ( trackTemplatesPrototypes[ i ] < 0 ) ) {
                throw new FreerailsException( "trackTemplate = " + trackTemplatesPrototypes[ i ] + ", it should be in the range 0-511" );
            }
            
            //TODO uncomment and add code.
            
            //for( int  k = 0;k < legalRoutesAcrossNodeTemplates.length;k++ ) {
            
            //    if( legalRoutesAcrossNodeTemplatePrototypes[ k ] != ( legalRoutesAcrossNodeTemplatePrototypes[ k ] & trackTemplatesPrototypes[i]  ) ) {
            
            //        throw new FreerailsException( "Route template: " + legalRoutesAcrossNodeTemplates[ k ] + " is not a subset of the track template: " + trackTemplate + "  It should be!" );
            
            //    }
            
            //}
            for( int  j = 0;j < trackTemplatesPrototypes.length;j++ ) {
                int[]  rotationsOfTrackTemplate = EightRotationsOfTrackPieceProducer.getRotations( trackTemplatesPrototypes[ j ] );
                for( int  k = 0;k < rotationsOfTrackTemplate.length;k++ ) {
                    if( legalTrackTemplates[ rotationsOfTrackTemplate[ k ] ] == false ) {
                        legalTrackTemplates[ rotationsOfTrackTemplate[ k ] ] = true;
                    }
                }
            }
        }
    }
    
    public void setCannotBuildOnTheseTerrainTypes( HashSet terrainTypes ) {
        cannotBuildOnTheseTerrainTypes = terrainTypes;
        canOnlyBuildOnTheseTerrainTypes = null;
    }
    
    public void setCanOnlyBuildOnTheseTerrainTypes( HashSet terrainTypes ) {
        canOnlyBuildOnTheseTerrainTypes = terrainTypes;
        cannotBuildOnTheseTerrainTypes = null;
    }
    
    public boolean isDoubleTrackEnabled() {
        return enableDoubleTrack;
    }
    
    public void setSignalTower( boolean signalTower ) {
        this.signalTower = signalTower;
    }
}
