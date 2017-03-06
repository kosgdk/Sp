package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDao;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;

import javax.persistence.NoResultException;
import java.util.List;

public interface SpDao extends GenericDao<Sp,Long> {
	
	Long getLastNumber();
	
	Sp getLastSp();

	Sp getByIdWithAllChildren(Long number) throws NoResultException;

	List<Long> getIdsByStatus(SpStatus... statuses);

}
