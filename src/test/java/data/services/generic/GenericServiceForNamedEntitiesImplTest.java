package data.services.generic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sp.data.dao.generic.GenericDaoForNamedEntities;
import sp.data.entities.Client;
import sp.data.services.generic.GenericServiceForNamedEntitiesImpl;

import javax.annotation.Resource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class GenericServiceForNamedEntitiesImplTest extends AbstractJUnit4SpringContextTests {

    @Mock
    GenericDaoForNamedEntities<Client, Long> dao;

    @InjectMocks
    @Resource(name = "ClientService")
    GenericServiceForNamedEntitiesImpl<Client, Long> service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void searchByName_ShouldCallCorrespondingMethodInDao() {
        service.searchByName("TestName");
        verify(dao, times(1)).searchByName("TestName");
    }

    @Test
    public void getByName_ShouldCallCorrespondingMethodInDao() {
        service.getByName("TestName");
        verify(dao, times(1)).getByName("TestName");
    }

}