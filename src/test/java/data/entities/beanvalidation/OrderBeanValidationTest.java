package data.entities.beanvalidation;

import testservices.DateService;
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
import sp.data.entities.Client;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class OrderBeanValidationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    Validator validator;

    Order order = new Order();
    Errors errors = new BeanPropertyBindingResult(order, "order");


    @Test
    public void clientCanNotBeNull() {
        validator.validate(order, errors);
        assertEquals("NotNull", errors.getFieldError("client").getCode());
    }

    @Test
    public void validClientTest() {
        order.setClient(new Client());
        validator.validate(order, errors);
        assertNull(errors.getFieldError("client"));
    }

    @Test
    public void spCanNotBeNull() {
        validator.validate(order, errors);
        assertEquals("NotNull", errors.getFieldError("sp").getCode());
    }

    @Test
    public void validSpTest() {
        order.setSp(new Sp());
        validator.validate(order, errors);
        assertNull(errors.getFieldError("sp"));
    }

    @Test
    public void noteCanBeNull() {
        validator.validate(order, errors);
        assertNull(errors.getFieldError("note"));
    }

    @Test
    public void noteCanNotBeLongerThan500Characters() {
        order.setNote(StringUtils.leftPad("", 501, "a"));
        validator.validate(order, errors);
        assertEquals("Size", errors.getFieldError("note").getCode());
    }

    @Test
    public void noteCanBe500CharactersLong() {
        order.setNote(StringUtils.leftPad("", 500, "a"));
        validator.validate(order, errors);
        assertNull(errors.getFieldError("note"));
    }

    @Test
    public void dateOrderedCanNotBeNull() {
        order.setDateOrdered(null);
        validator.validate(order, errors);
        assertEquals("NotNull", errors.getFieldError("dateOrdered").getCode());
    }

    @Test
    public void dateOrderedCanNotBeFuture() {
        order.setDateOrdered(DateService.getShiftedDate(10));
        validator.validate(order, errors);
        assertEquals("Past", errors.getFieldError("dateOrdered").getCode());
    }

    @Test
    public void dateOrderedShouldBePast() {
        order.setDateOrdered(DateService.getShiftedDate(-1));
        validator.validate(order, errors);
        assertNull(errors.getFieldError("dateOrdered"));
    }

    @Test
    public void statusCanNotBeNull() {
        order.setStatus(null);
        validator.validate(order, errors);
        assertEquals("NotNull", errors.getFieldError("status").getCode());
    }

    @Test
    public void validStatusTest() {
        order.setStatus(OrderStatus.PACKING);
        validator.validate(order, errors);
        assertNull(errors.getFieldError("status"));
    }

    @Test
    public void prepaidCanNotBeNull() throws NoSuchFieldException, IllegalAccessException {
        Field field = Order.class.getDeclaredField("prepaid");
        field.setAccessible(true);
        field.set(order, null);
        validator.validate(order, errors);
        assertEquals("NotNull", errors.getFieldError("prepaid").getCode());
    }

    @Test
    public void prepaidCanNotBeNegative() {
        order.setPrepaid(new BigDecimal(-1));
        validator.validate(order, errors);
        assertEquals("Min", errors.getFieldError("prepaid").getCode());
    }

    @Test
    public void validPrepaidValueTest() {
        order.setPrepaid(new BigDecimal(0));
        validator.validate(order, errors);
        assertNull(errors.getFieldError("prepaid"));
    }

    @Test
    public void placeCanBeNull() {
        order.setPlace(null);
        validator.validate(order, errors);
        assertNull(errors.getFieldError("place"));
    }

    @Test
    public void dateCompletedCanBeNull() {
        order.setDateCompleted(null);
        validator.validate(order, errors);
        assertNull(errors.getFieldError("dateCompleted"));
    }

    @Test
    public void dateCompletedCanNotBeFuture() {
        order.setDateOrdered(DateService.getShiftedDate(10));
        validator.validate(order, errors);
        assertEquals("Past", errors.getFieldError("dateOrdered").getCode());
    }

    @Test
    public void dateCompletedShouldBePast() {
        order.setDateOrdered(DateService.getShiftedDate(-1));
        validator.validate(order, errors);
        assertNull(errors.getFieldError("dateOrdered"));
    }

}