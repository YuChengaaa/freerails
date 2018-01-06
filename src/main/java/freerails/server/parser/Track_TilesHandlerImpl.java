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
package freerails.server.parser;

import freerails.world.SKEY;
import freerails.world.World;
import freerails.world.terrain.TerrainCategory;
import freerails.world.track.*;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Processes Track_TilesHandle events, generates track rules, and provides a
 * methods to add the track rules to the world object.
 *
 * @see Track_TilesParser
 */
// TODO difference between interface and implementation
public class Track_TilesHandlerImpl implements Track_TilesHandler {

    /**
     *
     */
    protected List<TrackRule> ruleList;

    /**
     *
     */
    protected freerails.world.track.TrackRuleProperties trackRuleProperties;

    /**
     *
     */
    protected ValidTrackConfigurations validTrackConfigurations;

    /**
     *
     */
    protected ArrayList<String> legalTemplates;

    /**
     *
     */
    protected HashSet<TerrainCategory> terrainTypes;

    /**
     *
     */
    protected ValidTrackPlacement validTrackPlacement;
    int maxConsequ;

    /**
     * @param trackXmlUrl
     */
    public Track_TilesHandlerImpl(java.net.URL trackXmlUrl) {
        try {
            Track_TilesParser.parse(trackXmlUrl, this);
        } catch (Exception ignored) {
        }
    }

    public void start_CanOnlyBuildOnTheseTerrainTypes(final Attributes meta) {
        terrainTypes = new HashSet<>();
    }

    public void end_CanOnlyBuildOnTheseTerrainTypes() {
        validTrackPlacement = new ValidTrackPlacement(terrainTypes,
                ValidTrackPlacement.PlacementRule.ONLY_ON_THESE);
        terrainTypes = null;
    }

    public void start_ListOfTrackPieceTemplates(final Attributes meta) {
        legalTemplates = new ArrayList<>();
    }

    public void end_ListOfTrackPieceTemplates() {
        validTrackConfigurations = new ValidTrackConfigurations(
                maxConsequ, legalTemplates);
        legalTemplates = null;
    }

    public void start_CannotBuildOnTheseTerrainTypes(final Attributes meta) {
        terrainTypes = new java.util.HashSet<>();
    }

    public void end_CannotBuildOnTheseTerrainTypes() {
        validTrackPlacement = new ValidTrackPlacement(terrainTypes,
                ValidTrackPlacement.PlacementRule.ANYWHERE_EXCEPT_ON_THESE);
        terrainTypes = null;
    }

    public void start_TrackType(final Attributes meta) {
        int rGBvalue;
        String rgbString = meta.getValue("RGBvalue");
        rGBvalue = Integer.parseInt(rgbString, 16);

        /*
         * We need to change the format of the rgb value to the same one as used
         * by the the BufferedImage that stores the map. See
         * freerails.common.Map
         */
        rGBvalue = new java.awt.Color(rGBvalue).getRGB();

        TrackCategories category = TrackCategories
                .valueOf(meta.getValue("category"));

        boolean enableDoubleTrack = Boolean.valueOf(
                meta.getValue("doubleTrack"));
        String typeName = meta.getValue("type");
        // TODO correct this meta value (probably needs to change somewhere else too)
        maxConsequ = Integer.parseInt(meta.getValue("maxConsecuativePieces"));

        String stationRadiusString = meta.getValue("stationRadius");
        int stationRadius;

        if (null != stationRadiusString) {
            stationRadius = Integer.parseInt(stationRadiusString);
        } else {
            stationRadius = 0;
        }

        String priceString = meta.getValue("price");
        int price = Integer.parseInt(priceString);

        String fixedCostString = meta.getValue("fixedCost");
        int fixedCost;

        if (null != fixedCostString) {
            fixedCost = Integer.parseInt(fixedCostString);
        } else {
            fixedCost = 0;
        }

        String maintenanceString = meta.getValue("maintenance");
        int maintenance = Integer.parseInt(maintenanceString);

        trackRuleProperties = new TrackRuleProperties(rGBvalue,
                enableDoubleTrack, typeName, category, stationRadius, price,
                maintenance, fixedCost);
    }

    public void end_TrackType() {
        TrackRuleImpl trackRuleImpl = new freerails.world.track.TrackRuleImpl(
                trackRuleProperties, validTrackConfigurations,
                validTrackPlacement);
        ruleList.add(trackRuleImpl);

        validTrackConfigurations = null;
        trackRuleProperties = null;
        validTrackPlacement = null;
    }

    public void handle_TerrainType(final Attributes meta) {
        TerrainCategory cat = TerrainCategory.valueOf(meta
                .getValue("name"));
        terrainTypes.add(cat);
    }

    public void end_Tiles() {
        // Sort the track tiles by category then price.
        Collections.sort(ruleList);
    }

    public void start_TrackPieceTemplate(final Attributes meta) {
        legalTemplates.add(meta.getValue("trackTemplate"));
    }

    public void start_TrackSet(final Attributes meta) {
        ruleList = new ArrayList<>();
    }

    /**
     * @param w
     */
    public void addTrackRules(World w) {
        for (TrackRule r : this.ruleList) {
            w.add(SKEY.TRACK_RULES, r);
        }
    }

    /**
     * @return
     */
    public List<TrackRule> getRuleList() {
        return ruleList;
    }
}