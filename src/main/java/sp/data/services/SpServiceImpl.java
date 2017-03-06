package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.SpService;

import java.util.*;


@Service("SpService")
public class SpServiceImpl extends GenericServiceImpl<Sp, Long> implements SpService {

	@Autowired
	SpDao spDao;
	
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

	public void setOrdersStatuses(Sp sp){
		System.out.println("inside setOrdersStatuses()");
		Set<Order> modifiedOrders = new HashSet<>();
		Iterator<Order> iterator = sp.getOrders().iterator();

		while (iterator.hasNext()) {
			Order order = iterator.next();
			switch (sp.getStatus()){
				case COLLECTING: //Сбор
					break;

				case CHECKOUT: //Оплата
					break;

				case PACKING: //Комплектуется
					System.out.println("case: PACKING");
					order.setStatus(OrderStatus.PACKING);
					break;

				case PAID: //Оплачен
					break;

				case SENT: //Отправлен
					System.out.println("case: SENT");
					order.setStatus(OrderStatus.SENT);
					break;

				case ARRIVED: //Получен
					System.out.println("case: ARRIVED");
					order.setStatus(OrderStatus.ARRIVED);
					break;

				case DISTRIBUTING: //Раздаётся
					break;

				case COMPLETED: //Завершён
					break;
			}

			iterator.remove();
			modifiedOrders.add(order);
		}
			sp.getOrders().clear();
			sp.getOrders().addAll(modifiedOrders);
			//return sp;
	}
}
