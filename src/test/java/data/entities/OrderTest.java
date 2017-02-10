package data.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(BlockJUnit4ClassRunner.class)
public class OrderTest {

    Order order = new Order();

    OrderPosition orderPosition1 = mock(OrderPosition.class);
    OrderPosition orderPosition2 = mock(OrderPosition.class);

    private List<OrderPosition> getOrderPositionsMocksList() {
        List<OrderPosition> orderPositions = new ArrayList<>();
        orderPositions.add(orderPosition1);
        orderPositions.add(orderPosition2);
        return orderPositions;
    }


    @Test
    public void getSummaryPrice_ShouldReturnCalculatedSummaryPrice() {
        when(orderPosition1.getPriceSpSummary()).thenReturn(new BigDecimal(700.25));
        when(orderPosition2.getPriceSpSummary()).thenReturn(new BigDecimal(299.75));
        order.setOrderPositions(getOrderPositionsMocksList());
        assertEquals("", new BigDecimal(1000).setScale(2, BigDecimal.ROUND_HALF_DOWN), order.getSummaryPrice());
    }

    @Test
    public void getSummaryPrice_ShouldReturnCalculatedSummaryPrice_WhenOneOfOrderPositionsIsNull() {
        when(orderPosition1.getPriceSpSummary()).thenReturn(new BigDecimal(700.25));
        when(orderPosition2.getPriceSpSummary()).thenReturn(new BigDecimal(299.75));
        order.setOrderPositions(getOrderPositionsMocksList());
        order.getOrderPositions().add(null);
        assertEquals("", new BigDecimal(1000).setScale(2, BigDecimal.ROUND_HALF_DOWN), order.getSummaryPrice());
    }

    @Test
    public void getSummaryPrice_ShouldReturn0_WhenOrderPositionsListIsNull() {
        order.setOrderPositions(null);
        assertEquals("", new BigDecimal(0), order.getSummaryPrice());
    }

    @Test
    public void getIncome_ShouldReturnCalculatedSummaryPrice() {
        when(orderPosition1.getIncome()).thenReturn(new BigDecimal(700.25));
        when(orderPosition2.getIncome()).thenReturn(new BigDecimal(299.75));
        order.setOrderPositions(getOrderPositionsMocksList());
        assertEquals("", new BigDecimal(1000).setScale(2, BigDecimal.ROUND_HALF_DOWN), order.getIncome());
    }

    @Test
    public void getIncome_ShouldReturnCalculatedSummaryPrice_WhenOneOfOrderPositionsIsNull() {
        when(orderPosition1.getIncome()).thenReturn(new BigDecimal(700.25));
        when(orderPosition2.getIncome()).thenReturn(new BigDecimal(299.75));
        order.setOrderPositions(getOrderPositionsMocksList());
        order.getOrderPositions().add(null);
        assertEquals("", new BigDecimal(1000).setScale(2, BigDecimal.ROUND_HALF_DOWN), order.getIncome());
    }

    @Test
    public void getSIncome_ShouldReturn0_WhenOrderPositionsListIsNull() {
        order.setOrderPositions(null);
        assertEquals("", new BigDecimal(0), order.getSummaryPrice());
    }

    @Test
    public void getDebt_ShouldReturnCalculatedDebt() {
        when(orderPosition1.getPriceSpSummary()).thenReturn(new BigDecimal(700.25));
        when(orderPosition2.getPriceSpSummary()).thenReturn(new BigDecimal(299.75));
        order.setOrderPositions(getOrderPositionsMocksList());
        order.setPrepaid(new BigDecimal(1050));
        assertEquals("", new BigDecimal(-50).setScale(2, BigDecimal.ROUND_HALF_DOWN), order.getDebt());
    }

    @Test
    public void getTotal_ShouldReturnCalculatedTotal() {
        when(orderPosition1.getPriceSpSummary()).thenReturn(new BigDecimal(700.25));
        when(orderPosition2.getPriceSpSummary()).thenReturn(new BigDecimal(299.75));
        order.setOrderPositions(getOrderPositionsMocksList());
        order.setPrepaid(new BigDecimal(1050));
        order.setDeliveryPrice(new BigDecimal(15));
        assertEquals("", new BigDecimal(-35).setScale(2, BigDecimal.ROUND_HALF_DOWN), order.getTotal());
    }

    @Test
    public void getWeight_ShouldReturnCalculatedWeight() {
        when(orderPosition1.getWeight()).thenReturn(32);
        when(orderPosition2.getWeight()).thenReturn(100);

        order.setOrderPositions(getOrderPositionsMocksList());
        assertEquals("", new Integer(132), order.getWeight());
    }

    @Test
    public void getWeight_ShouldReturnCalculatedWeight_IfOneOfOrderPositionsIsNull() {
        when(orderPosition1.getWeight()).thenReturn(32);
        when(orderPosition2.getWeight()).thenReturn(100);

        order.setOrderPositions(getOrderPositionsMocksList());
        order.getOrderPositions().add(null);
        assertEquals("", new Integer(132), order.getWeight());
    }

    @Test
    public void getWeight_ShouldReturn0_IfOrderPositionsListIsNull() {
        order.setOrderPositions(null);
        assertEquals("", new Integer(0), order.getWeight());
    }

    @Test
    public void setPrepaid_ShouldSetScaledValue() throws Exception {
        order.setPrepaid(new BigDecimal(100));
        assertEquals("",  new BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_DOWN), order.getPrepaid());
    }

    @Test
    public void setPrepaid_InputNull_ShouldSet0() throws Exception {
        order.setPrepaid(null);
        assertEquals("",  new BigDecimal(0), order.getPrepaid());
    }

    @Test
    public void setDeliveryPrice_ShouldSetScaledValue() throws Exception {
        order.setDeliveryPrice(new BigDecimal(20));
        assertEquals("",  new BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_DOWN), order.getDeliveryPrice());
    }

    @Test
    public void setDeliveryPrice_InputNull_ShouldSet0() throws Exception {
        order.setDeliveryPrice(null);
        assertEquals("",  new BigDecimal(0), order.getDeliveryPrice());
    }

}