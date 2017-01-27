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
import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.Product;
import database.services.TestEntitiesCreationService;

import javax.persistence.NoResultException;
import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.*;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/basic/product/ProductDbBasicTest.mainDataSet.xml")
public class ProductDbBasicTest {

    @SpringBeanByType
    ProductDao dao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
        DatabaseUnitils.updateSequences();
    }


    @Test
    public void allColumnsCanBeWrittenAndRead() {
        TestEntitiesCreationService service = new TestEntitiesCreationService();
        Product product = service.createTestProduct(1);
        dao.save(product);
        Product productFromDb = dao.getById(product.getId());
        assertSelectCount(1);
        assertEquals("id", product.getId(), productFromDb.getId());
        assertEquals("name", product.getName(), productFromDb.getName());
        assertEquals("price", product.getPrice(), productFromDb.getPrice());
        assertEquals("link", product.getLink(), productFromDb.getLink());
        assertEquals("vkId", product.getVkId(), productFromDb.getVkId());
        assertEquals("imageLink", product.getImageLink(), productFromDb.getImageLink());
        assertEquals("deleted", product.getDeleted(), productFromDb.getDeleted());
        assertEquals("vkPhotoId", product.getVkPhotoId(), productFromDb.getVkPhotoId());
        assertEquals("weight", product.getWeight(), productFromDb.getWeight());
    }

    @Test
    public void getById_ShouldReturnEntity(){
        Product product = dao.getById(2L);
        assertSelectCount(1);
        assertNotNull(product);
        assertEquals(new Long(2), product.getId());
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
        List<Product> products = dao.getAll();
        assertSelectCount(1);
        assertNotNull("", products);
        assertEquals("", 3, products.size());
        for (int i = 0; i < products.size(); i++) {
            assertNotNull("", products.get(i));
            assertEquals("", new Long(i+1), products.get(i).getId());
        }
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/product/ProductDbBasicTest.save_ShouldWriteToDb_ExpectedDataSet.xml")
    public void save_ShouldWriteToDb(){
        Product product = new Product();
        dao.save(product);
        assertInsertCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/product/ProductDbBasicTest.update_ShouldUpdateDb_ExpectedDataSet.xml")
    public void update_ShouldUpdateDb(){
        Product product = dao.getById(2L);
        product.setName("TestName");
        dao.update(product);
        assertUpdateCount(1);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/product/ProductDbBasicTest.delete_ShouldDeleteFromDb_ExpectedDataSet.xml")
    public void delete_ShouldDeleteFromDb(){
        Product product = dao.getById(2L);
        dao.delete(product);
        assertDeleteCount(1);
    }

    @Test
    public void delete_UnsavedEntity_ShouldNotFireDeleteQuery(){
        Product product = new Product();
        dao.delete(product);
        assertDeleteCount(0);
    }

    @Test
    @ExpectedDataSet("db_test/dataset/basic/product/ProductDbBasicTest.delete_ShouldDeleteFromDb_ExpectedDataSet.xml")
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