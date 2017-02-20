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
import sp.data.dao.interfaces.PropertiesDao;
import sp.data.entities.Client;
import sp.data.entities.Properties;
import sp.data.services.generic.GenericService;
import sp.data.services.interfaces.PropertiesService;

import javax.annotation.Resource;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class PropertiesServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Mock
    PropertiesDao dao;

    @Mock
    Properties entity;

    @InjectMocks
    @Resource(name = "PropertiesService")
    PropertiesService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getProperties_ShouldCallCorrespondingMethodInDao() {
        service.getProperties();
        verify(dao, times(1)).getProperties();
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

}