package database;

import database.tests.ClientDbIntegrationTest;
import database.tests.OrderDbIntegrationTest;
import database.tests.OrderPositionDbIntegrationTest;
import database.tests.SpDbIntegrationTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import database.unitils.SchemaRebuilder;

import static database.unitils.ConstraintsDisabler.ConstraintType;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SchemaRebuilder.class,
        OrderPositionDbIntegrationTest.class,
        ClientDbIntegrationTest.class,
        OrderDbIntegrationTest.class,
        SpDbIntegrationTest.class
})
public class DbIntegrationTestsSuite {

    @BeforeClass
    public static void setUp(){
        SchemaRebuilder.setConstraintTypeToDisable(ConstraintType.VALUE);
    }

}
