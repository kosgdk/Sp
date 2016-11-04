package sp.data.dao.hibernate;

import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import sp.data.dao.hibernate.Interfaces.BasicCrudDaoTests;
import sp.data.dao.hibernate.Interfaces.DaoTestsForEntitiesWithNameField;
import sp.data.dao.interfaces.ClientDao;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.*;

import javax.persistence.Query;

import java.util.*;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml"})
@Transactional
public class ClientDaoHibernateImplTest implements BasicCrudDaoTests, DaoTestsForEntitiesWithNameField {


	@Autowired
	ClientDao dao;

	@Autowired
	SpDao spDao;

	@Autowired
	private SessionFactory sessionFactory;

	private EntitiesCreationService entityService = new EntitiesCreationServiceImpl();

	private static boolean collated = false;

	private void setNameColumnCollation(){
		if (!collated){
			collated = true;
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createNativeQuery("ALTER TABLE clients MODIFY name VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin;");
			query.executeUpdate();
		}
	}

	private Session currentSession(){
		return sessionFactory.getCurrentSession();
	}

	@Transactional
	private void disableForeignKeyCheck(){
		Session session = sessionFactory.getCurrentSession();
		String sql = "SET FOREIGN_KEY_CHECKS = 0;";
		Query query = session.createNativeQuery(sql);
		query.executeUpdate();
		session.getTransaction().commit();
	}

	@Transactional
	private void enableForeignKeyCheck(){
		Session session = sessionFactory.getCurrentSession();
		String sql = "SET FOREIGN_KEY_CHECKS = 1;";
		Query query = session.createNativeQuery(sql);
		Transaction transaction = session.beginTransaction();
		query.executeUpdate();
		transaction.commit();
	}


	@Before
	public void setUp() throws Exception {
		SQLStatementCountValidator.reset();
		setNameColumnCollation();
		System.out.println("Begin test");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("End test");
	}


	@Test
	public void saveAndGetById_InputExistingId_ShouldWriteAndReadFromDB() {
		Client client = entityService.createTestClient(1);
		dao.save(client);
		assertInsertCount(1);
		int id = client.getId();
		assertNotNull("save() method didn't write to DB", id);

		sessionFactory.getCurrentSession().evict(client);
		Client clientFromDB = dao.getById(id);
		assertSelectCount(1);
		assertNotNull("getById() method didn't read from DB", clientFromDB);
		assertEquals("DB client is not equal to persisted client", client, clientFromDB);
	}

	@Test
	public void GetById_InputNonExistentId_ShouldReturnNull() {
		Client client = entityService.createTestClient(1);
		dao.save(client);
		assertInsertCount(1);
		int id = client.getId();
		assertNotNull("save() method didn't write to DB", id);

		sessionFactory.getCurrentSession().evict(client);
		Client clientFromDB = dao.getById(id+1);
		assertSelectCount(1);
		assertNull("getById() method returned entry instead of NULL", clientFromDB);
	}

	@Test
	public void getById_InputNull_ShouldReturnNull(){
		Client client = entityService.createTestClient(1);
		dao.save(client);
		assertInsertCount(1);
		int id = client.getId();
		assertNotNull("save() method didn't write to DB", id);

		sessionFactory.getCurrentSession().evict(client);
		Client clientFromDB = dao.getById(null);
		assertSelectCount(0);
		assertNull("getById() method returned entry instead of NULL", clientFromDB);
	}

	@Test
	public void GetById_ShouldLazyLoadOrdersSet(){
		Sp sp = entityService.createTestSp(1);
		spDao.save(sp);
		currentSession().flush();
		assertInsertCount(1);

		Client client = entityService.createTestClient(1);

		Set<Order> orders = new LinkedHashSet<>();
		Order order1 = entityService.createTestOrder(1, client, sp);
		Order order2 = entityService.createTestOrder(2, client, sp);
		orders.add(order1);
		orders.add(order2);

		client.setOrders(orders);
		dao.save(client);
		currentSession().flush();
		assertInsertCount(4);

		currentSession().evict(order1);
		currentSession().evict(order2);
		currentSession().evict(client);
		Client clientFromDB = dao.getById(client.getId());
		assertSelectCount(1);
		assertEquals("Set of Orders have wrong size", 2, clientFromDB.getOrders().size());
		assertSelectCount(4);

		Iterator<Order> iterator = clientFromDB.getOrders().iterator();
		//Set of Orders in Client have descending order, so Order2 will go first
		assertEquals("Order 2 is invalid", order2, iterator.next());
		assertEquals("Order 1 is invalid", order1, iterator.next());
	}


	@Test
	public void getByName_InputExactName_ReturnOneEntity(){
		setNameColumnCollation();
		Client client1 = entityService.createTestClient(1);
		Client client2 = entityService.createTestClient(2);
		dao.save(client1);
		dao.save(client2);
		assertInsertCount(2);

		String name = client2.getName();
		currentSession().evict(client1);
		currentSession().evict(client2);

		Client clientFromDB = dao.getByName(name);
		assertSelectCount(1);
		assertNotNull(clientFromDB);
		assertEquals("getByName() returns wrong result", name, clientFromDB.getName());

	}

	@Test
	public void getByName_InputExactNameWrongCase_ReturnNull(){
		setNameColumnCollation();

		Client client1 = entityService.createTestClient(1);
		dao.save(client1);
		assertInsertCount(1);

		String name = client1.getName().toUpperCase();
		currentSession().evict(client1);

		Client clientFromDB = dao.getByName(name);
		assertSelectCount(1);
		assertNull(clientFromDB);
	}

	@Test
	public void getByName_InputWrongName_ReturnNull(){
		Client client = entityService.createTestClient(1);
		dao.save(client);
		assertInsertCount(1);

		currentSession().evict(client);

		String name = "wrongName";
		Client clientFromDB = dao.getByName(name);
		assertSelectCount(1);
		assertNull(clientFromDB);
	}

	@Test
	public void getByName_InputNullName_ReturnNull(){
		Client clientFromDB = dao.getByName(null);
		assertSelectCount(0);
		assertNull(clientFromDB);
	}


	@Test
	public void searchByName_InputExistingFragment_ShouldReturnMatches(){
		Client client1 = entityService.createTestClient(1);
		Client client2 = entityService.createTestClient(2);
		Client client12 = entityService.createTestClient(12);
		dao.save(client1);
		dao.save(client2);
		dao.save(client12);
		assertInsertCount(3);

		List<Client> clients = dao.searchByName(client1.getName());
		assertSelectCount(1);
		assertEquals(2, clients.size());

		assertEquals("First result is wrong", client1, clients.get(0));
		assertEquals("Second result is wrong", client12, clients.get(1));
	}

	@Test
	public void searchByName_InputExistingFragment_ShouldReturnMax30Matches(){
		int n = 31;
		for (int i = 0; i < n; i++) {
			 dao.save(entityService.createTestClient(i));
		}
		assertInsertCount(n);

		List<Client> clients = dao.searchByName("test");
		assertSelectCount(1);
		assertEquals(30, clients.size());
	}

	@Test
	public void searchByName_InputExistingFragmentWrongCase_ShouldReturnMatches(){
		Client client1 = entityService.createTestClient(1);
		Client client2 = entityService.createTestClient(2);
		Client client12 = entityService.createTestClient(12);
		dao.save(client1);
		dao.save(client2);
		dao.save(client12);
		assertInsertCount(3);

		List<Client> clients = dao.searchByName(client1.getName().toUpperCase());
		assertSelectCount(1);
		assertEquals(2, clients.size());

		assertEquals("First result is wrong", client1, clients.get(0));
		assertEquals("Second result is wrong", client12, clients.get(1));
	}

	@Test
	public void searchByName_InputNonExistentFragment_ShouldReturnEmptyList(){
		Client client1 = entityService.createTestClient(1);
		dao.save(client1);
		assertInsertCount(1);

		List<Client> clients = dao.searchByName("NonExistentFragment");
		assertSelectCount(1);
		assertEquals(0, clients.size());
	}

	@Test
	public void searchByName_InputNull_ShouldReturnNull(){
		List<Client> clients = dao.searchByName(null);
		assertSelectCount(0);
		assertNull(clients);
	}


	@Test
	public void getAll_ShouldReturnAllEntitiesList(){
		Client client1 = entityService.createTestClient(1);
		Client client2 = entityService.createTestClient(2);
		dao.save(client1);
		dao.save(client2);
		assertInsertCount(2);

		List<Client> clients = dao.getAll();
		assertSelectCount(1);
		assertNotNull("getAll() returns NULL instead of List", clients);
		assertEquals(2, clients.size());

		for (Client client : clients) {
			assertNotNull("getAll() returns NULL entity in List", client);
		}

	}


	@Test
	public void save_InputNull_ShouldNotWriteToDB(){
		dao.save(null);
		assertInsertCount(0);
	}


	@Test
	public void update_ShouldUpdateDB(){
		Client client = entityService.createTestClient(1);
		dao.save(client);
		assertInsertCount(1);
		int id = client.getId();
		assertNotNull("save() method didn't write to DB", id);

		String updatedName = "updated name";
		client.setName(updatedName);
		dao.update(client);
		sessionFactory.getCurrentSession().flush();
		assertUpdateCount(1);

		sessionFactory.getCurrentSession().evict(client);
		assertEquals("update() didn't write changes to DB", updatedName, dao.getById(id).getName());
		assertSelectCount(1);
	}


	@Test
	public void delete_ShouldDeleteFromDB(){
		Client client = entityService.createTestClient(1);
		dao.save(client);
		assertInsertCount(1);
		int id = client.getId();
		assertNotNull("save() method didn't write to DB", id);

		dao.delete(client);
		currentSession().flush();
		assertDeleteCount(1);

		currentSession().evict(client);
		assertNull("delete() method didn't delete entry from DB" ,dao.getById(id));
		assertSelectCount(1);
	}

	@Test
	public void delete_InputNull_ShouldNotDeleteFromDB(){
		dao.delete(null);
		assertDeleteCount(0);
	}


	@Test
	public void deleteById_InputExistingId_ShouldDeleteFromDB(){
		Client client = entityService.createTestClient(1);
		dao.save(client);
		assertInsertCount(1);
		int id = client.getId();
		assertNotNull(id);

		dao.deleteById(id);
		currentSession().flush();
		assertDeleteCount(1);

		currentSession().evict(client);
		assertNull("deleteById() didn't delete entry from DB", dao.getById(id));
		assertSelectCount(1);
	}

	@Test
	public void deleteById_InputNonExistentId_ShouldNotDeleteFromDB(){
		Client client = entityService.createTestClient(1);
		dao.save(client);
		assertInsertCount(1);
		int id = client.getId();
		assertNotNull(id);

		dao.deleteById(id+1);
		currentSession().flush();
		assertSelectCount(1);
		assertDeleteCount(0);

		currentSession().evict(client);
		assertNotNull("deleteById() deleted entry but shouldn't", dao.getById(id));
		assertSelectCount(2);
	}

	@Test
	public void deleteById_InputNULL_ShouldNotDeleteFromDB(){
		dao.deleteById(null);
		assertDeleteCount(0);
	}

}