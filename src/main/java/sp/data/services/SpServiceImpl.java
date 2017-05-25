package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderPositionService;
import sp.data.services.interfaces.OrderService;
import sp.data.services.interfaces.ProductService;
import sp.data.services.interfaces.SpService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("SpService")
public class SpServiceImpl extends GenericServiceImpl<Sp, Long> implements SpService {

	@Autowired SpDao spDao;
    @Autowired OrderService orderService;
    @Autowired ProductService productService;
    @Autowired OrderPositionService orderPositionService;
	
	@Override
	public Long getLastNumber() {
		return spDao.getLastNumber();
	}

	@Override
	public Sp getLastSp() {
		return spDao.getLastSp();
	}

	@Override
	public Sp getByIdWithAllChildren(Long id) {
		return spDao.getByIdWithAllChildren(id);
	}

	@Override
	public List<Long> getIdsByStatus(SpStatus... statuses) {
		return spDao.getIdsByStatus(statuses);
	}

	@Override
	public void processOrdersStatuses(Sp sp){
		System.out.println("inside processOrdersStatuses()");

		Set<Order> orders = sp.getOrders();
        if (orders != null && !orders.isEmpty()) {

            Set<OrderStatus> ordersStatuses = new HashSet<>();
            orders.forEach(order -> ordersStatuses.add(order.getStatus()));

            switch (sp.getStatus()) {

                case PACKING: //Комплектуется
                    System.out.println("case: PACKING");
                    if (ordersStatuses.size() == 1 && ordersStatuses.contains(OrderStatus.PAID)) {
                        orderService.updateStatuses(sp, OrderStatus.PACKING);
                    }
                    break;

                case SENT: //Отправлен
                    System.out.println("case: SENT");
                    if (ordersStatuses.size() == 1 && ordersStatuses.contains(OrderStatus.PACKING)) {
                        orderService.updateStatuses(sp, OrderStatus.SENT);
                    }
                    break;

                case ARRIVED: //Прибыл
                    System.out.println("case: ARRIVED");
                    if (ordersStatuses.size() == 1 && ordersStatuses.contains(OrderStatus.SENT)) {
						System.out.println("updateStatuses()");
						orderService.updateStatuses(sp, OrderStatus.ARRIVED);
                    }
                    if (sp.getDeliveryPrice() != null && sp.getDeliveryPrice().compareTo(BigDecimal.ZERO) >= 0){
						System.out.println("calculateDeliveryPriceForOrders()");
						calculateDeliveryPriceForOrders(sp);
                    }
                    break;
            }
        }

	}

    @Override
    public void calculateDeliveryPriceForOrders(Sp sp) {
        // Return if Sp has OrderPositions with zero weight
        if (!orderPositionService.getZeroWeightOrderPositions(sp.getId()).isEmpty()) return;

        BigDecimal spWeight = new BigDecimal(0);
        Set<Order> orders = sp.getOrders();

        // Calculate summary Sp weight
        for (Order order : orders) {
            spWeight = spWeight.add(new BigDecimal(order.getWeight()));
        }

        // Calculate and set deliveryPrice for each order
        for (Order order : orders) {
            BigDecimal orderWeight = new BigDecimal(order.getWeight());
            order.setDeliveryPrice((orderWeight.multiply(sp.getDeliveryPrice())).divide(spWeight, BigDecimal.ROUND_HALF_UP));
            orderService.update(order);
        }

    }

}
