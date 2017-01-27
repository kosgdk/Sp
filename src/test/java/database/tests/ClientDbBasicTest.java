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
import sp.data.dao.interfaces.ClientDao;
import sp.data.entities.Client;
import database.services.TestEntitiesCreationService;

import javax.persistence.NoResultException;
import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/basic/client/ClientDbBasicTest.mainDataSet.xml")
public class ClientDbBasicTest {

    @SpringBeanByType
    ClientDao dao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
        DatabaseUnitils.updateSequences();
    }


    @Test
    public void allColumnsCanBeWrittenAndRead() {
        TestEntitiesCreationService service = new TestEntitiesCreationService();
        Client client = service.createTestClient(1);
        dao.save(client);
        Client clientFromDb = dao.getById(client.getId());
        assertSelectCount(1);
        assertEquals("id", client.getId(), clientFromDb.getId());
        assertEquals("name", client.getName(), clientFromDb.getName());
        assertEquals("realName", client.getRealName(), clientFromDb.getRealName());
        assertEquals("phone", client.getPhone(), clientFromDb.getPhone());
        assertEquals("note", client.getNote(), clientFromDb.getNote());
        assertEquals("referrer", client.getClientReferrer(), clientFromDb.getClientReferrer());
    }

    @Test
    public void getById_ShouldReturnEntity(){
        Client client = dao.getById(2L);
        assertSelectCount(1);
        assertNotNull(client);
        assertEquals("", new Long(2), client.getId());
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
        List<Client> clients = dao.getAll();
        assertSelectCount(1);
        assertNotNull("", clients);
        assertEquals("", 3, clients.size());
        for (int i = 0; i < clients.size(); i++) {
            assertNotNull("", clients.get(i));
            assertEquals("", new Long(i+1), clients.get(i).getId());
        }
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/client/ClientDbBasicTest.save_ShouldWriteToDb_ExpectedDataSet.xml")
    public void save_ShouldWriteToDb(){
        Client client = new Client();
        dao.save(client);
        assertInsertCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/client/ClientDbBasicTest.update_ShouldUpdateDb_ExpectedDataSet.xml")
    public void update_ShouldUpdateDb(){
        Client client = dao.getById(2L);
        client.setNote("TestNote");
        dao.update(client);
        assertUpdateCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/client/ClientDbBasicTest.delete_ShouldDeleteFromDb.xml")
    public void delete_ShouldDeleteFromDb(){
        Client client = dao.getById(2L);
        dao.delete(client);
        assertDeleteCount(1);
    }

    @Test
    public void delete_UnsavedEntity_ShouldNotFireDeleteQuery(){
        Client client = new Client();
        dao.delete(client);
        assertDeleteCount(0);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/client/ClientDbBasicTest.delete_ShouldDeleteFromDb.xml")
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
