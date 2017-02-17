package testsuits;

import data.converters.*;
import data.converters.attributeconverters.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientReferrerConverterTest.class,
        OrderStatusConverterTest.class,
        PlaceConverterTest.class,
        ProductStatusConverterTest.class,
        SpStatusConverterTest.class,
        IdToOrderConverterTest.class,
        IdToOrderPositionConverterTest.class,
        IdToSpConverterTest.class,
        NameToClientConverterTest.class,
        NameToProductConverterTest.class,
        StringToClientReferrerConverterTest.class,
        StringToDateConverterTest.class,
        StringToOrderStatusConverterTest.class,
        StringToPlaceConverterTest.class,
        StringToSpStatusConverterTest.class
})
public class ConvertersTestsSuite {

}
