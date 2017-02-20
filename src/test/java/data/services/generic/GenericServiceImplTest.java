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
import sp.data.dao.generic.GenericDao;
import sp.data.entities.Client;
import sp.data.entities.Order;
import sp.data.services.generic.GenericService;
import sp.data.services.generic.GenericServiceImpl;

import javax.annotation.Resource;

import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class GenericServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Mock
    GenericDao<Client, Long> dao;

    @Mock
    Client entity;

    @InjectMocks
    @Resource(name = "ClientService")
    GenericService<Client, Long> service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getById_ShouldCallCorrespondingMethodInDao() {
        service.getById(1L);
        verify(dao, times(1)).getById(1L);
    }

    @Test
    public void getAll_ShouldCallCorrespondingMethodInDao() {
        service.getAll();
        verify(dao, times(1)).getAll();
    }

    @Test
    public void save_ShouldCallCorrespondingMethodInDao() {
        service.save(entity);
        verify(dao, times(1)).save(entity);
    }

    @Test
    public void update_ShouldCallCorrespondingMethodInDao() {
        service.update(entity);
        verify(dao, times(1)).update(entity);
    }

    @Test
    public void delete_ShouldCallCorrespondingMethodInDao() {
        service.delete(entity);
        verify(dao, times(1)).delete(entity);
    }

    @Test
    public void deleteById_ShouldCallCorrespondingMethodInDao() {
        service.deleteById(1L);
        verify(dao, times(1)).deleteById(1L);
    }

}