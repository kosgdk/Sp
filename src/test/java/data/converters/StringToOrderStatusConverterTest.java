package data.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sp.data.converters.StringToOrderStatusConverter;
import sp.data.entities.enumerators.OrderStatus;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class StringToOrderStatusConverterTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    StringToOrderStatusConverter converter;


    @Test
    public void convert_InputId_ShouldConvertToOrderStatus() {
        assertEquals(OrderStatus.PAID, converter.convert("2"));
    }

    @Test
    public void convert_InputName_ShouldConvertToOrderStatus() {
        assertEquals(OrderStatus.PAID, converter.convert("Оплачен"));
    }
}