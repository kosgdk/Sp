package sp.data.dao.interfaces;

import sp.data.dao.generic.GenericDao;
import sp.data.entities.Sp;

public interface SpDao extends GenericDao<Sp, Integer>{
	
	public int getLastNumber();
	
	public Sp getLastSp();
	
	public Sp getByNumber(int number);

}
