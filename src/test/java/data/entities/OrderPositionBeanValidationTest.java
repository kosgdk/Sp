package data.entities;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.*;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class OrderPositionBeanValidationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    Validator validator;

    OrderPosition orderPosition = new OrderPosition();
    Errors errors = new BeanPropertyBindingResult(orderPosition, "orderPosition");

    
    @Test
    public void orderCanNotBeNull() {
        validator.validate(orderPosition, errors);
        assertEquals("NotNull", errors.getFieldError("order").getCode());
    }

    @Test
    public void validOrderTest() {
        orderPosition.setOrder(new Order());
        validator.validate(orderPosition, errors);
        assertNull(errors.getFieldError("order"));
    }

    @Test
    public void productCanNotBeNull() {
        orderPosition.setProduct(null);
        validator.validate(orderPosition, errors);
        assertEquals("NotNull", errors.getFieldError("product").getCode());
    }

    @Test
    public void validProductTest() {
        orderPosition.setProduct(new Product());
        validator.validate(orderPosition, errors);
        assertNull(errors.getFieldError("product"));
    }

    @Test
    public void priceOrderedCanNotBeNull() {
        validator.validate(orderPosition, errors);
        assertEquals("NotNull", errors.getFieldError("priceOrdered").getCode());
    }

    @Test
    public void priceOrderedCanNotBeNegative() {
        orderPosition.setPriceOrdered(new BigDecimal(-1));
        validator.validate(orderPosition, errors);
        assertEquals("Min", errors.getFieldError("priceOrdered").getCode());
    }

    @Test
    public void validPriceOrderedValueTest() {
        orderPosition.setPriceOrdered(new BigDecimal(0));
        validator.validate(orderPosition, errors);
        assertNull(errors.getFieldError("priceOrdered"));
    }

    @Test
    public void priceVendorCanNotBeNull() {
        validator.validate(orderPosition, errors);
        assertEquals("NotNull", errors.getFieldError("priceVendor").getCode());
    }

    @Test
    public void priceVendorCanNotBeNegative() {
        orderPosition.setPriceVendor(new BigDecimal(-1));
        validator.validate(orderPosition, errors);
        assertEquals("Min", errors.getFieldError("priceVendor").getCode());
    }

    @Test
    public void validPriceVendorValueTest() {
        orderPosition.setPriceVendor(new BigDecimal(0));
        validator.validate(orderPosition, errors);
        assertNull(errors.getFieldError("priceVendor"));
    }

    @Test
    public void priceSpCanNotBeNull() {
        validator.validate(orderPosition, errors);
        assertEquals("NotNull", errors.getFieldError("priceSp").getCode());
    }

    @Test
    public void priceSpCanNotBeNegative() {
        orderPosition.setPriceSp(new BigDecimal(-1));
        validator.validate(orderPosition, errors);
        assertEquals("Min", errors.getFieldError("priceSp").getCode());
    }

    @Test
    public void validPriceSpValueTest() {
        orderPosition.setPriceSp(new BigDecimal(0));
        validator.validate(orderPosition, errors);
        assertNull(errors.getFieldError("priceSp"));
    }

    @Test
    public void quantityCanNotBeNull() {
        orderPosition.setQuantity(null);
        validator.validate(orderPosition, errors);
        assertEquals("NotNull", errors.getFieldError("quantity").getCode());
    }

    @Test
    public void quantityCanNotBeLessThan1() {
        orderPosition.setQuantity(0);
        validator.validate(orderPosition, errors);
        assertEquals("Min", errors.getFieldError("quantity").getCode());
    }

    @Test
    public void validQuantityValueTest() {
        orderPosition.setQuantity(1);
        validator.validate(orderPosition, errors);
        assertNull(errors.getFieldError("quantity"));
    }

    @Test
    public void noteCanBeNull() {
        validator.validate(orderPosition, errors);
        assertNull(errors.getFieldError("note"));
    }

    @Test
    public void noteCanNotBeLongerThan500Characters() {
        orderPosition.setNote(StringUtils.leftPad("", 501, "a"));
        validator.validate(orderPosition, errors);
        assertEquals("Size", errors.getFieldError("note").getCode());
    }

    @Test
    public void noteCanBe500CharactersLong() {
        orderPosition.setNote(StringUtils.leftPad("", 500, "a"));
        validator.validate(orderPosition, errors);
        assertNull(errors.getFieldError("note"));
    }

}