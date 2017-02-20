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
import sp.data.dao.interfaces.PropertiesDao;
import sp.data.entities.Properties;
import testservices.TestEntitiesCreationService;

import java.math.BigDecimal;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.assertEquals;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/basic/properties/ProductDbBasicTest.mainDataSet.xml")
public class PropertiesDbBasicTest {

    @SpringBeanByType
    PropertiesDao dao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
        DatabaseUnitils.updateSequences();
    }


    @Test
    public void allColumnsCanBeWrittenAndRead() {
        TestEntitiesCreationService service = new TestEntitiesCreationService();
        Properties properties = service.createTestProperties();
        dao.save(properties);
        Properties propertiesFromDb = dao.getProperties();
        assertSelectCount(1);
        assertEquals("id", properties.getId(), propertiesFromDb.getId());
        assertEquals("percentSp", properties.getPercentSp(), propertiesFromDb.getPercentSp());
        assertEquals("percentDiscount", properties.getPercentDiscount(), propertiesFromDb.getPercentDiscount());
        assertEquals("percentBankCommission", properties.getPercentBankCommission(), propertiesFromDb.getPercentBankCommission());
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/properties/ProductDbBasicTest.save_ShouldWriteToDb_ExpectedDataSet.xml")
    public void save_ShouldWriteToDb(){
        Properties properties = new Properties();
        dao.save(properties);
        assertInsertCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/properties/ProductDbBasicTest.update_ShouldUpdateDb_ExpectedDataSet.xml")
    public void update_ShouldUpdateDb(){
        Properties properties = dao.getProperties();
        properties.setPercentSp(new BigDecimal(0.11));
        dao.update(properties);
        assertUpdateCount(1);
    }
    
}