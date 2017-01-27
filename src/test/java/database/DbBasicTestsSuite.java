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
        OrderPositionDbBasicTest.class,
        SpDbBasicTest.class,
        OrderDbBasicTest.class,
        ProductDbBasicTest.class,
        ClientDbBasicTest.class,
        PropertiesDbBasicTest.class
})
public class DbBasicTestsSuite {

    @BeforeClass
    public static void setUp(){
        SchemaRebuilder.setConstraintTypeToDisable(ConstraintType.ALL);
    }

}
