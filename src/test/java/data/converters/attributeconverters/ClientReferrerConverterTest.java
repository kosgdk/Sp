package data.converters.attributeconverters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import sp.data.converters.attributeconverters.ClientReferrerConverter;
import sp.data.entities.enumerators.ClientReferrer;

import static org.junit.Assert.*;


@RunWith(BlockJUnit4ClassRunner.class)
public class ClientReferrerConverterTest {

    ClientReferrerConverter converter = new ClientReferrerConverter();

    @Test
    public void convertToDatabaseColumn_ShouldConvertClientReferrerToInteger() {
        assertEquals(new Integer(2), converter.convertToDatabaseColumn(ClientReferrer.VK));
    }

    @Test
    public void convertToEntityAttribute_ShouldConvertIntegerToClientReferrer() {
        assertEquals(ClientReferrer.VK, converter.convertToEntityAttribute(2));
    }

}