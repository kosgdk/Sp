package testsuits;

import data.validators.ClientValidatorTest;
import data.validators.OrderValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientValidatorTest.class,
        OrderValidatorTest.class
})
public class ValidatorTestsSuite {

}
