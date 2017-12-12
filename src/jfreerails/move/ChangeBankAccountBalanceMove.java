
package jfreerails.move;

import jfreerails.misc.Money;
import jfreerails.type.DebitTransactionType;
import jfreerails.world.financial.ListedCompany;

/**
 * Represents ...
 * 
 * @see OtherClasses
 * @author lindsal
 */

final public class ChangeBankAccountBalanceMove implements FinancialMove {


    private final Money oldBalance;


    private final Money newBalance;

    private final DebitTransactionType type;


    public Money getOldBalance() {
        return oldBalance;
    }

    public Money getNewBalance() {
        return newBalance;
    }

    public DebitTransactionType getType() {
        return type;
    }
    
    public ListedCompany getListedCompany() {
    	throw new java.lang.UnsupportedOperationException(
			"Method not implemented yet!");
    }
    
    public ChangeBankAccountBalanceMove(ListedCompany lc, Money before, Money after, DebitTransactionType t){
    
    		oldBalance=before;
    		newBalance=after;
    		type=t;
    } 
}




