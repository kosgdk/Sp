package testsuits;

import data.entities.beanvalidation.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientBeanValidationTest.class,
        OrderBeanValidationTest.class,
        OrderPositionBeanValidationTest.class,
        ProductBeanValidationTest.class,
        PropertiesBeanValidationTest.class,
        SpBeanValidationTest.class
})
public class BeanValidationTestsSuite {

}
