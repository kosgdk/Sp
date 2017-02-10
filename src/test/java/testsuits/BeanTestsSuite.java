package testsuits;

import data.entities.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        OrderPositionTest.class,
        OrderTest.class,
        ProductTest.class,
        PropertiesTest.class,
        SpTest.class
})
public class BeanTestsSuite {

}
