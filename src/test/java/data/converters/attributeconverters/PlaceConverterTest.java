package data.converters.attributeconverters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.converters.attributeconverters.PlaceConverter;
import sp.data.entities.enumerators.Place;

import static org.junit.Assert.assertEquals;


@RunWith(BlockJUnit4ClassRunner.class)
public class PlaceConverterTest {

    PlaceConverter converter = new PlaceConverter();

    @Test
    public void convertToDatabaseColumn_ShouldConvertPlaceToInteger() {
        assertEquals(new Integer(2), converter.convertToDatabaseColumn(Place.OKEAN));
    }

    @Test
    public void convertToEntityAttribute_ShouldConvertIntegerToPlace() {
        assertEquals(Place.OKEAN, converter.convertToEntityAttribute(2));
    }

}