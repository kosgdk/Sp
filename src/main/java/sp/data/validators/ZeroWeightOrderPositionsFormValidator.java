package sp.data.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.OrderPosition;
import sp.data.entities.formscontainers.ZeroWeightOrderPositionsForm;

import java.util.List;

@Component("ZeroWeightProductsFormValidator")
public class ZeroWeightOrderPositionsFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ZeroWeightOrderPositionsForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("inside ZeroWeightProductsFormValidator");

        ZeroWeightOrderPositionsForm zeroWeightOrderPositionsForm = (ZeroWeightOrderPositionsForm) target;
        List<OrderPosition> formOrderPositions = zeroWeightOrderPositionsForm.getOrderPositions();
        for (OrderPosition zeroWeightOrderPosition : formOrderPositions) {
            if (zeroWeightOrderPosition.getWeight() < 0) {
                errors.rejectValue("orderPositions[" + formOrderPositions.indexOf(zeroWeightOrderPosition) + "].productWeight", "product.weight.incorrect");
            }
        }
    }

}