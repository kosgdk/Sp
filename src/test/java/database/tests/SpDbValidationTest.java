package database.tests;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.database.DatabaseUnitils;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Sp;
import database.services.CauseExceptionMatcher;
import database.services.TestEntitiesCreationService;

import java.math.BigDecimal;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(TransactionMode.DISABLED)
public class SpDbValidationTest {

    @SpringBeanByType
    SpDao dao;

    TestEntitiesCreationService service = new TestEntitiesCreationService();
    Sp sp = service.createTestSp(1);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() {
        DatabaseUnitils.updateSequences();
    }


    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @DataSet("db_test/dataset/validation/sp/SpDbValidationTest.defaultValuesTest.xml")
    @ExpectedDataSet("db_test/dataset/validation/sp/SpDbValidationTest.defaultValuesTest_ExpectedDataSet.xml")
    public void defaultValuesTest() {}

    @Test(expected = IdentifierGenerationException.class)
    public void number_ShouldNotBeNull_OrThrowException() {
        sp.setNumber(null);
        dao.save(sp);
    }

    @Test
    public void percent_ShouldNotBeNull_OrThrowException() throws Throwable{
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'percent' cannot be null"));
        sp.setPercent(null);
        try {
            dao.save(sp);
        } catch (DataIntegrityViolationException e){
            throw e.getCause();
        }
    }

    @Test
    public void status_ShouldNotBeNull_OrThrowException() throws Throwable{
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'status' cannot be null"));
        sp.setStatus(null);
        try {
            dao.save(sp);
        } catch (DataIntegrityViolationException e){
            throw e.getCause();
        }
    }

    @Test
    public void dateStart_ShouldNotBeNull_OrThrowException() throws Throwable{
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'date_start' cannot be null"));
        sp.setDateStart(null);
        try {
            dao.save(sp);
        } catch (DataIntegrityViolationException e){
            throw e.getCause();
        }
    }

    @Test
    public void dateEnd_ShouldNotBeNull_OrThrowException() throws Throwable{
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'date_end' cannot be null"));
        sp.setDateEnd(null);
        try {
            dao.save(sp);
        } catch (DataIntegrityViolationException e){
            throw e.getCause();
        }
    }

    @Test
    public void dateToPay_ShouldNotBeNull_OrThrowException() throws Throwable{
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'date_to_pay' cannot be null"));
        sp.setDateToPay(null);
        try {
            dao.save(sp);
        } catch (DataIntegrityViolationException e){
            throw e.getCause();
        }
    }

    @Test
    public void percent_ShouldBeNonNegative_OrThrowException() throws Throwable {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'percent'"));

        Sp sp = service.createTestSp(1);
        sp.setPercent(new BigDecimal(-1));
        try {
            dao.save(sp);
        } catch (DataIntegrityViolationException e){
            throw e.getCause();
        }
    }

    @Test
    public void number_ShouldBeUnique_OrThrowException() throws Throwable{
        Long number = dao.getLastNumber() + 1;

        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Duplicate entry '" + number + "' for key 'PRIMARY'"));
        sp.setNumber(number);
        Sp sp2 = service.createTestSp(number);
        dao.save(sp);
        try{
            dao.save(sp2);
        }catch (DataIntegrityViolationException e){
            throw e.getCause();
        }
    }

}