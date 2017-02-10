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
import sp.data.entities.Product;
import sp.data.entities.enumerators.ProductStatus;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class ProductBeanValidationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    Validator validator;

    Product product = new Product();
    Errors errors = new BeanPropertyBindingResult(product, "product");


    @Test
    public void nameCanNotBeNull() {
        validator.validate(product, errors);
        assertEquals("NotNull", errors.getFieldError("name").getCode());
    }

    @Test
    public void nameCanNotBeLongerThan300Characters() {
        product.setName(StringUtils.leftPad("", 301, "a"));
        validator.validate(product, errors);
        assertEquals("Size", errors.getFieldError("name").getCode());
    }

    @Test
    public void nameCanBe300CharactersLong() {
        product.setName(StringUtils.leftPad("", 300, "a"));
        validator.validate(product, errors);
        assertNull(errors.getFieldError("name"));
    }
    
    @Test
    public void linkCanNotBeNull() {
        validator.validate(product, errors);
        assertEquals("NotNull", errors.getFieldError("link").getCode());
    }

    @Test
    public void linkCanNotBeLongerThan300Characters() {
        product.setLink(StringUtils.leftPad("", 301, "a"));
        validator.validate(product, errors);
        assertEquals("Size", errors.getFieldError("link").getCode());
    }

    @Test
    public void linkCanBe300CharactersLong() {
        product.setLink(StringUtils.leftPad("", 300, "a"));
        validator.validate(product, errors);
        assertNull(errors.getFieldError("link"));
    }

    @Test
    public void weightCanNotBeNull() {
        product.setWeight(null);
        validator.validate(product, errors);
        assertEquals("NotNull", errors.getFieldError("weight").getCode());
    }

    @Test
    public void weightCanNotBeNegative() {
        product.setWeight(-1);
        validator.validate(product, errors);
        assertEquals("Min", errors.getFieldError("weight").getCode());
    }

    @Test
    public void validWeightValueTest() {
        product.setWeight(0);
        validator.validate(product, errors);
        assertNull(errors.getFieldError("weight"));
    }

    @Test
    public void priceCanNotBeNull() throws NoSuchFieldException, IllegalAccessException {
        Field field = Product.class.getDeclaredField("price");
        field.setAccessible(true);
        field.set(product, null);
        validator.validate(product, errors);
        assertEquals("NotNull", errors.getFieldError("price").getCode());
    }

    @Test
    public void priceCanNotBeNegative() {
        product.setPrice(new BigDecimal(-1));
        validator.validate(product, errors);
        assertEquals("Min", errors.getFieldError("price").getCode());
    }

    @Test
    public void validPriceValueTest() {
        product.setPrice(new BigDecimal(0));
        validator.validate(product, errors);
        assertNull(errors.getFieldError("price"));
    }

    @Test
    public void vkIdCanNotBeNegative(){
        product.setVkId(-1);
        validator.validate(product, errors);
        assertEquals("Min", errors.getFieldError("vkId").getCode());
    }

    @Test
    public void vkIdValidValueTest() {
        product.setVkId(0);
        validator.validate(product, errors);
        assertNull(errors.getFieldError("vkId"));
    }

    @Test
    public void imageLinkCanBeNull() {
        product.setImageLink(null);
        validator.validate(product, errors);
        assertNull(errors.getFieldError("imageLink"));
    }

    @Test
    public void imageLinkCanNotBeLongerThan300Characters() {
        product.setImageLink(StringUtils.leftPad("", 301, "a"));
        validator.validate(product, errors);
        assertEquals("Size", errors.getFieldError("imageLink").getCode());
    }

    @Test
    public void imageLinkCanBe300CharactersLong() {
        product.setImageLink(StringUtils.leftPad("", 300, "a"));
        validator.validate(product, errors);
        assertNull(errors.getFieldError("imageLink"));
    }

    @Test
    public void statusCanNotBeNull() {
        product.setStatus(null);
        validator.validate(product, errors);
        assertEquals("NotNull", errors.getFieldError("status").getCode());
    }

    @Test
    public void validStatusTest() {
        product.setStatus(ProductStatus.AVAILABLE);
        validator.validate(product, errors);
        assertNull(errors.getFieldError("status"));
    }

    @Test
    public void vkPhotoIdCanNotBeNegative(){
        product.setVkPhotoId(-1);
        validator.validate(product, errors);
        assertEquals("Min", errors.getFieldError("vkPhotoId").getCode());
    }

    @Test
    public void vkPhotoIdValidValueTest() {
        product.setVkPhotoId(0);
        validator.validate(product, errors);
        assertNull(errors.getFieldError("vkPhotoId"));
    }

}