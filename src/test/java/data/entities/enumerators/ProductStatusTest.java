package data.entities.enumerators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.enumerators.ProductStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(BlockJUnit4ClassRunner.class)
public class ProductStatusTest {

    private final String [] names = {"В наличии", "Удалён", "Нет в наличии"};
    private final ProductStatus[] productStatuses = ProductStatus.values();

    @Test
    public void quantityTest(){
        assertEquals(names.length, productStatuses.length);
    }

    @Test
    public void getName_ShouldReturnAppropriateName(){
        for (int i = 0; i < productStatuses.length; i++) {
            assertEquals("i = " + i, names[i], productStatuses[i].getName());
        }
    }

    @Test
    public void getId_ShouldReturnAppropriateId(){
        for (int i = 0; i < productStatuses.length; i++) {
            assertEquals("i = " + i, i, productStatuses[i].getId());
        }
    }

    @Test
    public void getById_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < productStatuses.length; i++) {
            assertEquals("i = " + i, ProductStatus.getById(i), productStatuses[i]);
        }
    }

    @Test
    public void getById_NonexistentId_ShouldReturnNull(){
        ProductStatus productStatus = ProductStatus.getById(20);
        assertNull(productStatus);
    }

    @Test
    public void getByName_ShouldReturnAppropriateInstance(){
        for (int i = 0; i < productStatuses.length; i++) {
            assertEquals("i = " + i, ProductStatus.getByName(names[i]), productStatuses[i]);
        }
    }

    @Test
    public void getByName_NullName_ShouldReturnNull(){
        ProductStatus productStatus = ProductStatus.getByName(null);
        assertNull(productStatus);
    }

    @Test
    public void getByName_NonexistentName_ShouldReturnNull(){
        ProductStatus productStatus = ProductStatus.getByName("InvalidName");
        assertNull(productStatus);
    }
}