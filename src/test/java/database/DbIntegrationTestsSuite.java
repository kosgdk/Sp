package database;

import database.tests.*;
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
        SpDbIntegrationTest.class,
        ProductDbIntegrationTest.class
})
public class DbIntegrationTestsSuite {

    @BeforeClass
    public static void setUp(){
        SchemaRebuilder.setConstraintTypeToDisable(ConstraintType.VALUE);
    }

}
