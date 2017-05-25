package data.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.validation.Errors;
import sp.data.entities.Order;
import sp.data.entities.Product;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.OrderService;
import sp.data.validators.OrderValidator;
import testservices.FaultTolerantTheoriesRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class OrderValidatorTest extends AbstractJUnit4SpringContextTests {

    @Mock OrderService service;
    @Mock Order changedOrder;
    @Mock Order persistedOrder;
    @Mock Sp changedOrderSp;
    @Mock Sp persistedOrderSp;
    @Mock Errors errors;

    @InjectMocks
    @Resource(name = "OrderValidator")
    OrderValidator validator;

    private static void printTestParameters(Object...parameters){
        System.out.println(" - Parameters: " + Arrays.toString(parameters));
    }

    @Before
    public void setUp() throws Exception {
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);

        MockitoAnnotations.initMocks(this);

        when(service.getById(anyLong())).thenReturn(persistedOrder);
        when(persistedOrder.getSp()).thenReturn(persistedOrderSp);
        when(persistedOrderSp.getId()).thenReturn(1L);
        when(changedOrder.getSp()).thenReturn(changedOrderSp);
    }

    @DataPoints
    public static SpStatus[] spStatuses() {
        return SpStatus.values();
    }

    @RunWith(FaultTolerantTheoriesRunner.class)
    public static class ValidateSpTests extends OrderValidatorTest{

        @Test
        public void validateNewSp_SpDoesNotChanged_ShouldNotCauseError() {
            when(changedOrderSp.getId()).thenReturn(1L);
            validator.validateSp(changedOrder, errors);
            verify(errors, never()).rejectValue("sp", "order.sp.notAllowed");
        }

        @Theory
        public void validateNewSp_NewSpHasSuitableStatus_ShouldNotCauseError(SpStatus spStatus) {
            assumeTrue(spStatus == SpStatus.COLLECTING || spStatus == SpStatus.CHECKOUT);
            printTestParameters(spStatus);

            when(changedOrderSp.getId()).thenReturn(2L);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateSp(changedOrder, errors);

            verify(errors, never()).rejectValue("sp", "order.sp.notAllowed");
        }

        @Theory
        public void validateSp_NewSpHasUnsuitableStatus_ShouldCauseError(SpStatus spStatus) {
            assumeFalse(spStatus == SpStatus.COLLECTING || spStatus == SpStatus.CHECKOUT);
            printTestParameters(spStatus);

            when(changedOrderSp.getId()).thenReturn(2L);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateSp(changedOrder, errors);

            verify(errors, times(1)).rejectValue("sp", "order.sp.notAllowed");
        }

    }

    @RunWith(FaultTolerantTheoriesRunner.class)
    public static class ValidateStatusTests extends OrderValidatorTest{

        @Theory
        public void validateStatus_OrderStatusUnpaid_SpHasSuitableStatus_ShouldNotCauseError(SpStatus spStatus) {
            assumeTrue(spStatus == SpStatus.COLLECTING || spStatus == SpStatus.CHECKOUT);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.UNPAID);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, never()).rejectValue("status", "order.status.incompatibleSpStatusForUnpaidOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusUnpaid_SpHasUnsuitableStatus_ShouldCauseError(SpStatus spStatus) {
            assumeFalse(spStatus == SpStatus.COLLECTING || spStatus == SpStatus.CHECKOUT);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.UNPAID);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, times(1)).rejectValue("status", "order.status.incompatibleSpStatusForUnpaidOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusPaid_SpHasSuitableStatus_ShouldNotCauseError(SpStatus spStatus) {
            assumeTrue(spStatus == SpStatus.CHECKOUT);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.PAID);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, never()).rejectValue("status", "order.status.incompatibleSpStatusForPaidOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusPaid_SpHasUnsuitableStatus_ShouldCauseError(SpStatus spStatus) {
            assumeFalse(spStatus == SpStatus.COLLECTING || spStatus == SpStatus.CHECKOUT);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.PAID);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, times(1)).rejectValue("status", "order.status.incompatibleSpStatusForPaidOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusPacking_SpHasSuitableStatus_ShouldNotCauseError(SpStatus spStatus) {
            assumeTrue(spStatus == SpStatus.PACKING);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.PACKING);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, never()).rejectValue("status", "order.status.incompatibleSpStatusForPackingOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusPacking_SpHasUnsuitableStatus_ShouldCauseError(SpStatus spStatus) {
            assumeFalse(spStatus == SpStatus.PACKING);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.PACKING);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, times(1)).rejectValue("status", "order.status.incompatibleSpStatusForPackingOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusSent_SpHasSuitableStatus_ShouldNotCauseError(SpStatus spStatus) {
            assumeTrue(spStatus == SpStatus.SENT);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.SENT);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, never()).rejectValue("status", "order.status.incompatibleSpStatusForSentOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusSent_SpHasUnsuitableStatus_ShouldCauseError(SpStatus spStatus) {
            assumeFalse(spStatus == SpStatus.SENT);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.SENT);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, times(1)).rejectValue("status", "order.status.incompatibleSpStatusForSentOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusArrived_SpHasSuitableStatus_ShouldNotCauseError(SpStatus spStatus) {
            assumeTrue(spStatus == SpStatus.ARRIVED);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.ARRIVED);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, never()).rejectValue("status", "order.status.incompatibleSpStatusForArrivedOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusArrived_SpHasUnsuitableStatus_ShouldCauseError(SpStatus spStatus) {
            assumeFalse(spStatus == SpStatus.ARRIVED);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.ARRIVED);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, times(1)).rejectValue("status", "order.status.incompatibleSpStatusForArrivedOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusCompleted_SpHasSuitableStatus_ShouldNotCauseError(SpStatus spStatus) {
            assumeTrue(spStatus == SpStatus.DISTRIBUTING || spStatus == SpStatus.COMPLETED);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.COMPLETED);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, never()).rejectValue("status", "order.status.incompatibleSpStatusForCompletedOrderStatus");
        }

        @Theory
        public void validateStatus_OrderStatusCompleted_SpHasUnsuitableStatus_ShouldCauseError(SpStatus spStatus) {
            assumeFalse(spStatus == SpStatus.DISTRIBUTING || spStatus == SpStatus.COMPLETED);
            printTestParameters(spStatus);

            when(changedOrder.getStatus()).thenReturn(OrderStatus.COMPLETED);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validateStatus(changedOrder, errors);

            verify(errors, times(1)).rejectValue("status", "order.status.incompatibleSpStatusForCompletedOrderStatus");
        }

    }

    @RunWith(FaultTolerantTheoriesRunner.class)
    public static class validatePrepaidTests extends OrderValidatorTest{

        @Test
        public void validatePrepaid_PrepaidEquals0_ShouldNotCauseError() {
            when(changedOrder.getPrepaid()).thenReturn(BigDecimal.ZERO);

            validator.validatePrepaid(changedOrder, errors);

            verify(errors, never()).rejectValue("prepaid", "order.status.incompatibleSpStatusForSettingPrepaidField");
        }

        @Theory
        public void validatePrepaid_PrepaidIsGreaterThan0_SpHasUnsuitableStatus_ShouldCauseError(SpStatus spStatus) {
            when(changedOrder.getPrepaid()).thenReturn(BigDecimal.ONE);
            assumeTrue(spStatus == SpStatus.CHECKOUT);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validatePrepaid(changedOrder, errors);

            verify(errors, never()).rejectValue("prepaid", "order.status.incompatibleSpStatusForSettingPrepaidField");
        }

        @Theory
        public void validatePrepaid_PrepaidIsGreaterThan0_SpHasSuitableStatus_ShouldNotCauseError(SpStatus spStatus) {
            when(changedOrder.getPrepaid()).thenReturn(BigDecimal.ONE);
            assumeFalse(spStatus == SpStatus.CHECKOUT);
            when(changedOrderSp.getStatus()).thenReturn(spStatus);

            validator.validatePrepaid(changedOrder, errors);

            verify(errors, times(1)).rejectValue("prepaid", "order.status.incompatibleSpStatusForSettingPrepaidField");
        }

    }

    @RunWith(FaultTolerantTheoriesRunner.class)
    public static class OtherTests extends OrderValidatorTest{

        @Autowired
        OrderValidator orderValidator;

        @Test
        public void validate_ShouldCallValidationMethods(){
            OrderValidator orderValidator = spy(OrderValidator.class);
            doNothing().when(orderValidator).validateSp(changedOrder, errors);
            doNothing().when(orderValidator).validatePrepaid(changedOrder, errors);
            doNothing().when(orderValidator).validateStatus(changedOrder, errors);

            orderValidator.validate(changedOrder, errors);

            verify(orderValidator, times(1)).validateSp(changedOrder, errors);
            verify(orderValidator, times(1)).validatePrepaid(changedOrder, errors);
            verify(orderValidator, times(1)).validateStatus(changedOrder, errors);
        }

        @Test
        public void supports_UnsupportedClass_ShouldReturnFalse() throws Exception {
            assertFalse(orderValidator.supports(Product.class));
        }

        @Test
        public void supports_null_ShouldReturnFalse() throws Exception {
            assertFalse(orderValidator.supports(null));
        }

        @Test
        public void supports_SupportedClass_ShouldReturnTrue() throws Exception {
            assertTrue(orderValidator.supports(Order.class));
        }
    }
}