package database.tests;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.vladmihalcea.sql.SQLStatementCountValidator;
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
import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.OrderPosition;
import sp.data.entities.Product;
import database.services.CauseExceptionMatcher;
import database.services.TestEntitiesCreationService;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.Assert.*;
import static com.vladmihalcea.sql.SQLStatementCountValidator.*;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(TransactionMode.DISABLED)
public class ProductDbValidationTest {

    @SpringBeanByType
    ProductDao dao;

    TestEntitiesCreationService service = new TestEntitiesCreationService();

    Product product = service.createTestProduct(1);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp() {
        DatabaseUnitils.updateSequences();
        SQLStatementCountValidator.reset();
    }


    @Test
    public void allColumnsCanBeWrittenAndRead(){
        Product product = service.createTestProduct(1);
        dao.save(product);
        Product productFromDb = dao.getById(product.getId());

        assertSelectCount(1);
        assertEquals("id", product.getId(), productFromDb.getId());
        assertEquals("name", product.getName(), productFromDb.getName());
        assertEquals("link", product.getLink(), productFromDb.getLink());
        assertEquals("weight", product.getWeight(), productFromDb.getWeight());
        assertEquals("price", product.getPrice(), productFromDb.getPrice());
        assertEquals("vkId", product.getVkId(), productFromDb.getVkId());
        assertEquals("imageLink", product.getImageLink(), productFromDb.getImageLink());
        assertEquals("status", product.getStatus(), productFromDb.getStatus());
        assertEquals("vkPhotoId", product.getVkPhotoId(), productFromDb.getVkPhotoId());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @DataSet("db_test/dataset/validation/product/ProductDbValidationTest.defaultValuesTest.xml")
    @ExpectedDataSet("db_test/dataset/validation/product/ProductDbValidationTest.defaultValuesTest_ExpectedDataSet.xml")
    public void defaultValuesTest() {}

    @Test
    public void name_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'name' cannot be null"));
        product.setName(null);
        dao.save(product);
    }

    @Test
    public void price_ShouldNotBeNull_OrThrowException() throws NoSuchFieldException, IllegalAccessException {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'price' cannot be null"));

        Field field = Product.class.getDeclaredField("price");
        field.setAccessible(true);
        field.set(product, null);

        dao.save(product);
    }

    @Test
    public void link_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'link' cannot be null"));
        product.setLink(null);
        dao.save(product);
    }

    @Test
    public void weight_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'weight' cannot be null"));
        product.setWeight(null);
        dao.save(product);
    }

    @Test
    public void name_ShouldNotBeLongerThan300Characters_OrThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'name'"));

        product.setName(StringUtils.leftPad("", 301, "a"));
        dao.save(product);
    }

    @Test
    public void link_ShouldNotBeLongerThan300Characters_OrThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'link'"));

        product.setLink(StringUtils.leftPad("", 301, "a"));
        dao.save(product);
    }

    @Test
    public void imageLink_ShouldNotBeLongerThan300Characters_OrThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'imagelink'"));

        product.setImageLink(StringUtils.leftPad("", 301, "a"));
        dao.save(product);
    }

    @Test
    public void price_ShouldBeNonNegative_OrThrowException() throws Exception {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'price'"));

        product.setPrice(new BigDecimal(-1));
        dao.save(product);
    }

    @Test
    public void weight_ShouldBeNonNegative_OrThrowException() throws Exception {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Out of range value for column 'weight'"));

        product.setWeight(-1);
        dao.save(product);
    }

}
