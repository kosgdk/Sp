package sp.data.services.interfaces;

import sp.data.entities.OrderPosition;
import sp.data.services.generic.GenericService;

import java.util.List;

public interface OrderPositionService extends GenericService<OrderPosition, Long> {

	List<OrderPosition> getZeroWeightOrderPositions(Long spId);
	
}
