package jfreerails.move;

import java.util.ArrayList;
import jfreerails.world.accounts.DeliverCargoReceipt;
import jfreerails.world.cargo.CargoBatch;
import jfreerails.world.common.Money;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.player.Player;


/**
 * This {@link CompositeMove} transfers cargo from a train to a station and vice-versa.
 *
 * @author Luke Lindsay
 *
 *
 */
public class TransferCargoAtStationMove extends CompositeMove {
    public static final int CHANGE_ON_TRAIN_INDEX = 1;
    public static final int CHANGE_AT_STATION_INDEX = 0;

    private TransferCargoAtStationMove(Move[] moves) {
        super(moves);
    }

    public static TransferCargoAtStationMove generateMove(
        ChangeCargoBundleMove changeAtStation,
        ChangeCargoBundleMove changeOnTrain, CompositeMove payment) {
        return new TransferCargoAtStationMove(new Move[] {
                changeAtStation, changeOnTrain, payment
            });
    }

    public ChangeCargoBundleMove getChangeAtStation() {
        return (ChangeCargoBundleMove)super.getMoves()[CHANGE_AT_STATION_INDEX];
    }

    public ChangeCargoBundleMove getChangeOnTrain() {
        return (ChangeCargoBundleMove)super.getMoves()[CHANGE_ON_TRAIN_INDEX];
    }

    public Money getRevenue() {
        Move[] moves = super.getMoves();
        long amount = CHANGE_AT_STATION_INDEX;

        for (int i = CHANGE_AT_STATION_INDEX; i < moves.length; i++) {
            if (moves[i] instanceof AddTransactionMove) {
                AddTransactionMove move = (AddTransactionMove)moves[i];
                DeliverCargoReceipt receipt = (DeliverCargoReceipt)move.getTransaction();
                amount += receipt.getValue().getAmount();
            }
        }

        return new Money(amount);
    }

    public int getQuantityOfCargo(int cargoType) {
        Move[] moves = super.getMoves();
        int quantity = CHANGE_AT_STATION_INDEX;

        for (int i = CHANGE_AT_STATION_INDEX; i < moves.length; i++) {
            if (moves[i] instanceof AddTransactionMove) {
                AddTransactionMove move = (AddTransactionMove)moves[i];
                DeliverCargoReceipt receipt = (DeliverCargoReceipt)move.getTransaction();
                CargoBatch cb = receipt.getCb();

                if (cb.getCargoType() == cargoType) {
                    quantity += receipt.getQuantity();
                }
            }
        }

        return quantity;
    }

    /** The player who is getting paid for the delivery.*/
    public FreerailsPrincipal getPrincipal() {
        Move[] moves = super.getMoves();

        for (int i = CHANGE_AT_STATION_INDEX; i < moves.length; i++) {
            if (moves[i] instanceof AddTransactionMove) {
                AddTransactionMove move = (AddTransactionMove)moves[i];

                return move.getPrincipal();
            }
        }

        return Player.NOBODY;
    }

    public TransferCargoAtStationMove(ArrayList movesArrayList) {
        super(movesArrayList);
    }
}