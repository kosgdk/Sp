package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDao;
import sp.data.entities.Sp;

public interface SpDao extends GenericDao<Sp, Integer>{
	
	int getLastNumber();
	
	Sp getLastSp();
	
	Sp getByNumber(int number);

}
