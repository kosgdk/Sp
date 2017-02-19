package data.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.Errors;
import sp.data.entities.Client;
import sp.data.services.interfaces.ClientService;
import sp.data.validators.ClientValidator;

import javax.annotation.Resource;

import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class ClientValidatorTest extends AbstractJUnit4SpringContextTests {

    @Mock
    ClientService service;

    @Mock
    Client client;

    @Mock
    Errors errors;

    @InjectMocks
    @Resource(name = "ClientValidator")
    ClientValidator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void validate_InputUniqueName_ShouldNotCauseErrors() {
        when(client.getName()).thenReturn("UniqueName");
        when(service.getByName("UniqueName")).thenReturn(null);
        validator.validate(client, errors);
        verify(errors, never()).rejectValue("name", "client.name.notUnique");
    }

    @Test
    public void validate_InputNonUniqueName_ShouldCauseErrors() {
        when(client.getName()).thenReturn("NonUniqueName");
        when(service.getByName("NonUniqueName")).thenReturn(new Client());
        validator.validate(client, errors);
        verify(errors, times(1)).rejectValue("name", "client.name.notUnique");
    }

}