package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sp.data.dao.interfaces.OrderPositionDao;
import sp.data.entities.OrderPosition;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.OrderPositionService;

import java.util.List;

@Service("OrderPositionService")
public class OrderPositionServiceImpl extends GenericServiceImpl<OrderPosition, Long> implements OrderPositionService {

	@Autowired
	OrderPositionDao orderPositionDao;

	@Override
	public List<OrderPosition> getZeroWeightOrderPositions(Long spId) {
		return orderPositionDao.getZeroWeightOrderPositions(spId);
	}
}
