package database.tests;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.database.DatabaseUnitils;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import sp.data.dao.interfaces.PropertiesDao;
import sp.data.entities.Properties;
import testservices.CauseExceptionMatcher;
import testservices.TestEntitiesCreationService;

import java.lang.reflect.Field;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(TransactionMode.DISABLED)
public class PropertiesDbValidationTest {

    @SpringBeanByType
    PropertiesDao dao;

    TestEntitiesCreationService service = new TestEntitiesCreationService();

    Properties properties = service.createTestProperties();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private void configureExpectedExceptionRule(String message){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class, message));
    }


    @Before
    public void setUp() {
        DatabaseUnitils.updateSequences();
    }


    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @DataSet("db_test/dataset/validation/properties/PropertiesDbValidationTest.defaultValuesTest.xml")
    @ExpectedDataSet("db_test/dataset/validation/properties/PropertiesDbValidationTest.defaultValuesTest_ExpectedDataSet.xml")
    public void defaultValuesTest() {}

    @Test
    public void percentSp_ShouldNotBeNull_OrThrowException() throws NoSuchFieldException, IllegalAccessException {
        configureExpectedExceptionRule("Column 'percent_sp' cannot be null");
        Field field = Properties.class.getDeclaredField("percentSp");
        field.setAccessible(true);
        field.set(properties, null);
        dao.save(properties);
    }

    @Test
    public void percentDiscount_ShouldNotBeNull_OrThrowException() throws NoSuchFieldException, IllegalAccessException {
        configureExpectedExceptionRule("Column 'percent_discount' cannot be null");
        Field field = Properties.class.getDeclaredField("percentDiscount");
        field.setAccessible(true);
        field.set(properties, null);
        properties.setPercentDiscount(null);
        dao.save(properties);
    }

    @Test
    public void percentBankCommission_ShouldNotBeNull_OrThrowException() throws NoSuchFieldException, IllegalAccessException {
        configureExpectedExceptionRule("Column 'percent_bank_commission' cannot be null");
        Field field = Properties.class.getDeclaredField("percentBankCommission");
        field.setAccessible(true);
        field.set(properties, null);
        dao.save(properties);
    }

}