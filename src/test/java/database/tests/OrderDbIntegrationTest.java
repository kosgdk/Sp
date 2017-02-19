package database.tests;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import net.sf.ehcache.CacheManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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
import sp.data.dao.interfaces.ClientDao;
import sp.data.dao.interfaces.OrderDao;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Client;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.entities.Sp;
import testservices.CauseExceptionMatcher;

import javax.persistence.NoResultException;
import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.assertNotNull;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/integration/order/OrderDbIntegrationTest.mainDataSet.xml")
public class OrderDbIntegrationTest {

    @SpringBeanByType
    OrderDao orderDao;

    @SpringBeanByType
    SpDao spDao;

    @SpringBeanByType
    ClientDao clientDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
    }


    //ORM test
    @Test
    public void getById_ClientShouldBeLoadedEagerly(){
        Order order = orderDao.getById(1L);
        order.getClient().getId();
        assertSelectCount(1);
    }

    //ORM test
    @Test
    public void getById_OrderPositionsShouldBeLoadedLazily(){
        Order order = orderDao.getById(1L);
        List<OrderPosition> orderPositions = order.getOrderPositions();
        assertNotNull(orderPositions);
        for (OrderPosition orderPosition : orderPositions) {
            orderPosition.getId();
        }
        assertSelectCount(2);
    }

    //ORM test
    @Test
    public void getById_SpShouldBeLoadedLazily(){
        Order order = orderDao.getById(1L);
        order.getSp().getId();
        assertSelectCount(2);
    }

    //DAO test
    @Test
    public void getByIdWithAllChildren_ShouldLoadAllRelationsInOneQuery(){
        Order order = orderDao.getByIdWithAllChildren(1L);
        assertNotNull(order.getClient().getId());
        assertNotNull(order.getOrderPositions().get(1).getId());
        assertNotNull(order.getSp().getId());
        assertSelectCount(1);
    }

    //DAO test
    @Test(expected = NoResultException.class)
    public void getByIdWithAllChildren_InputNonexistentId_ShouldThrowException(){
        orderDao.getByIdWithAllChildren(-1L);
    }

    //DAO test
    @Test(expected = NoResultException.class)
    public void getByIdWithAllChildren_InputNull_ShouldThrowException(){
        orderDao.getByIdWithAllChildren(null);
    }

    //ORM test
    @Test
    @ExpectedDataSet("db_test/dataset/integration/order/OrderDbIntegrationTest.update_ShouldCascadeUpdateOrderPositions_ExpectedDataSet.xml")
    public void update_ShouldCascadeUpdateOrderPositions(){
        Order order = orderDao.getById(1L);
        order.getOrderPositions().get(0).setNote("Test note");
        orderDao.update(order);
        assertUpdateCount(1);
    }

    //DB test
    @Test
    @ExpectedDataSet("db_test/dataset/integration/order/OrderDbIntegrationTest.ifSpRemoved_OrdersShouldBeRemovedCascade_ExpectedDataSet.xml")
    public void ifSpRemoved_OrdersShouldBeRemovedCascade() throws Exception {
        spDao.deleteById(1L);
        assertDeleteCount(1);
    }

    //DB test
    @Test
    public void ifSpNotSaved_OrderCanNotBeSaved(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "CONSTRAINT `sp` FOREIGN KEY (`sp`)"));
        Order order = new Order();
        Sp sp = new Sp();
        sp.setId(2L);
        order.setSp(sp);
        orderDao.save(order);
    }

    //DB test
    @Test
    public void clientCanNotBeRemovedWhileInReference() throws Throwable{
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "CONSTRAINT `client` FOREIGN KEY (`client`)"));
        try {
            clientDao.deleteById(1L);
        }catch (DataIntegrityViolationException e){
            throw e.getCause();
        }
    }

    //DB test
    @Test
    public void ifClientNotSaved_OrderCanNotBeSaved(){
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "CONSTRAINT `client` FOREIGN KEY (`client`)"));
        Order order = new Order();
        Client client = new Client();
        client.setId(2L);
        order.setClient(client);
        orderDao.save(order);
    }

}
