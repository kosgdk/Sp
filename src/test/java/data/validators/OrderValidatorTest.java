package data.validators;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.validation.Errors;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.OrderService;
import sp.data.validators.OrderValidator;

import javax.annotation.Resource;

import static org.mockito.Mockito.*;


@RunWith(ZohhakRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class OrderValidatorTest extends AbstractJUnit4SpringContextTests {

    // Spring context initialization
    private TestContextManager testContextManager;

    @Mock
    OrderService service;

    @Mock
    Order changedOrder;

    @Mock
    Order persistedOrder;

    @Mock
    Sp changedOrderSp;

    @Mock
    Sp persistedOrderSp;

    @Mock
    Errors errors;

    @InjectMocks
    @Resource(name = "OrderValidator")
    OrderValidator validator;

    @Before
    public void setUp() throws Exception {
        // Initialize Spring context
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);

        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Prepare mocks behavior
        when(service.getById(anyLong())).thenReturn(persistedOrder);
        when(changedOrder.getSp()).thenReturn(changedOrderSp);
        when(persistedOrder.getSp()).thenReturn(persistedOrderSp);
        when(persistedOrderSp.getId()).thenReturn(1L);
    }


    @Test
    public void validateChangedSp_SpNotChanged_ShouldNotCauseError() {
        when(changedOrderSp.getId()).thenReturn(1L);
        validator.validate(changedOrder, errors);
        verify(errors, never()).rejectValue("sp", "order.sp.notAllowed");
    }

    @TestWith({"COLLECTING", "CHECKOUT"})
    public void validateChangedSp_AllowedSpStatuses_ShouldNotCauseError(SpStatus spStatus) {
        when(changedOrderSp.getId()).thenReturn(2L);
        when(changedOrderSp.getStatus()).thenReturn(spStatus);
        validator.validate(changedOrder, errors);
        verify(errors, never()).rejectValue("sp", "order.sp.notAllowed");
    }

    @TestWith({"PACKING", "PAID", "SENT", "ARRIVED", "DISTRIBUTING", "COMPLETED"})
    public void validateChangedSp_DisallowedSpStatuses_ShouldCauseError(SpStatus spStatus) {
        when(changedOrderSp.getId()).thenReturn(2L);
        when(changedOrderSp.getStatus()).thenReturn(spStatus);
        validator.validate(changedOrder, errors);
        verify(errors, times(1)).rejectValue("sp", "order.sp.notAllowed");
    }

}