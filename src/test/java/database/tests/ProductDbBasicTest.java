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
import testservices.TestEntitiesCreationService;

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
        assertEquals("status", product.getStatus(), productFromDb.getStatus());
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

    @Test
    public void getById_InputNonexistentId_ShouldReturnNull(){
        Product product = dao.getById(4L);
        assertSelectCount(1);
        assertNull(product);
    }

    @Test
    public void getById_InputNull_ShouldReturnNull(){
        Product product = dao.getById(null);
        assertSelectCount(0);
        assertNull(product);
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
        product.setName("NewTestName");
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
    public void getByName_ShouldReturnProduct(){
        String name = "Product2";
        Product product = dao.getByName(name);
        assertSelectCount(1);
        assertNotNull(product);
        assertEquals(name, product.getName());
    }

    @Test
    public void getByName_CaseInsensitiveName_ShouldReturnProduct(){
        Product product = dao.getByName("product2");
        assertSelectCount(1);
        assertNotNull(product);
        assertEquals("Product2", product.getName());
    }

    @Test
    public void getByName_NullName_ShouldReturnNull(){
        Product product = dao.getByName(null);
        assertSelectCount(0);
        assertNull(product);
    }

    @Test
    public void getByName_NonexistentName_ShouldReturnNull(){
        String name = "Product3";
        Product product = dao.getByName(name);
        assertSelectCount(1);
        assertNull(product);
    }

    @Test
    public void searchByName_ShouldReturnListOfProducts(){
        List<Product> products = dao.searchByName("roduc");
        assertSelectCount(1);
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product1", products.get(0).getName());
        assertEquals("Product2", products.get(1).getName());
    }

    @Test
    public void searchByName_CaseInsensitiveName_ShouldReturnListOfProducts(){
        List<Product> products = dao.searchByName("RodUc");
        assertSelectCount(1);
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product1", products.get(0).getName());
        assertEquals("Product2", products.get(1).getName());
    }

    @Test
    public void searchByName_NullName_ShouldReturnNull(){
        List<Product> products = dao.searchByName(null);
        assertSelectCount(0);
        assertNull(products);
    }

    @Test
    public void searchByName_NonexistentName_ShouldReturnEmptyListOfProducts(){
        List<Product> products = dao.searchByName("foo");
        assertSelectCount(1);
        assertEquals(0, products.size());
    }
}