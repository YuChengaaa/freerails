/*
 * Created on 26-May-2003
 *
 */
package freerails.move;

import freerails.world.cargo.CargoBatch;
import freerails.world.cargo.MutableCargoBundle;
import freerails.world.top.KEY;
import freerails.world.top.MapFixtureFactory;

/**
 * JUnit test.
 *
 */
public class ChangeCargoBundleMoveTest extends AbstractMoveTestCase {

    /**
     *
     */
    @Override
    public void testMove() {
        MutableCargoBundle before;
        MutableCargoBundle after;
        before = new MutableCargoBundle();
        after = new MutableCargoBundle();
        before.setAmount(new CargoBatch(1, 2, 3, 4, 0), 5);
        after.setAmount(new CargoBatch(1, 2, 3, 4, 0), 8);

        Move m = new ChangeCargoBundleMove(before.toImmutableCargoBundle(),
                after.toImmutableCargoBundle(), 0,
                MapFixtureFactory.TEST_PRINCIPAL);
        assertSurvivesSerialisation(m);

        assertTryMoveFails(m);
        assertTryUndoMoveFails(m);
        getWorld().add(MapFixtureFactory.TEST_PRINCIPAL, KEY.CARGO_BUNDLES,
                before.toImmutableCargoBundle());
    }
}