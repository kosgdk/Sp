package sp.data.services.interfaces;

import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.generic.GenericService;

import java.util.SortedSet;

public interface SpService extends GenericService<Sp, Integer> {
	
	int getLastNumber();
	
	Sp getLastSp();
	
	Sp getByIdLazy(int number);

	Sp getByIdWithAllChildren(int number);

    SortedSet<Integer> getIdsByStatus(SpStatus... statuses);

	public void setOrdersStatuses(Sp sp);
	
}
