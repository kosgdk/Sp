package data.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sp.data.dao.interfaces.OrderPositionDao;
import sp.data.services.interfaces.OrderPositionService;

import javax.annotation.Resource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class OrderPositionServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Mock
    OrderPositionDao orderPositionDao;

    @InjectMocks
    @Resource(name = "OrderPositionService")
    OrderPositionService orderPositionService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getZeroWeightOrderPositions_shouldCallCorrespondingMethodInDao() throws Exception {
        orderPositionService.getZeroWeightOrderPositions(1L);
        verify(orderPositionDao, times(1)).getZeroWeightOrderPositions(1L);
    }
}