package data.converters.attributeconverters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.converters.attributeconverters.OrderStatusConverter;
import sp.data.entities.enumerators.OrderStatus;

import static org.junit.Assert.assertEquals;


@RunWith(BlockJUnit4ClassRunner.class)
public class OrderStatusConverterTest {

    OrderStatusConverter converter = new OrderStatusConverter();

    @Test
    public void convertToDatabaseColumn_ShouldConvertOrderStatusToInteger() {
        assertEquals(new Integer(4), converter.convertToDatabaseColumn(OrderStatus.SENT));
    }

    @Test
    public void convertToEntityAttribute_ShouldConvertIntegerToOrderStatus() {
        assertEquals(OrderStatus.SENT, converter.convertToEntityAttribute(4));
    }

}