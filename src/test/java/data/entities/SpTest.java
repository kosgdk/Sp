package data.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.Order;
import sp.data.entities.Sp;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(BlockJUnit4ClassRunner.class)
public class SpTest {

    private final Sp sp = new Sp();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void setPercent_ShouldSetScaledValue() {
        sp.setPercent(new BigDecimal(0.6));
        assertEquals("",  new BigDecimal(0.6).setScale(2, BigDecimal.ROUND_HALF_DOWN), sp.getPercent());
    }

    @Test
    public void setPercent_InputNull_ShouldDoNothing() {
        sp.setPercent(new BigDecimal(0.5));
        sp.setPercent(null);
        assertEquals("",  new BigDecimal(0.5).setScale(2, BigDecimal.ROUND_HALF_DOWN), sp.getPercent());
    }

    @Test
    public void setDateEnd_ShouldSetDateEnd() throws ParseException {
        sp.setDateEnd(sdf.parse("2017-01-01"));
        assertEquals(sdf.parse("2017-01-01"), sp.getDateEnd());
    }

    @Test
    public void setDateEnd_ShouldCalculateAndSetDateToPay() throws ParseException {
        sp.setDateEnd(sdf.parse("2017-01-01"));
        assertEquals(sdf.parse("2017-01-04"), sp.getDateToPay());
    }

    @Test
    public void setDateEnd_InputNull_ShouldSetDateEndToNull() throws ParseException {
        sp.setDateEnd(sdf.parse("2017-01-01"));
        sp.setDateEnd(null);
        assertNull(sp.getDateEnd());
    }

    @Test
    public void setDateEnd_InputNull_ShouldSetDateToPayToNull() throws ParseException {
        sp.setDateEnd(sdf.parse("2017-01-01"));
        sp.setDateEnd(null);
        assertNull(sp.getDateToPay());
    }

    @Test
    public void getWeight_ShouldReturnWeight(){
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        when(order1.getWeight()).thenReturn(200);
        when(order2.getWeight()).thenReturn(300);
        sp.setOrders(new HashSet<>(Arrays.asList(order1, order2)));
        assertEquals(500, sp.getWeight());
    }

    @Test
    public void getWeight_OrdersListIsNull_ShouldReturn0() {
        sp.setOrders(null);
        assertEquals(0, sp.getWeight());
    }

    @Test
    public void getWeight_OrdersListIsEmpty_ShouldReturn0() {
        sp.setOrders(new HashSet<>());
        assertEquals(0, sp.getWeight());
    }

    @Test
    public void getWeight_OneOfOrdersIsNull_ShouldReturn0() {
        Order order1 = mock(Order.class);
        when(order1.getWeight()).thenReturn(200);
        sp.setOrders(new HashSet<>(Arrays.asList(order1, null)));
        assertEquals(0, sp.getWeight());
    }

}