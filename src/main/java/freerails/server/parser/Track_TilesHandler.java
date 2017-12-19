package freerails.server.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Defines methods to handle parsing the track types XML.
 *
 */
public interface Track_TilesHandler {
    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_CanOnlyBuildOnTheseTerrainTypes(final Attributes meta)
            throws SAXException;

    /**
     * A container element end event handling method.
     *
     * @throws org.xml.sax.SAXException
     */
    void end_CanOnlyBuildOnTheseTerrainTypes() throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_ListOfTrackPieceTemplates(final Attributes meta)
            throws SAXException;

    /**
     * A container element end event handling method.
     *
     * @throws org.xml.sax.SAXException
     */
    void end_ListOfTrackPieceTemplates() throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_ListOfLegalRoutesAcrossNode(final Attributes meta)
            throws SAXException;

    /**
     * A container element end event handling method.
     *
     * @throws org.xml.sax.SAXException
     */
    void end_ListOfLegalRoutesAcrossNode() throws SAXException;

    /**
     * An empty element event handling method.
     *
     * @param meta
     * @throws org.xml.sax.SAXException
     */
    void handle_LegalRouteAcrossNode(final Attributes meta) throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_CannotBuildOnTheseTerrainTypes(final Attributes meta)
            throws SAXException;

    /**
     * A container element end event handling method.
     *
     * @throws org.xml.sax.SAXException
     */
    void end_CannotBuildOnTheseTerrainTypes() throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_TrackType(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     *
     * @throws org.xml.sax.SAXException
     */
    void end_TrackType() throws SAXException;

    /**
     * An empty element event handling method.
     *
     * @param meta
     * @throws org.xml.sax.SAXException
     */
    void handle_TerrainType(final Attributes meta) throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_Tiles(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     * @throws org.xml.sax.SAXException
     */
    void end_Tiles() throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_TrackPieceTemplate(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     *
     * @throws org.xml.sax.SAXException
     */
    void end_TrackPieceTemplate() throws SAXException;

    /**
     * A container element start event handling method.
     *
     * @param meta attributes
     * @throws org.xml.sax.SAXException
     */
    void start_TrackSet(final Attributes meta) throws SAXException;

    /**
     * A container element end event handling method.
     *
     * @throws org.xml.sax.SAXException
     */
    void end_TrackSet() throws SAXException;
}