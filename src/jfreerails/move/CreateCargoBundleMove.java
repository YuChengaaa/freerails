
package jfreerails.move;


import jfreerails.world.std_cargo.CompositeCargoBundle;
import jfreerails.world.cargo.CargoContainer;

/**
 * Represents ...
 * 
 * @see OtherClasses
 * @author lindsal
 */

final public class CreateCargoBundleMove {

  

    private final CompositeCargoBundle bundle;


    private final CargoContainer oldContainer;


    public CompositeCargoBundle getBundle() {
        return bundle;
    }

    public CargoContainer getOldContainer() {
        return oldContainer;
    }
    
    public CreateCargoBundleMove(CompositeCargoBundle b, CargoContainer c){
    	bundle=b;
    	oldContainer=c;
    }
}




