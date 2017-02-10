package database.tests;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
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
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Order;
import database.services.CauseExceptionMatcher;
import database.services.TestEntitiesCreationService;
import sp.data.entities.OrderPosition;

import java.lang.reflect.Field;
import java.math.BigDecimal;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(TransactionMode.DISABLED)
public class OrderDbValidationTest {

    @SpringBeanByType
    OrderDao dao;

    TestEntitiesCreationService service = new TestEntitiesCreationService();

    Order order = service.createTestOrder(1);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        DatabaseUnitils.updateSequences();
    }


    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @DataSet("db_test/dataset/validation/order/OrderDbValidationTest.defaultValuesTest.xml")
    @ExpectedDataSet("db_test/dataset/validation/order/OrderDbValidationTest.defaultValuesTest_ExpectedDataSet.xml")
    public void defaultValuesTest() {}

    @Test
    public void client_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'client' cannot be null"));
        order.setClient(null);
        dao.save(order);
    }

    @Test
    public void sp_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'sp' cannot be null"));
        order.setSp(null);
        dao.save(order);
    }

    @Test
    public void weight_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'weight' cannot be null"));
        order.setWeight(null);
        dao.save(order);
    }

    @Test
    public void status_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'status' cannot be null"));
        order.setStatus(null);
        dao.save(order);
    }

    @Test
    public void prepaid_ShouldNotBeNull_OrThrowException() throws NoSuchFieldException, IllegalAccessException {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'prepaid' cannot be null"));

        Field field = Order.class.getDeclaredField("prepaid");
        field.setAccessible(true);
        field.set(order, null);

        dao.save(order);
    }

    @Test
    public void dateOrdered_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'date_ordered' cannot be null"));
        order.setDateOrdered(null);
        dao.save(order);
    }

    @Test
    public void deliveryPrice_ShouldNotBeNull_OrThrowException() throws NoSuchFieldException, IllegalAccessException {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'delivery_price' cannot be null"));

        Field field = Order.class.getDeclaredField("deliveryPrice");
        field.setAccessible(true);
        field.set(order, null);

        dao.save(order);
    }

    @Test
    public void note_ShouldNotBeLongerThan500Chars_OrThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'note'"));

        order.setNote(StringUtils.leftPad("", 501, "a"));
        dao.save(order);
    }

    @Test
    public void weight_ShouldBeNonNegative_OrThrowException() throws Exception {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'weight'"));
        
        order.setWeight(-1);
        dao.save(order);
    }

    @Test
    public void prepaid_ShouldBeNonNegative_OrThrowException() throws Exception {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'prepaid'"));

        order.setPrepaid(new BigDecimal(-1));
        dao.save(order);
    }

    @Test
    public void deliveryPrice_ShouldBeNonNegative_OrThrowException() throws Exception {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'delivery_price'"));

        order.setDeliveryPrice(new BigDecimal(-1));
        dao.save(order);
    }

}
