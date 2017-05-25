package testsuits;

import data.validators.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientValidatorTest.class,
        OrderValidatorTest.class,
        SpValidatorTest.class,
		OrderPositionValidatorTest.class,
		ZeroWeightOrderPositionsFormValidatorTest.class
})
public class ValidatorTestsSuite {

}
