package sp.data.services.interfaces;

import sp.data.entities.Sp;

public interface SpService {
	
	public int getLastNumber();
	
	public Sp getLastSp();
	
	public Sp getByNumber(int number);
	
	
}
