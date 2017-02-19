package database.tests;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.unitils.UnitilsBlockJUnit4ClassRunner;
import org.unitils.database.DatabaseUnitils;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.annotation.ExpectedDataSet;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByType;
import sp.data.dao.interfaces.ClientDao;
import sp.data.entities.Client;
import testservices.CauseExceptionMatcher;
import testservices.TestEntitiesCreationService;


@RunWith(UnitilsBlockJUnit4ClassRunner.class)
@SpringApplicationContext("applicationContext_ForTests_DisabledBeanValidation.xml")
@Transactional(TransactionMode.DISABLED)
public class ClientDbValidationTest {

    @SpringBeanByType
    ClientDao dao;

    TestEntitiesCreationService service = new TestEntitiesCreationService();

    Client client = service.createTestClient(1);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        DatabaseUnitils.updateSequences();
    }


    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @DataSet("db_test/dataset/validation/client/ClientDbValidationTest.defaultValuesTest.xml")
    @ExpectedDataSet("db_test/dataset/validation/client/ClientDbValidationTest.defaultValuesTest_ExpectedDataSet.xml")
    public void defaultValuesTest() {}

    @Test
    public void name_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'name' cannot be null"));
        client.setName(null);
        dao.save(client);
    }

    @Test
    public void referrer_ShouldNotBeNull_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Column 'referrer' cannot be null"));
        client.setClientReferrer(null);
        dao.save(client);
    }

    @Test
    @DataSet("db_test/dataset/validation/client/ClientDbValidationTest.emptyDataSet.xml")
    public void note_ShouldNotBeLongerThan500Characters_OrThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'note'"));

        client.setNote(StringUtils.leftPad("", 501, "a"));
        dao.save(client);
    }

    @Test
    @DataSet("db_test/dataset/validation/client/ClientDbValidationTest.emptyDataSet.xml")
    public void name_ShouldNotBeLongerThan50Characters_OrThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'name'"));

        client.setName(StringUtils.leftPad("", 51, "a"));
        dao.save(client);
    }

    @Test
    @DataSet("db_test/dataset/validation/client/ClientDbValidationTest.emptyDataSet.xml")
    public void realName_ShouldNotBeLongerThan50Characters_OrThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'real_name'"));

        client.setRealName(StringUtils.leftPad("", 51, "a"));
        dao.save(client);
    }

    @Test
    @DataSet("db_test/dataset/validation/client/ClientDbValidationTest.emptyDataSet.xml")
    public void phone_ShouldNotBeLongerThan16Characters_OrThrowException() {
        expectedException.expect(DataException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MysqlDataTruncation.class, "Data too long for column 'phone'"));

        client.setPhone(StringUtils.leftPad("", 17, "a"));
        dao.save(client);
    }

    @Test
    public void name_ShouldBeUnique_OrThrowException() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectCause(new CauseExceptionMatcher(MySQLIntegrityConstraintViolationException.class,
                                                                "Duplicate entry 'Name' for key 'name_UNIQUE'"));
        Client client2 = service.createTestClient(2);
        String name = "Name";
        client.setName(name);
        client2.setName(name);
        dao.save(client);
        dao.save(client2);
    }

}
