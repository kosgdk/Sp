package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDao;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;

import javax.persistence.NoResultException;
import java.util.SortedSet;

public interface SpDao extends GenericDao<Sp,Long> {
	
	Long getLastNumber();
	
	Sp getLastSp();

	Sp getByIdWithAllChildren(Long number) throws NoResultException;

	SortedSet<Long> getIdsByStatus(SpStatus... statuses);

}
