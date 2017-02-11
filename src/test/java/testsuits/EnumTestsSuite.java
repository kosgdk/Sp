package testsuits;

import data.entities.enumerators.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientReferrerTest.class,
        OrderStatusTest.class,
        PlaceTest.class,
        ProductStatusTest.class,
        SpStatusTest.class
})
public class EnumTestsSuite {

}
