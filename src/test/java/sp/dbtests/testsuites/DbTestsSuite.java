package sp.dbtests.testsuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        DbIntegrationTestsSuite.class,
        DbBasicTestsSuite.class
})
public class DbTestsSuite {


}
