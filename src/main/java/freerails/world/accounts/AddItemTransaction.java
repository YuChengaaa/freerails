package freerails.world.accounts;

import freerails.world.common.Money;

/**
 * This Transaction represents the charge/credit for buying/selling an item.
 *
 */
public class AddItemTransaction implements Transaction {

    private static final long serialVersionUID = 3690471411852326457L;
    private final Money amount;
    /**
     * For example track.
     */
    private final Category category;
    /**
     * For example, 4 tiles.
     */
    private final int quantity;
    /**
     * For example, standard track.
     */
    private final int type;

    /**
     *
     * @param category
     * @param type
     * @param quantity
     * @param amount
     */
    public AddItemTransaction(Category category, int type, int quantity, Money amount) {
        this.category = category;
        this.type = type;
        this.quantity = quantity;
        this.amount = amount;
    }

    /**
     *
     * @return
     */
    public Money deltaAssets() {
        return amount.changeSign();
    }

    public Money deltaCash() {
        return amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AddItemTransaction) {
            AddItemTransaction test = (AddItemTransaction) obj;

            return this.amount.equals(test.amount) && category == test.category
                    && type == test.type && quantity == test.quantity;
        }
        return false;
    }

    /**
     *
     * @return
     */
    public Category getCategory() {
        return category;
    }

    /**
     *
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    @Override
    public int hashCode() {
        int result;
        result = category.hashCode();
        result = 29 * result + type;
        result = 29 * result + quantity;
        result = 29 * result + amount.hashCode();

        return result;
    }

    @Override
    public String toString() {
        return "AddItemTransaction " +
                category +
                ", type " +
                type +
                ", quantity " +
                quantity +
                ", amount " +
                amount;
    }
}