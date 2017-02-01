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
import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;

import java.math.BigDecimal;
import java.util.Date;

import static data.entities.services.DateService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class SpBeanValidationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    Validator validator;

    Sp sp = new Sp();
    Errors errors = new BeanPropertyBindingResult(sp, "sp");


    @Test
    public void numberCanNotBeNull() {
        sp.setNumber(null);
        validator.validate(sp, errors);
        assertEquals("NotNull", errors.getFieldError("number").getCode());
    }

    @Test
    public void numberCanNotBeLessThan1() {
        sp.setNumber(0L);
        validator.validate(sp, errors);
        assertEquals("Min", errors.getFieldError("number").getCode());
    }

    @Test
    public void numberCanBe1() {
        sp.setNumber(1L);
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("number"));
    }

    @Test
    public void percentCanNotBeNull() {
        sp.setPercent(null);
        validator.validate(sp, errors);
        assertEquals("NotNull", errors.getFieldError("percent").getCode());
    }

    @Test
    public void percentCanNotBeNegative() {
        sp.setPercent(new BigDecimal(-0.1));
        validator.validate(sp, errors);
        assertEquals("DecimalMin", errors.getFieldError("percent").getCode());
    }

    @Test
    public void percentCanNotBeMoreThan1() {
        sp.setPercent(new BigDecimal(1.1));
        validator.validate(sp, errors);
        assertEquals("DecimalMax", errors.getFieldError("percent").getCode());
    }

    @Test
    public void validPercentValueTest() {
        sp.setPercent(new BigDecimal(0.5));
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("percent"));
    }

    @Test
    public void statusCanNotBeNull() {
        sp.setStatus(null);
        validator.validate(sp, errors);
        assertEquals("NotNull", errors.getFieldError("status").getCode());
    }

    @Test
    public void validStatusTest() {
        sp.setStatus(SpStatus.COLLECTING);
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("status"));
    }

    @Test
    public void dateStartCanNotBeNull() {
        sp.setDateStart(null);
        validator.validate(sp, errors);
        assertEquals("NotNull", errors.getFieldError("dateStart").getCode());
    }

    @Test
    public void dateStartValidValueTest() {
        sp.setDateStart(new Date());
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateStart"));
    }

    @Test
    public void dateEndCanNotBeNull() {
        sp.setDateEnd(null);
        validator.validate(sp, errors);
        assertEquals("NotNull", errors.getFieldError("dateEnd").getCode());
    }

    @Test
    public void dateEndValidValueTest() {
        sp.setDateEnd(new Date());
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateEnd"));
    }

    @Test
    public void dateToPayCanNotBeNull() {
        sp.setDateToPay(null);
        validator.validate(sp, errors);
        assertEquals("NotNull", errors.getFieldError("dateToPay").getCode());
    }

    @Test
    public void dateToPayValidValueTest() {
        sp.setDateToPay(new Date());
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateToPay"));
    }

    @Test
    public void dateSentCanBeNull() {
        sp.setDateSent(null);
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateSent"));
    }

    @Test
    public void dateSentCanNotBeFuture() {
        sp.setDateSent(getShiftedDate(10));
        validator.validate(sp, errors);
        assertEquals("Past", errors.getFieldError("dateSent").getCode());
    }
    
    @Test
    public void dateSentValidValueTest() {
        sp.setDateSent(getShiftedDate(-1));
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateSent"));
    }

    @Test
    public void dateToReceiveCanBeNull() {
        sp.setDateToReceive(null);
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateToReceive"));
    }

    @Test
    public void dateToReceiveValidValueTest() {
        sp.setDateToReceive(new Date());
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateToReceive"));
    }

    @Test
    public void dateReceivedCanBeNull() {
        sp.setDateReceived(null);
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateReceived"));
    }

    @Test
    public void dateReceivedCanNotBeFuture() {
        sp.setDateReceived(getShiftedDate(10));
        validator.validate(sp, errors);
        assertEquals("Past", errors.getFieldError("dateReceived").getCode());
    }

    @Test
    public void dateReceivedValidValueTest() {
        sp.setDateReceived(getShiftedDate(-1));
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateReceived"));
    }

    @Test
    public void dateToDistributeCanBeNull() {
        sp.setDateToDistribute(null);
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateToDistribute"));
    }

    @Test
    public void dateToDistributeValidValueTest() {
        sp.setDateToDistribute(new Date());
        validator.validate(sp, errors);
        assertNull(errors.getFieldError("dateToDistribute"));
    }

}