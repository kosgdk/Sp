package database.tests;

import com.vladmihalcea.sql.SQLStatementCountValidator;
import net.sf.ehcache.CacheManager;
import org.junit.Before;
import org.junit.Test;
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
import testservices.TestEntitiesCreationService;

import javax.persistence.NoResultException;
import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.*;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/basic/orderPosition/OrderPositionDbBasicTest.mainDataSet.xml")
public class OrderPositionDbBasicTest {

    @SpringBeanByType
    OrderPositionDao dao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
        DatabaseUnitils.updateSequences();
    }


    @Test
    public void allColumnsCanBeWrittenAndRead(){
        TestEntitiesCreationService service = new TestEntitiesCreationService();
        OrderPosition orderPosition = service.createTestOrderPositionNoReferences(1);

        dao.save(orderPosition);
        OrderPosition orderPositionFromDb = dao.getById(orderPosition.getId());
        assertSelectCount(1);
        assertEquals("id", orderPosition.getId(), orderPositionFromDb.getId());
        assertEquals("priceOrdered", orderPosition.getPriceOrdered(), orderPositionFromDb.getPriceOrdered());
        assertEquals("priceSp", orderPosition.getPriceSp(), orderPositionFromDb.getPriceSp());
        assertEquals("priceVendor", orderPosition.getPriceVendor(), orderPositionFromDb.getPriceVendor());
        assertEquals("quantity", orderPosition.getQuantity(), orderPositionFromDb.getQuantity());
        assertEquals("note", orderPosition.getNote(), orderPositionFromDb.getNote());
    }

    @Test
    public void getById_ShouldReturnEntity(){
        OrderPosition orderPosition = dao.getById(2L);
        assertSelectCount(1);
        assertNotNull(orderPosition);
        assertEquals("", new Long(2), orderPosition.getId());
    }

    @Test(expected = NoResultException.class)
    public void getById_InputNonexistentId_ShouldThrowException(){
        dao.getById(4L);
        assertSelectCount(1);
    }

    @Test(expected = NoResultException.class)
    public void getById_InputNull_ShouldThrowException(){
        dao.getById(null);
        assertSelectCount(0);
    }

    @Test
    public void getAll_ShouldReturnListOfEntities(){
        List<OrderPosition> orderPositions = dao.getAll();
        assertSelectCount(1);
        assertNotNull("", orderPositions);
        assertEquals("", 3, orderPositions.size());
        for (int i = 0; i < orderPositions.size(); i++) {
            assertNotNull("", orderPositions.get(i));
            assertEquals("", new Long(i+1), orderPositions.get(i).getId());
        }
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/orderPosition/OrderPositionDbBasicTest.save_ShouldWriteToDb_ExpectedDataSet.xml")
    public void save_ShouldWriteToDb(){
        OrderPosition orderPosition = new OrderPosition();
        dao.save(orderPosition);
        assertInsertCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/orderPosition/OrderPositionDbBasicTest.update_ShouldUpdateDb_ExpectedDataSet.xml")
    public void update_ShouldUpdateDb(){
        OrderPosition orderPosition = dao.getById(2L);
        orderPosition.setNote("TestNote");
        dao.update(orderPosition);
        assertUpdateCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/orderPosition/OrderPositionDbBasicTest.delete_ShouldDeleteFromDb_ExpectedDataSet.xml")
    public void delete_ShouldDeleteFromDb(){
        OrderPosition orderPosition = dao.getById(2L);
        dao.delete(orderPosition);
        assertDeleteCount(1);
    }

    @Test
    public void delete_UnsavedEntity_ShouldNotFireDeleteQuery(){
        OrderPosition orderPosition = new OrderPosition();
        dao.delete(orderPosition);
        assertDeleteCount(0);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/orderPosition/OrderPositionDbBasicTest.delete_ShouldDeleteFromDb_ExpectedDataSet.xml")
    public void deleteById_ShouldDeleteFromDb(){
        dao.deleteById(2L);
        assertSelectCount(1);
        assertDeleteCount(1);
    }

    @Test(expected = NoResultException.class)
    public void deleteById_InputNonExistentId_ShouldThrowException(){
        dao.deleteById(4L);
    }

    @Test(expected = NoResultException.class)
    public void deleteById_InputNull_ShouldThrowException(){
        dao.deleteById(null);
    }
}
