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
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Sp;
import database.services.TestEntitiesCreationService;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/basic/sp/SpDbBasicTest.mainDataSet.xml")
public class SpDbBasicTest {

    @SpringBeanByType
    SpDao dao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll(); //Clearing 2nd level cache
        DatabaseUnitils.updateSequences();
    }


    @Test
    public void allColumnsCanBeWrittenAndRead(){
        TestEntitiesCreationService service = new TestEntitiesCreationService();
        Sp sp = service.createTestSp(dao.getLastNumber()+1);
        dao.save(sp);
        Sp spFromDb = dao.getById(sp.getId());
        assertSelectCount(3);
        assertEquals("id", sp.getId(), spFromDb.getId());
        assertEquals("percent", sp.getPercent(), spFromDb.getPercent());
        assertEquals("status", sp.getDateStart(), spFromDb.getDateStart());
        assertEquals("dateStart", sp.getDateStart(), spFromDb.getDateStart());
        assertEquals("dateEnd", sp.getDateEnd(), spFromDb.getDateEnd());
        assertEquals("dateToPay", sp.getDateToPay(), spFromDb.getDateToPay());
        assertEquals("dateSent", sp.getDateSent(), spFromDb.getDateSent());
        assertEquals("dateToReceive", sp.getDateToReceive(), spFromDb.getDateToReceive());
        assertEquals("dateReceived", sp.getDateReceived(), spFromDb.getDateReceived());
        assertEquals("dateToDistribute", sp.getDateToDistribute(), spFromDb.getDateToDistribute());
    }
    
    @Test
    public void getById_ShouldReturnEntity(){
        Sp sp = dao.getById(2L);
        assertSelectCount(1);
        assertNotNull(sp);
        assertEquals("", new Long(2), sp.getId());
    }

    @Test(expected = NoResultException.class)
    public void getById_InputNonexistentId_ShouldThrowException(){
        dao.getById(4L);
        assertSelectCount(1);
    }

    @Test
    public void getAll_ShouldReturnListOfEntities(){
        List<Sp> spList = dao.getAll();
        assertSelectCount(1);
        assertNotNull("", spList);
        assertEquals("", 3, spList.size());
        for (int i = 0; i < spList.size(); i++) {
            assertNotNull("", spList.get(i));
            assertEquals("", new Long(i+1), spList.get(i).getId());
        }
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/sp/SpDbBasicTest.save_ShouldWriteToDb_ExpectedDataSet.xml")
    public void save_ShouldWriteToDb(){
        Sp sp = new Sp();
        sp.setId(4L);
        dao.save(sp);
        assertSelectCount(1);
        assertInsertCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/sp/SpDbBasicTest.update_ShouldUpdateDb_ExpectedDataSet.xml")
    public void update_ShouldUpdateDb(){
        Sp sp = dao.getById(2L);
        sp.setPercent(new BigDecimal(0.1));
        dao.update(sp);
        assertUpdateCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/sp/SpDbBasicTest.delete_ShouldDeleteFromDb_ExpectedDataSet.xml")
    public void delete_ShouldDeleteFromDb(){
        Sp sp = dao.getById(2L);
        dao.delete(sp);
        assertDeleteCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/sp/SpDbBasicTest.delete_ShouldDeleteFromDb_ExpectedDataSet.xml")
    public void deleteById_ShouldDeleteFromDb(){
        dao.deleteById(2L);
        assertSelectCount(1);
        assertDeleteCount(1);
    }

    @Test
    public void getLastNumber_ShouldReturnNumberOfLastSp() {
        assertEquals(new Long(3L), dao.getLastNumber());
        assertSelectCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/sp/SpDbBasicTest.mainDataSet.xml")
    public void incrementIdGeneratorShouldReuseFreedIdAfterDelete() {
        dao.deleteById(3L);
        assertDeleteCount(1);
        Sp sp = new Sp();
        dao.save(sp);
    }
}