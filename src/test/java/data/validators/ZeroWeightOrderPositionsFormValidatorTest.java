package data.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.Errors;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.entities.formscontainers.ZeroWeightOrderPositionsForm;
import sp.data.validators.ZeroWeightOrderPositionsFormValidator;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_ForTests.xml", "classpath:dispatcher-servlet.xml"})
public class ZeroWeightOrderPositionsFormValidatorTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    ZeroWeightOrderPositionsFormValidator validator;

    @Mock ZeroWeightOrderPositionsForm zeroWeightOrderPositionsForm;
    @Mock OrderPosition orderPosition1;
    @Mock OrderPosition orderPosition2;
    @Mock Errors errors;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(zeroWeightOrderPositionsForm.getOrderPositions()).thenReturn(Arrays.asList(orderPosition1, orderPosition2));
        when(orderPosition1.getWeight()).thenReturn(100);
    }

    @Test
    public void validate_OrderPositionHasNegativeWeight_ShouldCauseError(){
        when(orderPosition2.getWeight()).thenReturn(-1);
        validator.validate(zeroWeightOrderPositionsForm, errors);
        verify(errors, times(1)).rejectValue("orderPositions[1].productWeight", "product.weight.incorrect");
    }

    @Test
    public void validate_OrderPositionHasPositiveWeight_ShouldCauseError(){
        when(orderPosition2.getWeight()).thenReturn(50);
        validator.validate(zeroWeightOrderPositionsForm, errors);
        verify(errors, never()).rejectValue("orderPositions[1].productWeight", "product.weight.incorrect");
    }

    @Test
    public void validate_OrderPositionHasZeroWeight_ShouldNotCauseError(){
        when(orderPosition2.getWeight()).thenReturn(0);
        validator.validate(zeroWeightOrderPositionsForm, errors);
        verify(errors, never()).rejectValue("orderPositions[1].productWeight", "product.weight.incorrect");
    }


    @Test
    public void supports_UnsupportedClass_ShouldReturnFalse() throws Exception {
        assertFalse(validator.supports(Order.class));
    }

    @Test
    public void supports_null_ShouldReturnFalse() throws Exception {
        assertFalse(validator.supports(null));
    }

    @Test
    public void supports_SupportedClass_ShouldReturnTrue() throws Exception {
        assertTrue(validator.supports(ZeroWeightOrderPositionsForm.class));
    }

}