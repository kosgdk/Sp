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
import sp.data.dao.generic.GenericDao;
import sp.data.dao.interfaces.ClientDao;
import sp.data.entities.Client;
import sp.data.services.generic.GenericService;
import sp.data.services.interfaces.ClientService;

import javax.annotation.Resource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class ClientServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Mock
    ClientDao dao;

    @Mock
    Client entity;

    @InjectMocks
    @Resource
    ClientService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getByIdWithAllChildren_ShouldCallCorrespondingMethodInDao() {
        service.getByIdWithAllChildren(1L);
        verify(dao, times(1)).getByIdWithAllChildren(1L);
    }



}