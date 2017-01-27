package database.unitils;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.DatabaseUnitils;
import org.unitils.database.annotations.Transactional;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

@SpringApplicationContext("applicationContext_ForTests.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class SchemaRebuilder {

    private static ConstraintsDisabler.ConstraintType constraintType;

    public static void setConstraintTypeToDisable(ConstraintsDisabler.ConstraintType constraintType) {
        SchemaRebuilder.constraintType = constraintType;
    }

    public static ConstraintsDisabler.ConstraintType getConstraintTypeToDisable() {
        return constraintType;
    }

    @SpringBeanByName
    private SessionFactory sessionFactory;

    @Transactional
    public void executeSqlQuery(String query){
        sessionFactory.getCurrentSession().createNativeQuery(query).executeUpdate();
    }

    @Before
    public void setUp(){
        executeSqlQuery("DROP TABLE test_sp_database.dbmaintain_scripts");
        DatabaseUnitils.updateDatabase();
        XsdGenerationService.generateXsd();
        if (constraintType != null) {
            switch (constraintType) {
                case VALUE:
                    ConstraintsDisabler.disableValueConstraints();
                    break;
                case REFERENTIAL:
                    ConstraintsDisabler.disableReferentialConstraints();
                    break;
                case ALL:
                    ConstraintsDisabler.disableAllConstraints();
            }
        }
    }

    @Test
    public void prepareDatabaseForIntegrationTests(){
    }


}
