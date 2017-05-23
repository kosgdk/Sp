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
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import testservices.TestEntitiesCreationService;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/basic/order/OrderDbBasicTest.mainDataSet.xml")
public class OrderDbBasicTest {

    @SpringBeanByType
    OrderDao dao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
        DatabaseUnitils.updateSequences();
    }


    @Test
    public void allColumnsCanBeWrittenAndRead(){
        TestEntitiesCreationService service = new TestEntitiesCreationService();
        Order order = service.createTestOrderNoReferences(1);
        dao.save(order);
        Order orderFromDb = dao.getById(order.getId());
        assertSelectCount(1);
        assertEquals("id", order.getId(), orderFromDb.getId());
        assertEquals("weight", order.getWeight(), orderFromDb.getWeight());
        assertEquals("status", order.getStatus(), orderFromDb.getStatus());
        assertEquals("prepaid", order.getPrepaid(), orderFromDb.getPrepaid());
        assertEquals("note", order.getNote(), orderFromDb.getNote());
        assertEquals("dateOrdered", order.getDateOrdered(), orderFromDb.getDateOrdered());
        assertEquals("dateCompleted", order.getDateCompleted(), orderFromDb.getDateCompleted());
        assertEquals("deliveryPrice", order.getDeliveryPrice(), orderFromDb.getDeliveryPrice());
        assertEquals("place", order.getPlace(), orderFromDb.getPlace());
    }

    @Test
    public void getById_ShouldReturnEntity(){
        Order order = dao.getById(2L);
        assertSelectCount(1);
        assertNotNull(order);
        assertEquals("", new Long(2), order.getId());
    }

    @Test
    public void getById_InputNonexistentId_ShouldReturnNull(){
        Order order = dao.getById(4L);
        assertSelectCount(1);
        assertNull(order);
    }

    @Test
    public void getById_InputNull_ShouldReturnNull(){
        Order order = dao.getById(null);
        assertSelectCount(0);
        assertNull(order);
    }

    @Test
    public void getAll_ShouldReturnListOfEntities(){
        List<Order> orders = dao.getAll();
        assertSelectCount(1);
        assertNotNull("", orders);
        assertEquals("", 3, orders.size());
        for (int i = 0; i < orders.size(); i++) {
            assertNotNull("", orders.get(i));
            assertEquals("", new Long(i+1), orders.get(i).getId());
        }
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/order/OrderDbBasicTest.save_ShouldWriteToDb_ExpectedDataSet.xml")
    public void save_ShouldWriteToDb(){
        Order order = new Order();
        dao.save(order);
        assertInsertCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/order/OrderDbBasicTest.update_ShouldUpdateDb_ExpectedDataSet.xml")
    public void update_ShouldUpdateDb(){
        Order order = dao.getById(2L);
        order.setNote("TestNote");
        dao.update(order);
        assertUpdateCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/order/OrderDbBasicTest.delete_ShouldDeleteFromDb_ExpectedDataSet.xml")
    public void delete_ShouldDeleteFromDb(){
        Order order = dao.getById(2L);
        dao.delete(order);
        assertDeleteCount(1);
    }

    @Test
    public void delete_UnsavedEntity_ShouldNotFireDeleteQuery(){
        Order order = new Order();
        dao.delete(order);
        assertDeleteCount(0);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/order/OrderDbBasicTest.delete_ShouldDeleteFromDb_ExpectedDataSet.xml")
    public void deleteById_ShouldDeleteFromDb(){
        dao.deleteById(2L);
        assertSelectCount(1);
        assertDeleteCount(1);
    }

    @Test
    public void deleteById_InputNonExistentId_ShouldNotDeleteAnything(){
        dao.deleteById(4L);
        assertSelectCount(1);
        assertDeleteCount(0);
    }

    @Test
    public void deleteById_InputNull_ShouldNotDeleteAnything(){
        dao.deleteById(null);
        assertSelectCount(0);
        assertDeleteCount(0);
    }

    @Test
    @DataSet("db_test/dataset/basic/order/OrderDbBasicTest.updateStatuses_shouldUpdateStatusesOfOrdersInSp.xml")
    @ExpectedDataSet("db_test/dataset/basic/order/OrderDbBasicTest.updateStatuses_shouldUpdateStatusesOfOrdersInSp_ExpectedDataSet.xml")
    public void updateStatuses_ShouldUpdateStatusesOfOrdersInSp() {
        Sp sp = mock(Sp.class);
        when(sp.getId()).thenReturn(1L);
        dao.updateStatuses(sp, OrderStatus.PACKING);
        assertUpdateCount(1);
    }

    @Test
    public void updateStatuses_InputNullSp_ShouldDoNothing() {
        dao.updateStatuses(null, OrderStatus.PACKING);
        assertUpdateCount(0);
    }

    @Test
    public void updateStatuses_InputNullOrderStatus_ShouldDoNothing() {
        dao.updateStatuses(null, OrderStatus.PACKING);
        assertUpdateCount(0);
    }

    @Test
    public void updateStatuses_InputNullSpAndNullOrderStatus_ShouldDoNothing() {
        dao.updateStatuses(null, OrderStatus.PACKING);
        assertUpdateCount(0);
    }
}