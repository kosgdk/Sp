package data.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sp.data.converters.StringToDateConverter;

import java.util.Date;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class StringToDateConverterTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    StringToDateConverter converter;


    @Test
    public void convert_ShouldConvertStringToDate() {
        assertEquals(new Date(1487282400000L), converter.convert("2017-02-17"));
    }

    @Test
    public void convert_InputInvalidValue_ShouldReturnNull() {
        assertEquals(null, converter.convert("2017a-02-50"));
    }

}