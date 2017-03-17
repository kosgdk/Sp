package data.validators;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theory;
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
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.validators.SpValidator;
import testservices.FaultTolerantTheoriesRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;


@RunWith(FaultTolerantTheoriesRunner.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class SpValidatorTest extends AbstractJUnit4SpringContextTests {

    @Mock
    Sp sp;

    @Mock
    Order order1;

    @Mock
    Order order2;

    @Mock
    Errors errors;

    @InjectMocks
    @Resource(name = "SpValidator")
    SpValidator spValidator;

    @Before
    public void setUp() throws Exception {
        // Initialize Spring context
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);

        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Prepare mocks behavior
        when(sp.getOrders()).thenReturn(new HashSet<>(Arrays.asList(order1, order2)));
    }

    private static void printTestParameters(Object...parameters){
        System.out.println(" - Parameters: " + Arrays.toString(parameters));
    }

    @DataPoints
    public static OrderStatus[] orderStatuses() {
        return OrderStatus.values();
    }

    @DataPoints
    public static SpStatus[] spStatuses() {
        return SpStatus.values();
    }


    @Test
    public void validateSpStatus_SpStatusIsCollecting_OrdersSetIsNull_ShouldNotCauseError() {
        when(sp.getStatus()).thenReturn(SpStatus.COLLECTING);
        when(sp.getOrders()).thenReturn(null);

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue(eq("status"), anyString());
    }

    @Test
    public void validateSpStatus_SpStatusIsCollecting_OrdersSetIsEmpty_ShouldNotCauseError() {
        when(sp.getStatus()).thenReturn(SpStatus.COLLECTING);
        when(sp.getOrders()).thenReturn(new HashSet<>());

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue(eq("status"), anyString());
    }

    @Theory
    public void validateSpStatus_SpHasUnsuitableStatus_OrdersSetIsNull_ShouldCauseError(SpStatus spStatus) {
        assumeTrue(spStatus != SpStatus.COLLECTING);
        printTestParameters(spStatus);

        when(sp.getStatus()).thenReturn(spStatus);
        when(sp.getOrders()).thenReturn(null);

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.noOrders");
    }

    @Theory
    public void validateSpStatus_SpHasUnsuitableStatus_OrdersSetIsEmpty_ShouldCauseError(SpStatus spStatus) {
        assumeTrue(spStatus != SpStatus.COLLECTING);
        printTestParameters(spStatus);

        when(sp.getStatus()).thenReturn(spStatus);
        when(sp.getOrders()).thenReturn(new HashSet<>());

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.noOrders");
    }



    @Theory
    public void validateSpStatus_SpStatusIsCollecting_OrdersHasSuitableStatuses_ShouldNotCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define suitable OrderStatuses
        assumeTrue( (orderStatus1 == OrderStatus.UNPAID || orderStatus1 == OrderStatus.PAID) &&
                    (orderStatus2 == OrderStatus.UNPAID || orderStatus2 == OrderStatus.PAID));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.COLLECTING);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue("status", "sp.status.incompatibleOrdersStatusesForCollectingSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsCollecting_OrdersHasUnsuitableStatuses_ShouldCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define unsuitable OrderStatuses
        assumeFalse((orderStatus1 == OrderStatus.UNPAID || orderStatus1 == OrderStatus.PAID) &&
                    (orderStatus2 == OrderStatus.UNPAID || orderStatus2 == OrderStatus.PAID));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.COLLECTING);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.incompatibleOrdersStatusesForCollectingSpStatus");
    }


    @Theory
    public void validateSpStatus_SpStatusIsCheckout_OrdersHasSuitableStatuses_ShouldNotCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define suitable OrderStatuses
        assumeTrue( (orderStatus1 == OrderStatus.UNPAID || orderStatus1 == OrderStatus.PAID) &&
                    (orderStatus2 == OrderStatus.UNPAID || orderStatus2 == OrderStatus.PAID));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.CHECKOUT);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue("status", "sp.status.incompatibleOrdersStatusesForCheckoutSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsCheckout_OrdersHasUnsuitableStatuses_ShouldCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define unsuitable OrderStatuses
        assumeFalse((orderStatus1 == OrderStatus.UNPAID || orderStatus1 == OrderStatus.PAID) &&
                    (orderStatus2 == OrderStatus.UNPAID || orderStatus2 == OrderStatus.PAID));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.CHECKOUT);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.incompatibleOrdersStatusesForCheckoutSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsPacking_OrdersHasSuitableStatuses_ShouldNotCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define suitable OrderStatuses
        assumeTrue( (orderStatus1 == OrderStatus.PAID || orderStatus1 == OrderStatus.PACKING) &&
                    (orderStatus2 == OrderStatus.PAID || orderStatus2 == OrderStatus.PACKING));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.PACKING);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue("status", "sp.status.incompatibleOrdersStatusesForPackingSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsPacking_OrdersHasUnsuitableStatuses_ShouldCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define unsuitable OrderStatuses
        assumeFalse((orderStatus1 == OrderStatus.PAID || orderStatus1 == OrderStatus.PACKING) &&
                    (orderStatus2 == OrderStatus.PAID || orderStatus2 == OrderStatus.PACKING));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.PACKING);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.incompatibleOrdersStatusesForPackingSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsPaid_OrdersHasSuitableStatuses_ShouldNotCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define suitable OrderStatuses
        assumeTrue((orderStatus1 == OrderStatus.PACKING && orderStatus2 == OrderStatus.PACKING));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.PAID);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue("status", "sp.status.incompatibleOrdersStatusesForPaidSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsPaid_OrdersHasUnsuitableStatuses_ShouldCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define unsuitable OrderStatuses
        assumeFalse((orderStatus1 == OrderStatus.PACKING && orderStatus2 == OrderStatus.PACKING));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.PAID);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.incompatibleOrdersStatusesForPaidSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsSent_OrdersHasSuitableStatuses_ShouldNotCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define suitable OrderStatuses
        assumeTrue( (orderStatus1 == OrderStatus.PACKING || orderStatus1 == OrderStatus.SENT) &&
                    (orderStatus2 == OrderStatus.PACKING || orderStatus2 == OrderStatus.SENT));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.SENT);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue("status", "sp.status.incompatibleOrdersStatusesForSentSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsSent_OrdersHasUnsuitableStatuses_ShouldCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define unsuitable OrderStatuses
        assumeFalse((orderStatus1 == OrderStatus.PACKING || orderStatus1 == OrderStatus.SENT) &&
                    (orderStatus2 == OrderStatus.PACKING || orderStatus2 == OrderStatus.SENT));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.SENT);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.incompatibleOrdersStatusesForSentSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsSent_OrdersHasSuitableArrived_ShouldNotCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define suitable OrderStatuses
        assumeTrue( (orderStatus1 == OrderStatus.SENT || orderStatus1 == OrderStatus.ARRIVED) &&
                    (orderStatus2 == OrderStatus.SENT || orderStatus2 == OrderStatus.ARRIVED));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.ARRIVED);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue("status", "sp.status.incompatibleOrdersStatusesForArrivedSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsSent_OrdersHasUnsuitableArrived_ShouldCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define unsuitable OrderStatuses
        assumeFalse((orderStatus1 == OrderStatus.SENT || orderStatus1 == OrderStatus.ARRIVED) &&
                    (orderStatus2 == OrderStatus.SENT || orderStatus2 == OrderStatus.ARRIVED));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.ARRIVED);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.incompatibleOrdersStatusesForArrivedSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsDistributing_OrdersHasSuitableStatuses_ShouldNotCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define suitable OrderStatuses
        assumeTrue((orderStatus1 == OrderStatus.ARRIVED && orderStatus2 == OrderStatus.ARRIVED));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.DISTRIBUTING);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue("status", "sp.status.incompatibleOrdersStatusesForDistributingSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsDistributing_OrdersHasUnsuitableStatuses_ShouldCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define unsuitable OrderStatuses
        assumeFalse((orderStatus1 == OrderStatus.ARRIVED && orderStatus2 == OrderStatus.ARRIVED));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.DISTRIBUTING);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.incompatibleOrdersStatusesForDistributingSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsCompleted_OrdersHasSuitableStatuses_ShouldNotCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define suitable OrderStatuses
        assumeTrue((orderStatus1 == OrderStatus.COMPLETED && orderStatus2 == OrderStatus.COMPLETED));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.COMPLETED);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, never()).rejectValue("status", "sp.status.incompatibleOrdersStatusesForCompletedSpStatus");
    }

    @Theory
    public void validateSpStatus_SpStatusIsCompleted_OrdersHasUnsuitableStatuses_ShouldCauseError (OrderStatus orderStatus1, OrderStatus orderStatus2) {
        // Define unsuitable OrderStatuses
        assumeFalse((orderStatus1 == OrderStatus.COMPLETED && orderStatus2 == OrderStatus.COMPLETED));
        printTestParameters(orderStatus1, orderStatus2);

        when(sp.getStatus()).thenReturn(SpStatus.COMPLETED);
        when(order1.getStatus()).thenReturn(orderStatus1);
        when(order2.getStatus()).thenReturn(orderStatus2);

        spValidator.validate(sp, errors);

        verify(errors, times(1)).rejectValue("status", "sp.status.incompatibleOrdersStatusesForCompletedSpStatus");
    }

}