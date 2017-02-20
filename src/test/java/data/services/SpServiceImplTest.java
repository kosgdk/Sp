package data.services;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.SpService;

import javax.annotation.Resource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class SpServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Mock
    SpDao dao;

    @Mock
    Sp entity;

    @InjectMocks
    @Resource(name = "SpService")
    SpService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getLastSp_ShouldCallCorrespondingMethodInDao() {
        service.getLastSp();
        verify(dao, times(1)).getLastSp();
    }

    @Test
    public void getByIdWithAllChildren_ShouldCallCorrespondingMethodInDao() {
        service.getByIdWithAllChildren(1L);
        verify(dao, times(1)).getByIdWithAllChildren(1L);
    }

    @Test
    public void getLastNumber_ShouldCallCorrespondingMethodInDao() {
        service.getLastNumber();
        verify(dao, times(1)).getLastNumber();
    }

    @Test
    public void getIdsByStatus_ShouldCallCorrespondingMethodInDao() {
        service.getIdsByStatus(SpStatus.COLLECTING);
        verify(dao, times(1)).getIdsByStatus(SpStatus.COLLECTING);
    }

    @Test
    @Ignore
    public void setOrdersStatusesTest() {
        // TODO: write test for SpService.setOrdersStatuses method
    }

}