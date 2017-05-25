package sp.data.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.OrderPosition;
import sp.data.services.interfaces.OrderPositionService;
import sp.data.services.interfaces.PropertiesService;

import java.math.BigDecimal;

@Component("OrderPositionValidator")
public class OrderPositionValidator implements Validator {

    @Autowired
    OrderPositionService orderPositionService;

    @Autowired
    PropertiesService propertiesService;


    @Override
    public boolean supports(Class<?> clazz) {
        return OrderPosition.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderPosition newOrderPosition = (OrderPosition) target;
        OrderPosition oldOrderPosition = orderPositionService.getById(newOrderPosition.getId());

        validatePriceOrdered(newOrderPosition, oldOrderPosition, errors);
        validatePriceVendor(newOrderPosition, oldOrderPosition, errors);
        validatePriceSp(newOrderPosition, oldOrderPosition, errors);

    }

    public void validatePriceOrdered(OrderPosition newOrderPosition, OrderPosition oldOrderPosition, Errors errors) {
        BigDecimal newPriceOrdered = newOrderPosition.getPriceOrdered();
        BigDecimal oldPriceOrdered = oldOrderPosition.getPriceOrdered();
        if (!newPriceOrdered.equals(oldPriceOrdered) && !newPriceOrdered.equals(newOrderPosition.getProduct().getPrice())){
            errors.rejectValue("priceOrdered", "orderPosition.priceOrdered.InvalidValue");
        }
    }

    public void validatePriceVendor(OrderPosition newOrderPosition, OrderPosition oldOrderPosition, Errors errors) {
        BigDecimal newPriceVendor = newOrderPosition.getPriceVendor();
        BigDecimal oldPriceVendor = oldOrderPosition.getPriceVendor();
        if (!newPriceVendor.equals(oldPriceVendor)){
            BigDecimal percentDiscount = propertiesService.getProperties().getPercentDiscount();
            BigDecimal calculatedPriceVendor = BigDecimal.ONE.subtract(percentDiscount).multiply(newOrderPosition.getPriceOrdered()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            if (!newPriceVendor.equals(calculatedPriceVendor)) {
                errors.rejectValue("priceVendor", "orderPosition.priceVendor.InvalidValue");
            }
        }
    }

    public void validatePriceSp(OrderPosition newOrderPosition, OrderPosition oldOrderPosition, Errors errors) {
        BigDecimal newPriceSp = newOrderPosition.getPriceSp();
        BigDecimal oldPriceSp = oldOrderPosition.getPriceSp();
        if (!newPriceSp.equals(oldPriceSp)) {
            BigDecimal percentSp = newOrderPosition.getOrder().getSp().getPercent();
            BigDecimal calculatedPriceSp = BigDecimal.ONE.add(percentSp).multiply(newOrderPosition.getPriceOrdered()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
            if (!newPriceSp.equals(calculatedPriceSp)) {
                errors.rejectValue("priceSp", "orderPosition.priceSp.InvalidValue");
            }
        }
    }

}
