package sp.data.services.interfaces;

import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.generic.GenericService;

import java.util.List;

public interface SpService extends GenericService<Sp, Long> {
	
	Long getLastNumber();
	
	Sp getLastSp();

	Sp getByIdWithAllChildren(Long number);

    List<Long> getIdsByStatus(SpStatus... statuses);

	public void setOrdersStatuses(Sp sp);
	
}
