package testsuits;

import data.validators.ClientValidatorTest;
import data.validators.OrderValidatorTest;
import data.validators.SpValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientValidatorTest.class,
        OrderValidatorTest.class,
        SpValidatorTest.class
})
public class ValidatorTestsSuite {

}
