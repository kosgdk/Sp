package sp.data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sp.data.dao.interfaces.SpDao;
import sp.data.entities.Sp;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.SpService;

@Service("SpService")
public class SpServiceImpl extends GenericServiceImpl<Sp, Integer> implements SpService {

	@Autowired
	SpDao spDao;
	
	@Override
	public int getLastNumber() {
		return spDao.getLastNumber();
	}

	@Override
	public Sp getLastSp() {
		return spDao.getLastSp();
	}

	@Override
	public Sp getByIdLazy(int id) {
		return spDao.getByIdLazy(id);
	}

	@Override
	public Sp getByIdWithAllChildren(int id) {
		return spDao.getByIdWithAllChildren(id);
	}
}
