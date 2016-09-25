package sp.data.services.interfaces;

import sp.data.entities.Sp;
import sp.data.services.generic.GenericService;

public interface SpService extends GenericService<Sp, Integer> {
	
	int getLastNumber();
	
	Sp getLastSp();
	
	Sp getByIdLazy(int number);

	Sp getByIdWithAllChildren(int number);
	
	
}
