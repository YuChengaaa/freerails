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

package freerails.move.listmove;

import freerails.move.CompositeMove;
import freerails.move.Move;
import freerails.move.listmove.ChangeCargoBundleMove;

import java.util.List;

// TODO this should probably not derive from CompositeMove ??
/**
 * This {@link CompositeMove} transfers cargo from a train to a station and
 * vice-versa.
 */
public class TransferCargoAtStationMove extends CompositeMove {

    public static final int CHANGE_ON_TRAIN_INDEX = 1;
    public static final int CHANGE_AT_STATION_INDEX = 0;
    private static final long serialVersionUID = 3257291318215456563L;

    /**
     * @param moves
     */
    public TransferCargoAtStationMove(List<Move> moves) {
        super(moves);
    }

    /**
     * @return
     */
    public ChangeCargoBundleMove getChangeAtStation() {
        return (ChangeCargoBundleMove) super.getMoves().get(CHANGE_AT_STATION_INDEX);
    }

    /**
     * @return
     */
    public ChangeCargoBundleMove getChangeOnTrain() {
        return (ChangeCargoBundleMove) super.getMoves().get(CHANGE_ON_TRAIN_INDEX);
    }

}