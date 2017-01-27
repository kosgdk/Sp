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
import sp.data.dao.interfaces.OrderPositionDao;
import sp.data.entities.OrderPosition;
import database.services.CauseExceptionMatcher;
import database.services.TestEntitiesCreationService;

import java.math.BigDecimal;

@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(TransactionMode.DISABLED)
public class OrderPositionDbValidationTest {

    @SpringBeanByType
    OrderPositionDao dao;

    TestEntitiesCreationService service = new TestEntitiesCreationService();

    OrderPosition orderPosition = service.createTestOrderPosition(1);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        DatabaseUnitils.updateSequences();
    }


    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @DataSet("db_test/dataset/validation/orderPosition/OrderPositionDbValidationTest.defaultValuesTestxml")
    @ExpectedDataSet("db_test/dataset/validation/orderPosition/OrderPositionDbValidationTest.defaultValuesTest_ExpectedDataSet.xml")
    public void defaultValuesTest() {}

    @Test
    public void quantity_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'quantity' cannot be null"));
        orderPosition.setQuantity(null);
        dao.save(orderPosition);
    }

    @Test
    public void orderId_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'order_id' cannot be null"));
        orderPosition.setOrder(null);
        dao.save(orderPosition);
    }

    @Test
    public void productId_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'product_id' cannot be null"));
        orderPosition.setProduct(null);
        dao.save(orderPosition);
    }

    @Test
    public void priceOrdered_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'price_ordered' cannot be null"));
        orderPosition.setPriceOrdered(null);
        dao.save(orderPosition);
    }

    @Test
    public void priceSp_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'price_sp' cannot be null"));
        orderPosition.setPriceSp(null);
        dao.save(orderPosition);
    }

    @Test
    public void priceVendor_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'price_vendor' cannot be null"));
        orderPosition.setPriceVendor(null);
        dao.save(orderPosition);
    }

    @Test
    public void note_ShouldBeNotLongerThan500Characters_orThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'note'"));

        orderPosition.setNote(StringUtils.leftPad("", 501, "a"));
        dao.save(orderPosition);
    }

    @Test
    public void priceOrdered_ShouldBeNonNegative_OrThrowException() throws Exception {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'price_ordered'"));

        orderPosition.setPriceOrdered(new BigDecimal(-100));
        dao.save(orderPosition);
    }

    @Test
    public void priceSp_ShouldBeNonNegative_OrThrowException() throws Exception {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'price_sp'"));

        orderPosition.setPriceSp(new BigDecimal(-100));
        dao.save(orderPosition);
    }

    @Test
    public void priceVendor_ShouldBeNonNegative_OrThrowException() throws Exception {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'price_vendor'"));

        orderPosition.setPriceVendor(new BigDecimal(-100));
        dao.save(orderPosition);
    }

    @Test
    public void quantity_ShouldBeNonNegative_OrThrowException() throws Exception {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'quantity'"));

        orderPosition.setQuantity(-1);
        dao.save(orderPosition);
    }

}