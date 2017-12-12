package jfreerails.world.station;

import jfreerails.world.type.StationType;
import jfreerails.world.GameTime;

final public class StationModelPublic implements StationModel {

	private final GameTime builtDate;

	private final String stationName;

	private final StationType stationType;

	public GameTime getBuiltDate() {
		return builtDate;
	}

	public String getStationName() {
		return stationName;
	}

	public StationType getStationType() {
		return stationType;
	}

	public StationModelPublic(GameTime t, String name, StationType type) {
		builtDate = t;
		stationName = name;
		stationType = type;

	}
}