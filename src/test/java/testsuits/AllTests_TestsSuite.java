package testsuits;

import database.DbCompleteTestsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        DbCompleteTestsSuite.class,
        BeanTestsSuite.class,
        BeanValidationTestsSuite.class,
        EnumTestsSuite.class,
        ConvertersTestsSuite.class,
        ValidatorTestsSuite.class,
        ServiceTestsSuite.class
})
public class AllTests_TestsSuite {

}
