package testsuits;

import data.services.*;
import data.services.generic.GenericServiceForNamedEntitiesImplTest;
import data.services.generic.GenericServiceImplTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        GenericServiceForNamedEntitiesImplTest.class,
        GenericServiceImplTest.class,
        ClientServiceImplTest.class,
        OrderServiceImplTest.class,
		OrderPositionServiceImplTest.class,
        PropertiesServiceImplTest.class,
        SpServiceImplTest.class
})
public class ServiceTestsSuite {

}
