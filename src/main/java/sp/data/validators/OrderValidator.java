package sp.data.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.Client;
import sp.data.entities.Order;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.ClientService;
import sp.data.services.interfaces.OrderService;

import java.util.*;


@Component("OrderValidator")
public class OrderValidator implements Validator {

    @Autowired
    OrderService orderService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Order.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        System.out.println("inside OrderValidator");

        Order order = (Order) target;

        // Sp change validation
        Order persistedOrder = orderService.getById(order.getId());
        if (order.getSp().getId() != persistedOrder.getSp().getId()) {
            List<SpStatus> spStatusCheck = Arrays.asList(SpStatus.COLLECTING, SpStatus.CHECKOUT);
            if (!spStatusCheck.contains(order.getSp().getStatus())) {
                errors.rejectValue("sp", "order.sp.notAllowed");
            }
        }

    }
}
