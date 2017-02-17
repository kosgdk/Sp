package data.converters.attributeconverters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.converters.attributeconverters.ProductStatusConverter;
import sp.data.entities.enumerators.ProductStatus;

import static org.junit.Assert.assertEquals;


@RunWith(BlockJUnit4ClassRunner.class)
public class ProductStatusConverterTest {

    ProductStatusConverter converter = new ProductStatusConverter();

    @Test
    public void convertToDatabaseColumn_ShouldConvertProductStatusToInteger() {
        assertEquals(new Integer(2), converter.convertToDatabaseColumn(ProductStatus.NOT_AVAILABLE));
    }

    @Test
    public void convertToEntityAttribute_ShouldConvertIntegerToProductStatus() {
        assertEquals(ProductStatus.NOT_AVAILABLE, converter.convertToEntityAttribute(2));
    }

}