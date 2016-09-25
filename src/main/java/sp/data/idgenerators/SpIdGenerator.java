package sp.data.idgenerators;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import sp.data.entities.Sp;

import java.io.Serializable;
import java.util.Properties;

public class SpIdGenerator implements IdentifierGenerator{

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Sp sp = (Sp) object;
        return sp.getNumber();
    }

}
