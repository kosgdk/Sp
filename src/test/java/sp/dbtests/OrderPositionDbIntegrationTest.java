package sp.dbtests;

import net.sf.ehcache.CacheManager;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import sp.data.dao.interfaces.OrderDao;
import sp.data.dao.interfaces.OrderPositionDao;
import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.OrderPosition;

import static org.junit.Assert.*;
import static com.vladmihalcea.sql.SQLStatementCountValidator.*;


@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@Transactional(value=TransactionMode.DISABLED)
@DataSet("unitils/dataset/OrderPositionDbIntegrationTest/mainDataSet.xml")
public class OrderPositionDbIntegrationTest {

    @SpringBeanByType
    OrderPositionDao orderPositionDao;

    @SpringBeanByType
    ProductDao productDao;

    @SpringBeanByType
    OrderDao orderDao;

    @Before
    public void setUp(){
        reset(); //SQLStatementCountValidator
        CacheManager.getInstance().clearAll(); //Clearing 2nd level cache
    }


    @Test
    public void getById_testFetchTypeOfChildren(){
        OrderPosition orderPosition = orderPositionDao.getById(1L);
        assertSelectCount(1);

        assertNotNull(orderPosition.getProduct());
        assertSelectCount(1);
        assertEquals(new Long(1), orderPosition.getProduct().getId());
        assertSelectCount(1); // Test for EAGER loading of Product

        assertNotNull(orderPosition.getOrder());
        assertSelectCount(1);
        System.out.println(orderPosition.getOrder().getId());
        assertEquals(new Long(1), orderPosition.getOrder().getId());
        assertSelectCount(2); // Test for LAZY loading of Order
    }

    @Test
    @ExpectedDataSet("unitils/dataset/OrderPositionDbIntegrationTest/update_ShouldNotCascadeUpdateChild_Expected.xml")
    public void update_ShouldNotCascadeUpdateChild(){
        OrderPosition orderPosition = orderPositionDao.getById(1L);
        assertSelectCount(1);
        orderPosition.getProduct().setName("TestProduct");
        orderPosition.getOrder().setNote("TestNote");
        assertSelectCount(2);
        orderPosition.setNote("TestNote");
        orderPositionDao.update(orderPosition);
        assertUpdateCount(1);
    }

    @Test
    @ExpectedDataSet("unitils/dataset/OrderPositionDbIntegrationTest/delete_ShouldNotCascadeDeleteChild.xml")
    public void delete_ShouldNotCascadeDeleteChild(){
        orderPositionDao.deleteById(1L);
        assertSelectCount(1);
        assertDeleteCount(1);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void delete_ChildProduct_ShouldCauseException(){
        productDao.deleteById(1L);
    }

}
