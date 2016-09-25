package sp.data.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.Order;
import sp.data.entities.Referer;


@Component("RefererValidator")
public class RefererValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Referer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Referer referer = (Referer) target;

        if (referer.getName().equals("qwerty")){
            errors.rejectValue("name", "referer.name.incorrect", "Default error message");
        }

    }
}
