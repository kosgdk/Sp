package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDao;
import sp.data.entities.OrderPosition;

import java.util.List;

public interface OrderPositionDao extends GenericDao<OrderPosition,Long> {

	List<OrderPosition> getZeroWeightOrderPositions(Long spId);

}
