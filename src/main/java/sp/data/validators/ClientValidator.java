package sp.data.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.Client;
import sp.data.services.interfaces.ClientService;


@Component("ClientValidator")
public class ClientValidator implements Validator {

    @Autowired
    ClientService clientService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        System.out.println("inside ClientValidator");

        Client client = (Client) target;

        // Unique name validation
        String name = client.getName();
        if (clientService.getByName(name) != null){
            errors.rejectValue("name", "client.name.notUnique");
        }

    }
}
