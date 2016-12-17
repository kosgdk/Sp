package sp.dbtests.testsuites;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import sp.dbtests.OrderPositionDbIntegrationTest;
import sp.dbtests.OrderPositionDbValidationTest;
import sp.dbtests.unitils.SchemaRebuilder;

import static sp.dbtests.unitils.ConstraintsDisabler.ConstraintType;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SchemaRebuilder.class,
        OrderPositionDbValidationTest.class
})
public class DbValidationTestsSuite {

    @BeforeClass
    public static void setUp(){
        SchemaRebuilder.setConstraintType(ConstraintType.REFERENTIAL);
    }

}
