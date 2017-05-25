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
import sp.data.dao.interfaces.OrderDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.OrderService;
import sp.data.services.interfaces.SpService;
import testservices.FaultTolerantTheoriesRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;


@RunWith(Enclosed.class)
@ContextConfiguration("classpath:applicationContext_ForTests.xml")
public class OrderServiceImplTest extends AbstractJUnit4SpringContextTests {

    @Mock
    OrderDao orderDao;

    @InjectMocks
    @Resource
    OrderService orderService;

    @Before
    public void setUp() throws Exception {
        // Initialize Spring context
        TestContextManager testContextManager = new TestContextManager(getClass());
        testContextManager.prepareTestInstance(this);

        MockitoAnnotations.initMocks(this);
    }


    @RunWith(BlockJUnit4ClassRunner.class)
    public static class OtherTests extends OrderServiceImplTest{

        @Test
        public void getByIdWithAllChildren_ShouldCallCorrespondingMethodInDao(){
            orderService.getByIdWithAllChildren(1L);
            verify(orderDao, times(1)).getByIdWithAllChildren(1L);
        }

        @Test
        public void updateStatuses_ShouldCallCorrespondingMethodInDao(){
            Sp sp = mock(Sp.class);
            orderService.updateStatuses(sp, OrderStatus.COMPLETED);
            verify(orderDao, times(1)).updateStatuses(sp, OrderStatus.COMPLETED);
        }
    }

    @RunWith(FaultTolerantTheoriesRunner.class)
    public static class processStatusTests extends OrderServiceImplTest{

        @Mock
        Order order;

        @DataPoints
        public static OrderStatus[] orderStatuses() {
            return OrderStatus.values();
        }

        @Test
        public void processStatus_OrderStatusIsUNPAID_PrepaidIsGreaterThan0_ShouldChangeOrderStatus(){
            when(order.getStatus()).thenReturn(OrderStatus.UNPAID);
            when(order.getPrepaid()).thenReturn(BigDecimal.ONE);
            orderService.processStatus(order);
            verify(order, times(1)).setStatus(OrderStatus.PAID);
        }

        @Test
        public void processStatus_OrderStatusIsUNPAID_PrepaidEquals0_ShouldNotChangeOrderStatus(){
            when(order.getStatus()).thenReturn(OrderStatus.UNPAID);
            when(order.getPrepaid()).thenReturn(BigDecimal.ZERO);
            orderService.processStatus(order);
            verify(order, never()).setStatus(any());
        }

        @Theory
        public void processStatus_OrderHasUnsuitableStatus_ShouldNotChangeOrderStatus(OrderStatus orderStatus){
            assumeFalse(orderStatus.equals(OrderStatus.UNPAID));
            when(order.getStatus()).thenReturn(orderStatus);
            orderService.processStatus(order);
            verify(order, never()).setStatus(any());
        }
    }

    @RunWith(FaultTolerantTheoriesRunner.class)
    public static class processSpStatusTests extends OrderServiceImplTest{

        @Mock Order order;
        @Mock Order order2;
        @Mock Sp sp;
        @Mock SpService spService;

        @DataPoints
        public static OrderStatus[] orderStatuses() {
            return OrderStatus.values();
        }

        @DataPoints
        public static SpStatus[] spStatuses() {
            return SpStatus.values();
        }

        @Before
        public void setUp() throws Exception {
            super.setUp();

            when(order.getId()).thenReturn(1L);
            when(order2.getId()).thenReturn(2L);
            when(order.getSp()).thenReturn(sp);
            when(sp.getOrders()).thenReturn(new HashSet<>(Arrays.asList(order, order2)));
        }

        private static void printTestParameters(Object...parameters){
            System.out.println(" - Parameters: " + Arrays.toString(parameters));
        }


        @Theory
        public void processSpStatus_OrderOrAnotherOrderInSpHasUnsuitableStatuses_ShouldNotUpdateSp(OrderStatus orderStatus, OrderStatus orderStatus2){
            assumeFalse(orderStatus == OrderStatus.COMPLETED && orderStatus2 == OrderStatus.COMPLETED);
            printTestParameters(orderStatus, orderStatus2);

            when(order.getStatus()).thenReturn(orderStatus);
            when(order2.getStatus()).thenReturn(orderStatus2);
            when(sp.getStatus()).thenReturn(SpStatus.DISTRIBUTING); // Sp has suitable status

            orderService.processSpStatus(order);

            verify(sp, never()).setStatus(any(SpStatus.class));
            verifyZeroInteractions(spService);
        }


        @Theory
        public void processSpStatus_SpHasUnsuitableStatus_ShouldNotUpdateSp(SpStatus spStatus){
            assumeFalse(spStatus == SpStatus.DISTRIBUTING);
            printTestParameters(spStatus);

            when(sp.getStatus()).thenReturn(spStatus);
            when(order.getStatus()).thenReturn(OrderStatus.COMPLETED); // Order has suitable status
            when(order2.getStatus()).thenReturn(OrderStatus.COMPLETED); // Order2 has suitable status

            orderService.processSpStatus(order);

            verify(sp, never()).setStatus(any(SpStatus.class));
            verifyZeroInteractions(spService);
        }

        @Theory
        public void processSpStatus_AllOrdersAndSpHasSuitableStatus_ShouldUpdateSp(OrderStatus orderStatus, OrderStatus orderStatus2){
            assumeTrue(orderStatus == OrderStatus.COMPLETED && orderStatus2 == OrderStatus.COMPLETED);
            printTestParameters(orderStatus, orderStatus2);

            when(order.getStatus()).thenReturn(orderStatus);
            when(order2.getStatus()).thenReturn(orderStatus2);
            when(sp.getStatus()).thenReturn(SpStatus.DISTRIBUTING); // Sp has suitable status

            orderService.processSpStatus(order);

            verify(sp, times(1)).setStatus(SpStatus.COMPLETED);
            verify(spService, times(1)).update(sp);
        }

    }

}