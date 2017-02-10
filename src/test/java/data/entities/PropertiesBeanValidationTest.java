package data.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.Properties;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class PropertiesBeanValidationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    Validator validator;

    Properties properties = new Properties();
    Errors errors = new BeanPropertyBindingResult(properties, "properties");


    @Test
    public void percentSpCanNotBeNull() throws NoSuchFieldException, IllegalAccessException {
        Field field = Properties.class.getDeclaredField("percentSp");
        field.setAccessible(true);
        field.set(properties, null);
        validator.validate(properties, errors);
        assertEquals("NotNull", errors.getFieldError("percentSp").getCode());
    }

    @Test
    public void percentSpCanNotBeNegative() {
        properties.setPercentSp(new BigDecimal(-0.1));
        validator.validate(properties, errors);
        assertEquals("DecimalMin", errors.getFieldError("percentSp").getCode());
    }

    @Test
    public void percentSpCanNotBeMoreThan1() {
        properties.setPercentSp(new BigDecimal(1.1));
        validator.validate(properties, errors);
        assertEquals("DecimalMax", errors.getFieldError("percentSp").getCode());
    }

    @Test
    public void percentSpValidValueTest() {
        properties.setPercentSp(new BigDecimal(0.5));
        validator.validate(properties, errors);
        assertNull(errors.getFieldError("percentSp"));
    }

    @Test
    public void percentDiscountCanNotBeNull() throws NoSuchFieldException, IllegalAccessException {
        Field field = Properties.class.getDeclaredField("percentDiscount");
        field.setAccessible(true);
        field.set(properties, null);
        properties.setPercentDiscount(null);
        validator.validate(properties, errors);
        assertEquals("NotNull", errors.getFieldError("percentDiscount").getCode());
    }

    @Test
    public void percentDiscountCanNotBeNegative() {
        properties.setPercentDiscount(new BigDecimal(-0.1));
        validator.validate(properties, errors);
        assertEquals("DecimalMin", errors.getFieldError("percentDiscount").getCode());
    }

    @Test
    public void percentDiscountCanNotBeMoreThan1() {
        properties.setPercentDiscount(new BigDecimal(1.1));
        validator.validate(properties, errors);
        assertEquals("DecimalMax", errors.getFieldError("percentDiscount").getCode());
    }

    @Test
    public void percentDiscountValidValueTest() {
        properties.setPercentDiscount(new BigDecimal(0.5));
        validator.validate(properties, errors);
        assertNull(errors.getFieldError("percentDiscount"));
    }

    @Test
    public void percentBankCommissionCanNotBeNull() throws NoSuchFieldException, IllegalAccessException {
        Field field = Properties.class.getDeclaredField("percentBankCommission");
        field.setAccessible(true);
        field.set(properties, null);
        validator.validate(properties, errors);
        assertEquals("NotNull", errors.getFieldError("percentBankCommission").getCode());
    }

    @Test
    public void percentBankCommissionCanNotBeNegative() {
        properties.setPercentBankCommission(new BigDecimal(-0.1));
        validator.validate(properties, errors);
        assertEquals("DecimalMin", errors.getFieldError("percentBankCommission").getCode());
    }

    @Test
    public void percentBankCommissionCanNotBeMoreThan1() {
        properties.setPercentBankCommission(new BigDecimal(1.1));
        validator.validate(properties, errors);
        assertEquals("DecimalMax", errors.getFieldError("percentBankCommission").getCode());
    }

    @Test
    public void percentBankCommissionValidValueTest() {
        properties.setPercentBankCommission(new BigDecimal(0.5));
        validator.validate(properties, errors);
        assertNull(errors.getFieldError("percentBankCommission"));
    }

}