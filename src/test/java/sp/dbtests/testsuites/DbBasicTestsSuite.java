package sp.dbtests.testsuites;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sp.dbtests.OrderPositionDbBasicTest;
import sp.dbtests.unitils.SchemaRebuilder;

import static sp.dbtests.unitils.ConstraintsDisabler.ConstraintType;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SchemaRebuilder.class,
        OrderPositionDbBasicTest.class
})
public class DbBasicTestsSuite {

    @BeforeClass
    public static void setUp(){
        SchemaRebuilder.setConstraintType(ConstraintType.ALL);
    }

}
