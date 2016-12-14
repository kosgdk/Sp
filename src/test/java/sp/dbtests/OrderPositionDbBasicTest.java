package sp.dbtests;

import net.sf.ehcache.CacheManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.DatabaseUnitils;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import sp.data.dao.interfaces.OrderPositionDao;
import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.OrderPosition;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.*;


@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@RunWith(UnitilsJUnit4TestClassRunner.class)
@Transactional(value=TransactionMode.DISABLED)
@DataSet("unitils/dataset/OrderPositionDbBasicTest/mainDataSet.xml")
public class OrderPositionDbBasicTest {

    @SpringBeanByType
    OrderPositionDao orderPositionDao;


    @Before
    public void setUp(){
        reset(); //SQLStatementCountValidator
        CacheManager.getInstance().clearAll(); //Clearing 2nd level cache
        DatabaseUnitils.updateSequences();
    }

    @Test
    public void getById_ShouldReturnEntity(){
        OrderPosition orderPosition = orderPositionDao.getById(2L);
        assertSelectCount(1);
        assertNotNull(orderPosition);
        assertEquals("", new Long(2), orderPosition.getId());
    }

    @Test
    public void getById_InputWrongId_ShouldReturnNull(){
        OrderPosition orderPosition = orderPositionDao.getById(4L);
        assertSelectCount(1);
        assertNull(orderPosition);
    }

    @Test
    public void getById_InputNull_ShouldReturnNull(){
        OrderPosition orderPosition = orderPositionDao.getById(null);
        assertSelectCount(0);
        assertNull(orderPosition);
    }

    @Test
    public void getAll_ShouldReturnList(){
        List<OrderPosition> orderPositions = orderPositionDao.getAll();
        assertSelectCount(1);
        assertNotNull("", orderPositions);
        assertEquals("", 3, orderPositions.size());
        for (int i = 0; i < orderPositions.size(); i++) {
            assertNotNull("", orderPositions.get(i));
            assertEquals("", new Long(i+1), orderPositions.get(i).getId());
        }
    }

    @Test
    @ExpectedDataSet("unitils/dataset/OrderPositionDbBasicTest/save_ShouldWriteToDB_ExpectedDataSet.xml")
    public void save_ShouldWriteToDB(){
        OrderPosition orderPosition = new OrderPosition();
        orderPositionDao.save(orderPosition);
        assertInsertCount(1);
    }

    @Test
    @ExpectedDataSet("unitils/dataset/OrderPositionDbBasicTest/UpdateDB_ExpectedDataSet.xml")
    public void update_ShouldUpdateDB(){
        OrderPosition orderPosition = orderPositionDao.getById(2L);
        orderPosition.setNote("TestNote");
        orderPositionDao.update(orderPosition);
        assertUpdateCount(1);
    }

    @Test
    @ExpectedDataSet("unitils/dataset/OrderPositionDbBasicTest/delete_ShouldDeleteFromDB_ExpectedDataSet.xml")
    public void delete_ShouldDeleteFromDB(){
        OrderPosition orderPosition = orderPositionDao.getById(2L);
        orderPositionDao.delete(orderPosition);
        assertDeleteCount(1);
    }

    @Test
    public void delete_UnsavedEntity_ShouldNotFireDeleteQuery(){
        OrderPosition orderPosition = new OrderPosition();
        orderPositionDao.delete(orderPosition);
        assertDeleteCount(0);
    }

    @Test
    @ExpectedDataSet("unitils/dataset/OrderPositionDbBasicTest/delete_ShouldDeleteFromDB_ExpectedDataSet.xml")
    public void deleteById_ShouldDeleteFromDB(){
        orderPositionDao.deleteById(2L);
        assertSelectCount(1);
        assertDeleteCount(1);
    }
}
