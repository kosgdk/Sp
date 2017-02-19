package testservices;

import org.apache.commons.lang.StringUtils;
import sp.data.entities.*;
import sp.data.entities.enumerators.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestEntitiesCreationService {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public static Date createDate(String string){
		try {
			return sdf.parse(string);
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
		product.setName(StringUtils.rightPad("Test product " + seed, 300, "_"));
		product.setPrice(new BigDecimal(500.15 + seed));
		product.setLink(StringUtils.rightPad("http://testlink.ru/" + seed, 300, "_"));
		product.setVkId(123456 + (seed % 10));
		product.setImageLink(StringUtils.rightPad("http://testlink.ru/image" + seed, 300, "_"));
		product.setStatus(ProductStatus.AVAILABLE);
		product.setVkPhotoId(123456789 + (seed % 10));
		product.setWeight(70 + seed);
		return product;
	}

	public Client createTestClient(int seed) {
		Client client = new Client();
		client.setName(StringUtils.rightPad("Test Client " + seed, 50, "_"));
		client.setRealName(StringUtils.rightPad("Real Name " + seed, 50, "_"));
		client.setPhone("+7(978)123-45-6" + (seed % 10));
		client.setNote(StringUtils.rightPad("Test note " + seed, 500, "_"));
		client.setClientReferrer(ClientReferrer.VK);
		return client;
	}

	public OrderPosition createTestOrderPositionNoReferences(int seed) {
		OrderPosition orderPosition = new OrderPosition();
		orderPosition.setPriceOrdered(new BigDecimal(400.15 + seed));
		orderPosition.setPriceSp(new BigDecimal(450.15 + seed));
		orderPosition.setPriceVendor(new BigDecimal(350.15 + seed));
		orderPosition.setQuantity(Math.abs(seed) + 1);
		orderPosition.setNote(StringUtils.leftPad("Test note " + seed, 500, "a"));

		return orderPosition;
	}

	public OrderPosition createTestOrderPosition(int seed) {
		OrderPosition orderPosition = createTestOrderPositionNoReferences(seed);

		Order order = new Order();
		order.setId(1L);
		orderPosition.setOrder(order);

		Product product = new Product();
		product.setId(1L);
		orderPosition.setProduct(product);

		return orderPosition;
	}

	public Order createTestOrderNoReferences(int seed) {
		Order order = new Order();
		order.setNote(StringUtils.rightPad("Test note " + seed, 500, "_"));
		order.setStatus(OrderStatus.UNPAID);
		order.setPrepaid(new BigDecimal(200.15 + seed));
		order.setWeight(150 + Math.abs(seed));
		order.setDeliveryPrice(new BigDecimal(20.15 + Math.abs(seed)));
		order.setPlace(Place.OKEAN);
		order.setDateOrdered(createDate("2016-10-05"));
		order.setDateCompleted(createDate("2016-10-25"));

		return order;
	}

	public Order createTestOrder(int seed) {
		Order order = createTestOrderNoReferences(seed);

		Client client = new Client();
		client.setId((long) seed);
		order.setClient(client);

		Sp sp = new Sp();
		sp.setId((long) seed);
		order.setSp(sp);

		return order;
	}

	public Sp createTestSp(long seed) {
		Sp sp = new Sp();
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
