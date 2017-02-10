package data.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.OrderPosition;
import sp.data.entities.Product;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(BlockJUnit4ClassRunner.class)
public class OrderPositionTest {

    OrderPosition orderPosition = new OrderPosition();


    @Test
    public void setPriceOrdered_ShouldSetScaledValue() throws Exception {
        orderPosition.setPriceOrdered(new BigDecimal(20));
        assertEquals("", new BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_DOWN), orderPosition.getPriceOrdered());
    }

    @Test
    public void setPriceOrdered_InputNull_ShouldDoNothing() throws Exception {
        orderPosition.setPriceOrdered(new BigDecimal(20));
        orderPosition.setPriceOrdered(null);
        assertEquals("", new BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_DOWN), orderPosition.getPriceOrdered());
    }

    @Test
    public void setPriceVendor_ShouldSetScaledValue() throws Exception {
        orderPosition.setPriceVendor(new BigDecimal(20));
        assertEquals("", new BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_DOWN), orderPosition.getPriceVendor());
    }

    @Test
    public void setPriceVendor_InputNull_ShouldSet0() throws Exception {
        orderPosition.setPriceVendor(new BigDecimal(20));
        orderPosition.setPriceVendor(null);
        assertEquals("", new BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_DOWN), orderPosition.getPriceVendor());
    }

    @Test
    public void setPriceSp_ShouldSetScaledValue() throws Exception {
        orderPosition.setPriceSp(new BigDecimal(20));
        assertEquals("",  new BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_DOWN), orderPosition.getPriceSp());
    }

    @Test
    public void setPriceSp_InputNull_ShouldSet0() throws Exception {
        orderPosition.setPriceSp(new BigDecimal(20));
        orderPosition.setPriceSp(null);
        assertEquals("",  new BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_DOWN), orderPosition.getPriceSp());
    }

    @Test
    public void getWeight_ShouldReturnCalculatedWeight() {
        Product product = mock(Product.class);
        when(product.getWeight()).thenReturn(100);

        orderPosition.setProduct(product);
        orderPosition.setQuantity(2);
        assertEquals("", 200, orderPosition.getWeight());
    }

    @Test
    public void getWeight_ShouldReturn0_IfProductIsNull() {
        orderPosition.setProduct(null);
        assertEquals("", 0, orderPosition.getWeight());
    }

    @Test
    public void getIncome_ShouldReturnCalculatedIncome() {
        orderPosition.setPriceSp(new BigDecimal(100.55));
        orderPosition.setPriceVendor(new BigDecimal(90.15));
        orderPosition.setQuantity(2);
        assertEquals("", new BigDecimal(20.80).setScale(2, BigDecimal.ROUND_HALF_DOWN), orderPosition.getIncome());
    }

    @Test
    public void getPriceSpSummary_ShouldReturnCalculatedPriceSpSummary() {
        orderPosition.setPriceSp(new BigDecimal(100.55));
        orderPosition.setQuantity(2);
        assertEquals("", new BigDecimal(201.10).setScale(2, BigDecimal.ROUND_HALF_DOWN), orderPosition.getPriceSpSummary());
    }

}