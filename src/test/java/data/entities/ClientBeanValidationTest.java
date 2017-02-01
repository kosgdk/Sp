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
import sp.data.entities.Client;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class ClientBeanValidationTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    Validator validator;

    Client client = new Client();
    Errors errors = new BeanPropertyBindingResult(client, "client");


    @Test
    public void nameCanNotBeNull() {
        validator.validate(client, errors);
        assertEquals("NotNull", errors.getFieldError("name").getCode());
    }

    @Test
    public void nameCanNotBeShorterThan3Characters() {
        client.setName("aa");
        validator.validate(client, errors);
        assertEquals("Size", errors.getFieldError("name").getCode());
    }

    @Test
    public void nameCanNotBeLongerThan50Characters() {
        client.setName(StringUtils.leftPad("", 51, "a"));
        validator.validate(client, errors);
        assertEquals("Size", errors.getFieldError("name").getCode());
    }

    @Test
    public void nameCanBe3CharactersLong() {
        client.setName("aaa");
        validator.validate(client, errors);
        assertNull(errors.getFieldError("name"));
    }

    @Test
    public void nameCanBe50CharactersLong() {
        client.setName(StringUtils.leftPad("", 50, "a"));
        validator.validate(client, errors);
        assertNull(errors.getFieldError("name"));
    }

    @Test
    public void realNameCanBeNull() {
        validator.validate(client, errors);
        assertNull(errors.getFieldError("realName"));
    }

    @Test
    public void realNameCanNotBeShorterThan2Characters() {
        client.setRealName("a");
        validator.validate(client, errors);
        assertEquals("Size", errors.getFieldError("realName").getCode());
    }

    @Test
    public void realNameCanNotBeLongerThan50Characters() {
        client.setRealName(StringUtils.leftPad("", 51, "a"));
        validator.validate(client, errors);
        assertEquals("Size", errors.getFieldError("realName").getCode());
    }

    @Test
    public void realNameCanBe2CharactersLong() {
        client.setName("aa");
        validator.validate(client, errors);
        assertNull(errors.getFieldError("realName"));
    }

    @Test
    public void realNameCanBe50CharactersLong() {
        client.setName(StringUtils.leftPad("", 50, "a"));
        validator.validate(client, errors);
        assertNull(errors.getFieldError("realName"));
    }

    @Test
    public void phoneCanBeNull() {
        client.setPhone(null);
        validator.validate(client, errors);
        assertNull(errors.getFieldError("phone"));
    }

    @Test
    public void phoneCanNotContainCharacters() {
        client.setPhone("+7(978)123-45-6a");
        validator.validate(client, errors);
        assertEquals("Pattern", errors.getFieldError("phone").getCode());
    }

    @Test
    public void phoneCanNotContainMoreThan11Digits() {
        client.setPhone("+7(978)123-45-678");
        validator.validate(client, errors);
        assertEquals("Pattern", errors.getFieldError("phone").getCode());
    }

    @Test
    public void phoneCanNotContainLessThan11Digits() {
        client.setPhone("+7(978)123-45-6");
        validator.validate(client, errors);
        assertEquals("Pattern", errors.getFieldError("phone").getCode());
    }

    @Test
    public void phoneShouldHaveFormat() {
        client.setPhone("79781234567");
        validator.validate(client, errors);
        assertEquals("Pattern", errors.getFieldError("phone").getCode());
    }

    @Test
    public void validPhoneTest() {
        client.setPhone("+7(978)123-45-67");
        validator.validate(client, errors);
        assertNull(errors.getFieldError("phone"));
    }

    @Test
    public void noteCanBeNull() {
        validator.validate(client, errors);
        assertNull(errors.getFieldError("note"));
    }

    @Test
    public void noteCanNotBeLongerThan500Characters() {
        client.setNote(StringUtils.leftPad("", 501, "a"));
        validator.validate(client, errors);
        assertEquals("Size", errors.getFieldError("note").getCode());
    }

    @Test
    public void noteCanBe500CharactersLong() {
        client.setNote(StringUtils.leftPad("", 500, "a"));
        validator.validate(client, errors);
        assertNull(errors.getFieldError("note"));
    }

    @Test
    public void clientReferrerCanNotBeNull() {
        validator.validate(client, errors);
        assertEquals("NotNull", errors.getFieldError("clientReferrer").getCode());
    }

}