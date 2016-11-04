package sp.data.entities;

import sp.data.entities.enumerators.ClientReferrer;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.Place;
import sp.data.entities.enumerators.SpStatus;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EntitiesCreationServiceImpl implements EntitiesCreationService {

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

	@Override
	public Properties createTestProperties() {
		Properties properties = new Properties();
		properties.setPercentSp(new BigDecimal(0.15));
		properties.setPercentDiscount(new BigDecimal(0.03));
		properties.setPercentBankCommission(new BigDecimal(0.01));
		return properties;
	}

	@Override
	public Product createTestProduct(int id) {
		Product product = new Product();
		product.setName("Test product " + id);
		product.setPrice(new BigDecimal(500 + id));
		product.setLink("http://testlink.ru/" + id);
		product.setPhoto(new byte[]{1,0,1});
		product.setVkId(123456 + id);
		product.setImageLink("http://testlink.ru/image" + id);
		product.setDeleted(0);
		product.setVkPhotoId(123456789 + id);
		product.setWeight(70 + id);
		return product;
	}

	@Override
	public Client createTestClient(int id) {
		Client client = new Client();
		client.setName("Test Client " + id);
		client.setRealName("Corey Taylor " + id);
		client.setPhone("+7(978)123-45-6" + Integer.toString(Math.abs(id)).charAt(0));
		client.setNote("Test note " + id);
		client.setClientReferrer(ClientReferrer.VK);
		return client;
	}

	@Override
	public OrderPosition createTestOrderPosition(int id) {
		OrderPosition orderPosition = new OrderPosition();
		orderPosition.setPriceOrdered(new BigDecimal(400 + id));
		orderPosition.setPriceSp(new BigDecimal(450 + id));
		orderPosition.setPriceVendor(new BigDecimal(350 + id));
		orderPosition.setQuantity(Math.abs(id) + 1);
		orderPosition.setNote("Test note " + id);
		return orderPosition;
	}

	@Override
	public Order createTestOrder(int id, Client client, Sp sp) {
		Order order = new Order();
		order.setClient(client);
		order.setSp(sp);
		order.setNote("Test note " + id);
		order.setStatus(OrderStatus.UNPAID);
		order.setPrepaid(new BigDecimal(200 + id));
		order.setWeight(150 + Math.abs(id));
		order.setDeliveryPrice(new BigDecimal(20 + Math.abs(id)));
		order.setPlace(Place.OKEAN);
		order.setDateOrdered(createDate("2016-10-05"));
		order.setDateCompleted(createDate("2016-10-25"));
		return order;
	}

	@Override
	public Sp createTestSp(int id) {
		Sp sp = new Sp();
		sp.setNumber(Math.abs(id));
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
