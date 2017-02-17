package data.converters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import sp.data.converters.IdToOrderPositionConverter;
import sp.data.services.interfaces.OrderPositionService;

import javax.annotation.Resource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class IdToOrderPositionConverterTest extends AbstractJUnit4SpringContextTests {

    @Mock
    OrderPositionService service;

    @InjectMocks
    @Resource
    IdToOrderPositionConverter converter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void convert_ShouldConvertIdOfStringToOrderPosition() {
        converter.convert("15");
        verify(service, times(1)).getById(15L);

    }
}