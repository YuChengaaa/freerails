package jfreerails.world.std_cargo;

import java.awt.Point;

import jfreerails.world.type.CargoType;
import jfreerails.world.cargo.CargoBatch;

public abstract class AbstCargoBatch implements CargoBatch {

	private final Point pointOfOrigin;

	private final String placeOfOrigin;

	private final CargoType cargoType;
	private final CompositeCargoBundle cargoBundle;

	public AbstCargoBatch() {
		pointOfOrigin = null;
		placeOfOrigin = null;
		cargoType = null;
		cargoBundle = null;
	}

	public Point getPointOfOrigin() {
		return pointOfOrigin;
	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public CargoType getCargoType() {
		return cargoType;
	}

	public CompositeCargoBundle getCargoBundle() {
		return cargoBundle;
	}

	public boolean hasTravelled() {
		return false;
	}

}