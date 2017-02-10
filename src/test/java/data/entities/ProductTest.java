package data.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.Product;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;


@RunWith(BlockJUnit4ClassRunner.class)
public class ProductTest {

    Product product = new Product();


    @Test
    public void setPrice_ShouldSetScaledValue() throws Exception {
        product.setPrice(new BigDecimal(20.15));
        assertEquals("",  new BigDecimal(20.15).setScale(2, BigDecimal.ROUND_HALF_DOWN), product.getPrice());
    }

    @Test
    public void setPrice_InputNull_ShouldSet0() throws Exception {
        product.setPrice(null);
        assertEquals("",  new BigDecimal(0), product.getPrice());
    }

}