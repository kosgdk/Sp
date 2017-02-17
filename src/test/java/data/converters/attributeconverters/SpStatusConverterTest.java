package data.converters.attributeconverters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.converters.attributeconverters.ProductStatusConverter;
import sp.data.converters.attributeconverters.SpStatusConverter;
import sp.data.entities.enumerators.ProductStatus;
import sp.data.entities.enumerators.SpStatus;

import static org.junit.Assert.assertEquals;


@RunWith(BlockJUnit4ClassRunner.class)
public class SpStatusConverterTest {

    SpStatusConverter converter = new SpStatusConverter();

    @Test
    public void convertToDatabaseColumn_ShouldConvertSpStatusToInteger() {
        assertEquals(new Integer(6), converter.convertToDatabaseColumn(SpStatus.ARRIVED));
    }

    @Test
    public void convertToEntityAttribute_ShouldConvertIntegerToSpStatus() {
        assertEquals(SpStatus.ARRIVED, converter.convertToEntityAttribute(6));
    }

}