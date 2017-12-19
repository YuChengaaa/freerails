package freerails.world.station;

import freerails.world.FreerailsSerializable;
import freerails.world.common.ImInts;

/**
 * Records which cargos are converted to other cargos at a station.
 *
 */
public class ConvertedAtStation implements FreerailsSerializable {
    private static final long serialVersionUID = 3690754012076978231L;

    private static final int NOT_CONVERTED = Integer.MIN_VALUE;

    private final ImInts convertedTo;

    /**
     *
     * @param convertedTo
     */
    public ConvertedAtStation(int[] convertedTo) {
        this.convertedTo = new ImInts(convertedTo);
    }

    /**
     *
     * @param numberOfCargoTypes
     * @return
     */
    public static ConvertedAtStation emptyInstance(int numberOfCargoTypes) {
        int[] convertedTo = emptyConversionArray(numberOfCargoTypes);

        return new ConvertedAtStation(convertedTo);
    }

    /**
     *
     * @param numberOfCargoTypes
     * @return
     */
    public static int[] emptyConversionArray(int numberOfCargoTypes) {
        int[] convertedTo = new int[numberOfCargoTypes];

        for (int i = 0; i < numberOfCargoTypes; i++) {
            convertedTo[i] = NOT_CONVERTED;
        }

        return convertedTo;
    }

    /**
     *
     * @param cargoNumber
     * @return
     */
    public boolean isCargoConverted(int cargoNumber) {
        return NOT_CONVERTED != convertedTo.get(cargoNumber);
    }

    /**
     *
     * @param cargoNumber
     * @return
     */
    public int getConversion(int cargoNumber) {
        return convertedTo.get(cargoNumber);
    }

    @Override
    public int hashCode() {
        int result = 0;

        for (int i = 0; i < convertedTo.size(); i++) {
            result = 29 * result + convertedTo.get(i);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ConvertedAtStation) {
            ConvertedAtStation test = (ConvertedAtStation) o;

            if (this.convertedTo.size() != test.convertedTo.size()) {
                return false;
            }

            for (int i = 0; i < convertedTo.size(); i++) {
                if (convertedTo.get(i) != test.convertedTo.get(i)) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }
}