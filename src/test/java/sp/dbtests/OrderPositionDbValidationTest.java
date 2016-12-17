package sp.dbtests;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
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
import sp.dbtests.services.CauseExceptionMatcher;
import sp.dbtests.services.TestEntitiesCreationService;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(TransactionMode.DISABLED)
public class OrderPositionDbValidationTest {

    @SpringBeanByType
    OrderPositionDao orderPositionDao;

    TestEntitiesCreationService service = new TestEntitiesCreationService();

    OrderPosition orderPosition = service.createTestOrderPosition(1);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        DatabaseUnitils.updateSequences();
    }

    private void configureExpectedExceptionRule(String message){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class, message));
    }


    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @DataSet("unitils/dataset/validation/orderPosition/defaultValuesTest_DataSet.xml")
    @ExpectedDataSet("unitils/dataset/validation/orderPosition/defaultValuesTest_ExpectedDataSet.xml")
    public void defaultValuesTest() {
        //Just datasets
    }

    @Test
    public void quantity_ShouldNotBeNullOrThrowException() {
        configureExpectedExceptionRule("Column 'quantity' cannot be null");
        orderPosition.setQuantity(null);
        orderPositionDao.save(orderPosition);
    }

    @Test
    public void orderId_ShouldNotBeNullOrThrowException() {
        configureExpectedExceptionRule("Column 'order_id' cannot be null");
        orderPosition.setOrder(null);
        orderPositionDao.save(orderPosition);
    }

    @Test
    public void productId_ShouldNotBeNullOrThrowException() {
        configureExpectedExceptionRule("Column 'product_id' cannot be null");
        orderPosition.setProduct(null);
        orderPositionDao.save(orderPosition);
    }

    @Test
    public void priceOrdered_ShouldNotBeNullOrThrowException() {
        configureExpectedExceptionRule("Column 'price_ordered' cannot be null");
        orderPosition.setPriceOrdered(null);
        orderPositionDao.save(orderPosition);
    }

    @Test
    public void priceSp_ShouldNotBeNullOrThrowException() {
        configureExpectedExceptionRule("Column 'price_sp' cannot be null");
        orderPosition.setPriceSp(null);
        orderPositionDao.save(orderPosition);
    }

    @Test
    public void priceVendor_ShouldNotBeNullOrThrowException() {
        configureExpectedExceptionRule("Column 'price_vendor' cannot be null");
        orderPosition.setPriceVendor(null);
        orderPositionDao.save(orderPosition);
    }

    @Test
    public void note_ShouldBeShorterThan300Chars_orThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'note'"));

        StringBuilder note = new StringBuilder();
        for (int i = 0; i < 301; i++) {
            note.append("a");
        }
        orderPosition.setNote(note.toString());
        orderPositionDao.save(orderPosition);
    }

    @Test
    public void note_CanBe299Chars() {
        StringBuilder note = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            note.append("a");
        }
        orderPosition.setNote(note.toString());
        orderPositionDao.save(orderPosition);
    }

}
