package testsuits;

import data.services.ClientServiceImplTest;
import data.services.OrderServiceImplTest;
import data.services.PropertiesServiceImplTest;
import data.services.SpServiceImplTest;
import data.services.generic.GenericServiceForNamedEntitiesImplTest;
import data.services.generic.GenericServiceImplTest;
import data.validators.ClientValidatorTest;
import data.validators.OrderValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        GenericServiceForNamedEntitiesImplTest.class,
        GenericServiceImplTest.class,
        ClientServiceImplTest.class,
        OrderServiceImplTest.class,
        PropertiesServiceImplTest.class,
        SpServiceImplTest.class
})
public class ServiceTestsSuite {

}
