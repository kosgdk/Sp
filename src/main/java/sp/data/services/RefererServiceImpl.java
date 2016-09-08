package sp.data.services;

import org.springframework.stereotype.Service;

import sp.data.entities.Referer;
import sp.data.services.generic.GenericServiceImpl;
import sp.data.services.interfaces.RefererService;

@Service("RefererService")
public class RefererServiceImpl extends GenericServiceImpl<Referer, Integer> implements RefererService {


}
