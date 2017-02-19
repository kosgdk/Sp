package testsuits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        BeanTestsSuite.class,
        BeanValidationTestsSuite.class,
        EnumTestsSuite.class,
        ConvertersTestsSuite.class,
        ValidatorTestsSuite.class
})
public class AllTests_ExceptDataBase_TestsSuite {

}
