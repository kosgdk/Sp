package data.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.Sp;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


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


}