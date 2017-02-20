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
import sp.data.dao.interfaces.ClientDao;
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Client;
import sp.data.entities.Order;
import sp.data.services.interfaces.ClientService;
import sp.data.services.interfaces.OrderService;

import javax.annotation.Resource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class OrderServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Mock
    OrderDao dao;

    @Mock
    Order entity;

    @InjectMocks
    @Resource
    OrderService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getByIdWithAllChildren_ShouldCallCorrespondingMethodInDao(){
        service.getByIdWithAllChildren(1L);
        verify(dao, times(1)).getByIdWithAllChildren(1L);
    }

   

}