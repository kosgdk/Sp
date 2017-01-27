package database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DbBasicTestsSuite.class,
        DbIntegrationTestsSuite.class,
        DbValidationTestsSuite.class
})
public class DbCompleteTestsSuite {

}
