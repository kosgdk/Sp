package sp.data.services.interfaces;

import sp.data.entities.Sp;

public interface SpService {
	
	int getLastNumber();
	
	Sp getLastSp();
	
	Sp getByNumber(int number);
	
	
}
