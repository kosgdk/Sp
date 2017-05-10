package data.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.OrderPositionService;
import sp.data.services.interfaces.OrderService;
import sp.data.services.interfaces.SpService;
import testservices.FaultTolerantTheoriesRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;


@RunWith(Enclosed.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class SpServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Mock
    Sp sp;

    @InjectMocks
    @Resource(name = "SpService")
    SpService spService;

    @Before
    public void setUp() throws Exception {
        // Initialize Spring context
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);

        MockitoAnnotations.initMocks(this);
    }


    @RunWith(BlockJUnit4ClassRunner.class)
    public static class OtherTests extends SpServiceImplTest{

        @Mock
        SpDao spDao;

        @Before
        public void setUp() throws Exception {
            super.setUp();
        }


        @Test
        public void getLastSp_ShouldCallCorrespondingMethodInDao() {
            spService.getLastSp();
            verify(spDao, times(1)).getLastSp();
        }

        @Test
        public void getByIdWithAllChildren_ShouldCallCorrespondingMethodInDao() {
            spService.getByIdWithAllChildren(1L);
            verify(spDao, times(1)).getByIdWithAllChildren(1L);
        }

        @Test
        public void getLastNumber_ShouldCallCorrespondingMethodInDao() {
            spService.getLastNumber();
            verify(spDao, times(1)).getLastNumber();
        }

        @Test
        public void getIdsByStatus_ShouldCallCorrespondingMethodInDao() {
            spService.getIdsByStatus(SpStatus.COLLECTING);
            verify(spDao, times(1)).getIdsByStatus(SpStatus.COLLECTING);
        }

    }

    @RunWith(BlockJUnit4ClassRunner.class)
    public static class CalculateDeliveryPriceForOrdersTests extends SpServiceImplTest{

        @Mock
        Order order1;

        @Mock
        Order order2;

        @Mock
        OrderPositionService orderPositionService
                ;

        @Override
        public void setUp() throws Exception {
            super.setUp();

            when(sp.getDeliveryPrice()).thenReturn(new BigDecimal(490));
            when(sp.getOrders()).thenReturn(new HashSet<>(Arrays.asList(order1, order2)));
            when(order1.getWeight()).thenReturn(42);
            when(order2.getWeight()).thenReturn(1075);
        }

        @Test
        public void calculateDeliveryPriceForOrders_SpHasOrderPositionsWithZeroWeight_ShouldNotCalculateDeliveryPrice() {
            when(orderPositionService.getZeroWeightOrderPositions(anyLong())).thenReturn(new ArrayList<>(Collections.singletonList(mock(OrderPosition.class))));

            spService.calculateDeliveryPriceForOrders(sp);

            verifyZeroInteractions(order1);
            verifyZeroInteractions(order2);
        }

        @Test
        public void calculateDeliveryPriceForOrders_SpHasNoOrderPositionsWithZeroWeight_ShouldCalculateDeliveryPrice() {
            when(orderPositionService.getZeroWeightOrderPositions(anyLong())).thenReturn(new ArrayList<>());

            spService.calculateDeliveryPriceForOrders(sp);

            verify(order1).setDeliveryPrice(new BigDecimal(19));
            verify(order2).setDeliveryPrice(new BigDecimal(472));

        }

    }

    @RunWith(FaultTolerantTheoriesRunner.class)
    public static class ProcessOrdersStatusesTests extends SpServiceImplTest{

        @Mock
        Order order1;

        @Mock
        Order order2;

        @Mock
        OrderService orderService;

        @Before
        public void setUp() throws Exception {
            super.setUp();
            when(sp.getOrders()).thenReturn(new HashSet<>(Arrays.asList(order1, order2)));
        }

        private static void printTestParameters(Object...parameters){
            System.out.println(" - Parameters: " + Arrays.toString(parameters));
        }

        @DataPoints
        public static OrderStatus[] orderStatuses() {
            return OrderStatus.values();
        }

        @Theory
        public void processOrdersStatuses_SpStatusPacking_OrdersHasSuitableStatuses_ShouldUpdateStatuses(OrderStatus orderStatus1, OrderStatus orderStatus2) {
            assumeTrue(orderStatus1 == OrderStatus.PAID && orderStatus2 == OrderStatus.PAID);
            printTestParameters(orderStatus1, orderStatus2);

            when(order1.getStatus()).thenReturn(orderStatus1);
            when(order2.getStatus()).thenReturn(orderStatus2);
            when(sp.getStatus()).thenReturn(SpStatus.PACKING);

            spService.processOrdersStatuses(sp);

            verify(orderService, times(1)).updateStatuses(sp, OrderStatus.PACKING);
        }

        @Theory
        public void processOrdersStatuses_SpStatusPacking_OrdersHasUnsuitableStatuses_ShouldNotUpdateStatuses(OrderStatus orderStatus1, OrderStatus orderStatus2) {
            assumeFalse(orderStatus1 == OrderStatus.PAID && orderStatus2 == OrderStatus.PAID);
            printTestParameters(orderStatus1, orderStatus2);

            when(order1.getStatus()).thenReturn(orderStatus1);
            when(order2.getStatus()).thenReturn(orderStatus2);
            when(sp.getStatus()).thenReturn(SpStatus.PACKING);

            spService.processOrdersStatuses(sp);

            verify(orderService, never()).updateStatuses(eq(sp), any(OrderStatus.class));
        }

        @Theory
        public void processOrdersStatuses_SpStatusSent_OrdersHasSuitableStatuses_ShouldUpdateStatuses(OrderStatus orderStatus1, OrderStatus orderStatus2) {
            assumeTrue(orderStatus1 == OrderStatus.PACKING && orderStatus2 == OrderStatus.PACKING);
            printTestParameters(orderStatus1, orderStatus2);

            when(order1.getStatus()).thenReturn(orderStatus1);
            when(order2.getStatus()).thenReturn(orderStatus2);
            when(sp.getStatus()).thenReturn(SpStatus.SENT);

            spService.processOrdersStatuses(sp);

            verify(orderService, times(1)).updateStatuses(sp, OrderStatus.SENT);
        }

        @Theory
        public void processOrdersStatuses_SpStatusSent_OrdersHasUnsuitableStatuses_ShouldDoNothing(OrderStatus orderStatus1, OrderStatus orderStatus2) {
            assumeFalse(orderStatus1 == OrderStatus.PACKING && orderStatus2 == OrderStatus.PACKING);
            printTestParameters(orderStatus1, orderStatus2);

            when(order1.getStatus()).thenReturn(orderStatus1);
            when(order2.getStatus()).thenReturn(orderStatus2);
            when(sp.getStatus()).thenReturn(SpStatus.SENT);

            spService.processOrdersStatuses(sp);

            verify(orderService, never()).updateStatuses(eq(sp), any(OrderStatus.class));
        }

        @Theory
        public void processOrdersStatuses_SpStatusArrived_OrdersHasSuitableStatuses_ShouldUpdateStatuses(OrderStatus orderStatus1, OrderStatus orderStatus2) {
            assumeTrue(orderStatus1 == OrderStatus.SENT && orderStatus2 == OrderStatus.SENT);
            printTestParameters(orderStatus1, orderStatus2);

            when(order1.getStatus()).thenReturn(orderStatus1);
            when(order2.getStatus()).thenReturn(orderStatus2);
            when(sp.getStatus()).thenReturn(SpStatus.ARRIVED);

            spService.processOrdersStatuses(sp);

            verify(orderService, times(1)).updateStatuses(sp, OrderStatus.ARRIVED);
        }

        @Theory
        public void processOrdersStatuses_SpStatusArrived_OrdersHasUnsuitableStatuses(OrderStatus orderStatus1, OrderStatus orderStatus2) {
            assumeFalse(orderStatus1 == OrderStatus.SENT && orderStatus2 == OrderStatus.SENT);
            printTestParameters(orderStatus1, orderStatus2);

            when(order1.getStatus()).thenReturn(orderStatus1);
            when(order2.getStatus()).thenReturn(orderStatus2);
            when(sp.getStatus()).thenReturn(SpStatus.ARRIVED);

            spService.processOrdersStatuses(sp);

            verify(orderService, never()).updateStatuses(eq(sp), any(OrderStatus.class));
        }

        @Test
        public void processOrdersStatuses_SpOrdersSetIsNull() {
            when(sp.getOrders()).thenReturn(null);

            spService.processOrdersStatuses(sp);

            verifyZeroInteractions(orderService);
        }

        @Test
        public void processOrdersStatuses_SpOrdersSetIsEmpty() {
            when(sp.getOrders()).thenReturn(new HashSet<>());

            spService.processOrdersStatuses(sp);

            verifyZeroInteractions(orderService);
        }

    }

}