package sp.data.entities;

public interface EntitiesCreationService {

	Properties createTestProperties();

	Product createTestProduct(int id);

	Client createTestClient(int id);

	OrderPosition createTestOrderPosition(int id);

	Order createTestOrder(int id, Client client, Sp sp);

	Sp createTestSp(int id);

}
