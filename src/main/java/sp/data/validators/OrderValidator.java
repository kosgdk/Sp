package sp.data.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.controllers.OrderController;
import sp.data.entities.Order;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component("OrderValidator")
public class OrderValidator implements Validator {

    @Autowired
    OrderService orderService;

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Override
    public boolean supports(Class<?> clazz) {
        return Order.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Order order = (Order) target;

        validateSp(order, errors);
        validatePrepaid(order, errors);
        validateStatus(order, errors);
    }

    public void validateSp(Order order, Errors errors) {
        System.out.println("inside validateSp()");
        Order persistedOrder = orderService.getById(order.getId());
        if (!order.getSp().getId().equals(persistedOrder.getSp().getId())) {
            List<SpStatus> allowedSpStatuses = Arrays.asList(SpStatus.COLLECTING, SpStatus.CHECKOUT);

            if (!allowedSpStatuses.contains(order.getSp().getStatus())) {
                errors.rejectValue("sp", "order.sp.notAllowed");
            }
        }
    }

    public void validateStatus(Order order, Errors errors) {
        System.out.println("inside validateStatus()");

        SpStatus spStatus = order.getSp().getStatus();
        switch (order.getStatus()){
            case UNPAID:
                System.out.println("case UNPAID");
                if (spStatus != SpStatus.COLLECTING && spStatus != SpStatus.CHECKOUT) {
                    errors.rejectValue("status", "order.status.incompatibleSpStatusForUnpaidOrderStatus");
                }
                break;

            case PAID:
                System.out.println("case PAID");
                if (spStatus != SpStatus.CHECKOUT) {
                    errors.rejectValue("status", "order.status.incompatibleSpStatusForPaidOrderStatus");
                }
                break;

            case PACKING:
                System.out.println("case PACKING");
                if (spStatus != SpStatus.PACKING) {
                    errors.rejectValue("status", "order.status.incompatibleSpStatusForPackingOrderStatus");
                }
                break;

            case SENT:
                System.out.println("case SENT");
                if (spStatus != SpStatus.SENT) {
                    errors.rejectValue("status", "order.status.incompatibleSpStatusForSentOrderStatus");
                }
                break;

            case ARRIVED:
                System.out.println("case ARRIVED");
                if (spStatus != SpStatus.ARRIVED) {
                    errors.rejectValue("status", "order.status.incompatibleSpStatusForArrivedOrderStatus");
                }
                break;

            case COMPLETED:
                System.out.println("case COMPLETED");
                if (spStatus != SpStatus.DISTRIBUTING && spStatus != SpStatus.COMPLETED) {
                    errors.rejectValue("status", "order.status.incompatibleSpStatusForCompletedOrderStatus");
                }
                break;
        }
    }

    public void validatePrepaid(Order order, Errors errors){
        if (order.getPrepaid().compareTo(BigDecimal.ZERO) > 0 && !order.getSp().getStatus().equals(SpStatus.CHECKOUT)) {
            errors.rejectValue("prepaid", "order.status.incompatibleSpStatusForSettingPrepaidField");
        }
    }

}
