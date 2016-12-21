package sp.dbtests;

import com.vladmihalcea.sql.SQLStatementCountValidator;
import net.sf.ehcache.CacheManager;
import org.junit.*;
import org.junit.runner.RunWith;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import sp.data.dao.interfaces.OrderPositionDao;
import sp.data.entities.OrderPosition;

import static org.junit.Assert.*;
import static com.vladmihalcea.sql.SQLStatementCountValidator.*;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("unitils/dataset/integration/orderPosition/mainDataSet.xml")
public class OrderPositionDbIntegrationTest {

    @SpringBeanByType
    OrderPositionDao orderPositionDao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
    }

    @Test
    public void getById_ShouldMakeOneQuery(){
        orderPositionDao.getById(1L);
        assertSelectCount(1);
    }

    @Test
    public void getById_ProductShouldBeLoadedEagerly(){
        OrderPosition orderPosition = orderPositionDao.getById(1L);
        assertNotNull(orderPosition.getProduct());
        assertEquals(new Long(1), orderPosition.getProduct().getId());
        assertSelectCount(1);
    }

    @Test
    public void getById_OrderShouldBeLoadedLazily(){
        OrderPosition orderPosition = orderPositionDao.getById(1L);
        assertNotNull(orderPosition.getOrder());
        assertEquals(new Long(1), orderPosition.getOrder().getId());
        assertSelectCount(2);
    }

    @Test
    @ExpectedDataSet("unitils/dataset/integration/orderPosition/update_ShouldNotCascadeUpdateChild_Expected.xml")
    public void update_ShouldNotCascadeUpdateChild(){
        OrderPosition orderPosition = orderPositionDao.getById(1L);
        orderPosition.getProduct().setName("TestProduct");
        orderPosition.getOrder().setNote("TestNote");
        orderPosition.setNote("TestNote");
        orderPositionDao.update(orderPosition);
        assertUpdateCount(1);
    }

    @Test
    @ExpectedDataSet("unitils/dataset/integration/orderPosition/delete_ShouldNotCascadeDeleteChild.xml")
    public void delete_ShouldNotCascadeDeleteChild(){
        orderPositionDao.deleteById(1L);
        assertSelectCount(1);
        assertDeleteCount(1);
    }

}
