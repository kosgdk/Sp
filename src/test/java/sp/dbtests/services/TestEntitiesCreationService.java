package sp.dbtests.services;

import sp.data.entities.*;
import sp.data.entities.enumerators.ClientReferrer;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.Place;
import sp.data.entities.enumerators.SpStatus;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestEntitiesCreationService {

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private Date createDate(String string){
		try {
			Date date = sdf.parse(string);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date(1477342800000L);
	}

	public Properties createTestProperties() {
		Properties properties = new Properties();
		properties.setPercentSp(new BigDecimal(0.15));
		properties.setPercentDiscount(new BigDecimal(0.03));
		properties.setPercentBankCommission(new BigDecimal(0.01));
		return properties;
	}

	public Product createTestProduct(int seed) {
		Product product = new Product();
		product.setName("Test product " + seed);
		product.setPrice(new BigDecimal(500.15 + seed));
		product.setLink("http://testlink.ru/" + seed);
		product.setPhoto(new byte[]{1,0,1});
		product.setVkId(123456 + seed);
		product.setImageLink("http://testlink.ru/image" + seed);
		product.setDeleted(0);
		product.setVkPhotoId(123456789 + seed);
		product.setWeight(70 + seed);
		return product;
	}

	public Client createTestClient(int seed) {
		Client client = new Client();
		client.setName("Test Client " + seed);
		client.setRealName("Corey Taylor " + seed);
		client.setPhone("+7(978)123-45-6" + Integer.toString(Math.abs(seed)).charAt(0));
		client.setNote("Test note " + seed);
		client.setClientReferrer(ClientReferrer.VK);
		return client;
	}

	public OrderPosition createTestOrderPositionWithChildren(int seed) {
		OrderPosition orderPosition = createTestOrderPosition(seed);
		orderPosition.setOrder(createTestOrder(seed));
		orderPosition.setProduct(createTestProduct(seed));
		return orderPosition;
	}

	public OrderPosition createTestOrderPosition(int seed) {
		OrderPosition orderPosition = new OrderPosition();
		orderPosition.setPriceOrdered(new BigDecimal(400.15 + seed));
		orderPosition.setPriceSp(new BigDecimal(450.15 + seed));
		orderPosition.setPriceVendor(new BigDecimal(350.15 + seed));
		orderPosition.setQuantity(Math.abs(seed) + 1);
		orderPosition.setNote("Test note " + seed);

		Order order = new Order();
		order.setId(1L);
		orderPosition.setOrder(order);

		Product product = new Product();
		product.setId(1L);
		orderPosition.setProduct(product);

		return orderPosition;
	}

	public Order createTestOrder(int seed) {
		Client client = createTestClient(seed);
		Sp sp = createTestSp(seed);
		return createTestOrder(seed, client, sp);
	}

	public Order createTestOrder(int seed, Client client, Sp sp) {
		Order order = new Order();
		order.setClient(client);
		order.setSp(sp);
		order.setNote("Test note " + seed);
		order.setStatus(OrderStatus.UNPAID);
		order.setPrepaid(new BigDecimal(200.15 + seed));
		order.setWeight(150 + Math.abs(seed));
		order.setDeliveryPrice(new BigDecimal(20.15 + Math.abs(seed)));
		order.setPlace(Place.OKEAN);
		order.setDateOrdered(createDate("2016-10-05"));
		order.setDateCompleted(createDate("2016-10-25"));
		return order;
	}

	public Sp createTestSp(int seed) {
		Sp sp = new Sp();
		sp.setNumber(Math.abs(seed));
		sp.setPercent(new BigDecimal(0.15));
		sp.setStatus(SpStatus.COLLECTING);
		sp.setDateStart(createDate("2016-10-01"));
		sp.setDateEnd(createDate("2016-10-10"));
		sp.setDateToPay(createDate("2016-10-13"));
		sp.setDateSent(createDate("2016-10-16"));
		sp.setDateToReceive(createDate("2016-10-23"));
		sp.setDateReceived(createDate("2016-10-22"));
		sp.setDateToDistribute(createDate("2016-10-25"));
		return sp;
	}
}
