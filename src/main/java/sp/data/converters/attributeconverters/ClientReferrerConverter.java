package sp.data.converters.attributeconverters;

import sp.data.entities.enumerators.ClientReferrer;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ClientReferrerConverter implements AttributeConverter<ClientReferrer, Integer> {

	@Override
	public Integer convertToDatabaseColumn(ClientReferrer clientReferrer) {
		return clientReferrer == null ? null : clientReferrer.getId();
	}

	@Override
	public ClientReferrer convertToEntityAttribute(Integer id) {
		return id == null ? null : ClientReferrer.getById(id);
	}
}
