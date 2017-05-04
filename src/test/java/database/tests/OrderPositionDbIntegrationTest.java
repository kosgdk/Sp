package database.tests;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import net.sf.ehcache.CacheManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import sp.data.dao.interfaces.OrderDao;
import sp.data.dao.interfaces.OrderPositionDao;
import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.entities.Product;
import testservices.CauseExceptionMatcher;

import java.util.List;

import static org.junit.Assert.*;
import static com.vladmihalcea.sql.SQLStatementCountValidator.*;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/integration/orderPosition/OrderPositionDbIntegrationTest.mainDataSet.xml")
public class OrderPositionDbIntegrationTest {

    @SpringBeanByType
    OrderPositionDao orderPositionDao;

    @SpringBeanByType
    ProductDao productDao;

    @SpringBeanByType
    OrderDao orderDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
    }


    //ORM test
    @Test
    public void getById_ProductShouldBeLoadedEagerly(){
        OrderPosition orderPosition = orderPositionDao.getById(1L);
        assertNotNull(orderPosition.getProduct());
        assertEquals(new Long(1), orderPosition.getProduct().getId());
        assertSelectCount(1);
    }

    //ORM test
    @Test
    public void getById_OrderShouldBeLoadedLazily(){
        OrderPosition orderPosition = orderPositionDao.getById(1L);
        assertNotNull(orderPosition.getOrder());
        assertEquals(new Long(1), orderPosition.getOrder().getId());
        assertSelectCount(2);
    }

    //DB test
    @Test
    public void IfOrderNotSaved_OrderPositionCanNotBeSaved() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "CONSTRAINT `order` FOREIGN KEY (`order_id`)"));
        Order order = new Order();
        order.setId(3L);
        OrderPosition orderPosition = new OrderPosition();
        orderPosition.setOrder(order);
        orderPositionDao.save(orderPosition);
    }

    //DB test
    @Test
    @ExpectedDataSet("db_test/dataset/integration/orderPosition/OrderPositionDbIntegrationTest.ifOrderRemoved_OrderPositionShouldBeRemovedCascade.xml")
    public void ifOrderRemoved_OrderPositionShouldBeRemovedCascade(){
        orderDao.deleteById(1L);
        assertDeleteCount(1);
    }

    //DB test
    @Test
    public void IfProductNotSaved_OrderPositionCanNotBeSaved() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "CONSTRAINT `product` FOREIGN KEY (`product_id`)"));
        Product product = new Product();
        product.setId(3L);
        OrderPosition orderPosition = new OrderPosition();
        orderPosition.setProduct(product);
        orderPositionDao.save(orderPosition);
    }

    //DB test
    @Test
    public void ProductCanNotBeRemovedWhileInReference() throws Throwable{
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "CONSTRAINT `product` FOREIGN KEY (`product_id`)"));
        try {
            productDao.deleteById(1L);
        }catch (DataIntegrityViolationException e){
            throw e.getCause();
        }
    }

    //DAO test
    @Test
    @DataSet("db_test/dataset/integration/orderPosition/OrderPositionDbIntegrationTest.getZeroWeightOrderPositions_ShouldReturnOrderPositionsList.xml")
    public void getZeroWeightOrderPositions_ShouldReturnOrderPositionsList(){
        List<OrderPosition> zeroWeightOrderPositions = orderPositionDao.getZeroWeightOrderPositions(1L);
        assertSelectCount(1);
        assertNotNull(zeroWeightOrderPositions);
        assertEquals(2, zeroWeightOrderPositions.size());
        assertEquals(new Long(1), zeroWeightOrderPositions.get(0).getId());
        assertEquals(new Long(3), zeroWeightOrderPositions.get(1).getId());
    }

    @Test
    @DataSet("db_test/dataset/integration/orderPosition/OrderPositionDbIntegrationTest.getZeroWeightOrderPositions_ShouldReturnOrderPositionsList.xml")
    public void getZeroWeightOrderPositions_InputNull_ShouldReturnEmptyList(){
        List<OrderPosition> zeroWeightOrderPositions = orderPositionDao.getZeroWeightOrderPositions(null);
        assertSelectCount(0);
        assertNotNull(zeroWeightOrderPositions);
        assertTrue(zeroWeightOrderPositions.isEmpty());
    }

    @Test
    @DataSet("db_test/dataset/integration/orderPosition/OrderPositionDbIntegrationTest.getZeroWeightOrderPositions_ShouldReturnOrderPositionsList.xml")
    public void getZeroWeightOrderPositions_InputNonexistentSpId_ShouldReturnEmptyList(){
        List<OrderPosition> zeroWeightOrderPositions = orderPositionDao.getZeroWeightOrderPositions(2L);
        assertSelectCount(1);
        assertNotNull(zeroWeightOrderPositions);
        assertTrue(zeroWeightOrderPositions.isEmpty());
    }

}
