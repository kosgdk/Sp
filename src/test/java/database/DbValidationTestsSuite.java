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
        OrderPositionDbValidationTest.class,
        SpDbValidationTest.class,
        OrderDbValidationTest.class,
        ClientDbValidationTest.class,
        ProductDbValidationTest.class,
        PropertiesDbValidationTest.class
})
public class DbValidationTestsSuite {

    @BeforeClass
    public static void setUp(){
        SchemaRebuilder.setConstraintTypeToDisable(ConstraintType.REFERENTIAL);
    }

}
