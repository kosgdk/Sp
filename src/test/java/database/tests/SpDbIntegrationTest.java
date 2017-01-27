package database.tests;

import com.vladmihalcea.sql.SQLStatementCountValidator;
import net.sf.ehcache.CacheManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.*;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Set;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/integration/sp/SpDbIntegrationTest.mainDataSet.xml")
public class SpDbIntegrationTest {

    @SpringBeanByType
    SpDao spDao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
    }


    //ORM test
    @Test
    public void getById_OrdersShouldBeLoadedLazily(){
        Sp sp = spDao.getById(1L);
        Set<Order> orders = sp.getOrders();
        assertNotNull(orders);
        assertEquals(3, orders.size());
        for (Order order : orders) {
            order.getId();
        }
        assertSelectCount(2);
    }

    //ORM test
    @Test
    public void getById_OrdersShouldBeSortedById(){
        Sp sp = spDao.getById(1L);
        Set<Order> orders = sp.getOrders();
        Long i = 1L;
        for (Order order : orders) {
            assertEquals(i, order.getId());
            i++;
        }
    }

    //ORM test
    @Test
    @ExpectedDataSet("db_test/dataset/integration/sp/SpDbIntegrationTest.update_OrdersShouldBeCascadeUpdated.xml")
    public void update_OrdersShouldBeCascadeUpdated(){
        Sp sp = spDao.getById(1L);
        Set<Order> orders = sp.getOrders();
        for (Order order : orders) {
            order.setNote("Test note");
        }
        spDao.update(sp);
        assertUpdateCount(3);
    }

    //DAO test
    @Test
    @DataSet("db_test/dataset/integration/sp/SpDbIntegrationTest.getByIdWithAllChildren_ShouldLoadAllRelationsInOneQuery.xml")
    public void getByIdWithAllChildren_ShouldLoadAllRelationsInOneQuery(){
        Sp sp = spDao.getByIdWithAllChildren(1L);

        assertNotNull(sp);
        assertEquals(new Long(1), sp.getId());

        Set<Order> orders = sp.getOrders();
        assertNotNull(orders);

        Order order = orders.iterator().next();
        assertNotNull(order);
        assertEquals(new Long(1), order.getId());

        Client client = order.getClient();
        assertNotNull(client);
        assertEquals(new Long(1), client.getId());

        List<OrderPosition> orderPositions = order.getOrderPositions();
        assertNotNull(orderPositions);

        OrderPosition orderPosition = orderPositions.get(0);
        assertNotNull(orderPosition);
        assertEquals(new Long(1), orderPosition.getId());

        Product product = orderPosition.getProduct();
        assertNotNull(product);
        assertEquals(new Long(1), product.getId());

        assertSelectCount(1);
    }

    //DAO test
    @Test(expected = NoResultException.class)
    public void getByIdWithAllChildren_InputNonexistentId_ShouldThrowException(){
        spDao.getByIdWithAllChildren(2L);
    }

    //DAO test
    @Test(expected = NoResultException.class)
    public void getByIdWithAllChildren_InputNull_ShouldThrowException(){
        spDao.getByIdWithAllChildren(null);
    }

}