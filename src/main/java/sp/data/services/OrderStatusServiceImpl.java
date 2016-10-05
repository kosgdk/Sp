package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.data.dao.interfaces.OrderStatusDao;
import sp.data.entities.OrderStatus;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderStatusService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("OrderStatusService")
public class OrderStatusServiceImpl extends GenericServiceImpl<OrderStatus, Integer> implements OrderStatusService {

	@Autowired
	OrderStatusDao orderStatusDao;

	@Override
	public Map<String, String> getAllForSelect() {
		List<OrderStatus> orderStatusesList = orderStatusDao.getAll();
		Map<String, String> orderStatusesMap = new LinkedHashMap<>();
		for (OrderStatus orderStatus : orderStatusesList) {
			orderStatusesMap.put(Integer.toString(orderStatus.getId()), orderStatus.getName());
		}
		return orderStatusesMap;
	}
}
