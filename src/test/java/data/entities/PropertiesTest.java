package data.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.entities.Properties;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;


@RunWith(BlockJUnit4ClassRunner.class)
public class PropertiesTest {

    Properties properties = new Properties();


    @Test
    public void setPercentSp_ShouldSetScaledValue() throws Exception {
        properties.setPercentSp(new BigDecimal(0.16));
        assertEquals("",  new BigDecimal(0.16).setScale(2, BigDecimal.ROUND_HALF_DOWN), properties.getPercentSp());
    }

    @Test
    public void setPercentSp_InputNull_ShouldDoNothing() throws Exception {
        properties.setPercentSp(null);
        assertEquals("",  new BigDecimal(0.15).setScale(2, BigDecimal.ROUND_HALF_DOWN), properties.getPercentSp());
    }

    @Test
    public void setPercentDiscount_ShouldSetScaledValue() throws Exception {
        properties.setPercentDiscount(new BigDecimal(0.02));
        assertEquals("",  new BigDecimal(0.02).setScale(2, BigDecimal.ROUND_HALF_DOWN), properties.getPercentDiscount());
    }

    @Test
    public void setPercentDiscount_InputNull_ShouldDoNothing() throws Exception {
        properties.setPercentDiscount(null);
        assertEquals("",  new BigDecimal(0.03).setScale(2, BigDecimal.ROUND_HALF_DOWN), properties.getPercentDiscount());
    }

    @Test
    public void setPercentBankCommission_ShouldSetScaledValue() throws Exception {
        properties.setPercentBankCommission(new BigDecimal(0.02));
        assertEquals("",  new BigDecimal(0.02).setScale(2, BigDecimal.ROUND_HALF_DOWN), properties.getPercentBankCommission());
    }

    @Test
    public void setPercentBankCommission_InputNull_ShouldDoNothing() throws Exception {
        properties.setPercentBankCommission(null);
        assertEquals("",  new BigDecimal(0.01).setScale(2, BigDecimal.ROUND_HALF_DOWN), properties.getPercentBankCommission());
    }

}