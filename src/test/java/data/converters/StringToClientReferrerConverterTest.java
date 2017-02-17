package data.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sp.data.converters.StringToClientReferrerConverter;
import sp.data.entities.enumerators.ClientReferrer;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class StringToClientReferrerConverterTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    StringToClientReferrerConverter converter;


    @Test
    public void convert_InputId_ShouldConvertToClientReferrer() {
        assertEquals(ClientReferrer.VK, converter.convert("2"));
    }

    @Test
    public void convert_InputName_ShouldConvertToClientReferrer() {
        assertEquals(ClientReferrer.VK, converter.convert("Вконтакте"));
    }
}