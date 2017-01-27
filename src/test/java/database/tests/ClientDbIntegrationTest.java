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
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import sp.data.dao.interfaces.ClientDao;
import sp.data.entities.Client;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;

import java.util.*;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/integration/client/ClientDbIntegrationTest.mainDataSet.xml")
public class ClientDbIntegrationTest {

    @SpringBeanByType
    ClientDao clientDao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll(); //Clearing 2nd level cache
    }


    //ORM test
    @Test
    public void getById_OrdersShouldBeLoadedLazily(){
        Client client = clientDao.getById(1L);
        assertNotNull(client.getOrders());
        assertEquals(3, client.getOrders().size());
        assertSelectCount(2);
    }

    //ORM test
    @Test
    public void getById_OrdersShouldBeSortedByIdDesc(){
        Client client = clientDao.getById(1L);
        Set<Order> orders = client.getOrders();
        Long i = 3L;
        for (Order order : orders) {
            assertEquals(i, order.getId());
            i--;
        }
    }

    //DAO test
    @Test
    @DataSet("db_test/dataset/integration/client/ClientDbIntegrationTest.getByIdWithAllChildren_ShouldLoadAllRelationsInOneQuery.xml")
    public void getByIdWithAllChildren_ShouldLoadAllRelationsInOneQuery(){
        Client client = clientDao.getByIdWithAllChildren(1L);

        assertEquals(new Long(1), client.getId());

        assertNotNull(client.getOrders());
        assertEquals(1, client.getOrders().size());

        Order order = client.getOrders().iterator().next();
        assertNotNull(order);
        assertEquals(new Long(1), order.getId());

        assertNotNull(order.getSp());
        assertEquals(new Long(1), order.getSp().getId());

        assertNotNull(order.getOrderPositions());
        assertEquals(1, order.getOrderPositions().size());

        OrderPosition orderPosition = order.getOrderPositions().get(0);
        assertNotNull(orderPosition);
        assertEquals(new Long(1), orderPosition.getId());

        assertNotNull(orderPosition.getProduct());
        assertEquals(new Long(1), orderPosition.getProduct().getId());

        assertSelectCount(1);
    }

}
