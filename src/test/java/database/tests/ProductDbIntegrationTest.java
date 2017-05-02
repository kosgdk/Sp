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
import sp.data.dao.interfaces.ProductDao;
import sp.data.entities.Product;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(value=TransactionMode.DISABLED)
@DataSet("db_test/dataset/integration/order/OrderDbIntegrationTest.mainDataSet.xml")
public class ProductDbIntegrationTest {

    @SpringBeanByType
    ProductDao productDao;


    @Before
    public void setUp(){
        SQLStatementCountValidator.reset();
        CacheManager.getInstance().clearAll();
    }

    @Test
    @DataSet("db_test/dataset/integration/product/ProductDbIntegrationTest.getZeroWeightProducts_ShouldReturnListOfProducts_DataSet.xml")
    public void getZeroWeightProducts_ShouldReturnListOfProducts(){
        List<Product> products = productDao.getZeroWeightProducts(1L);

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(new Long(2L), products.get(0).getId());
        assertEquals(new Long(4L), products.get(1).getId());
    }

    @Test
    @DataSet("db_test/dataset/integration/product/ProductDbIntegrationTest.getZeroWeightProducts_ShouldReturnListOfProducts_DataSet.xml")
    public void getZeroWeightProducts_InputNull_ShouldReturnEmptyListOfProducts(){
        List<Product> products = productDao.getZeroWeightProducts(null);

        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    @DataSet("db_test/dataset/integration/product/ProductDbIntegrationTest.getZeroWeightProducts_ShouldReturnListOfProducts_DataSet.xml")
    public void getZeroWeightProducts_InputNonexistent_ShouldReturnEmptyListOfProducts(){
        List<Product> products = productDao.getZeroWeightProducts(10L);

        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

}
