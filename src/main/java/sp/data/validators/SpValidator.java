package sp.data.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.OrderPositionService;
import sp.data.services.interfaces.ProductService;
import sp.data.services.interfaces.SpService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Component("SpValidator")
public class SpValidator implements Validator {

    @Autowired SpService spService;
    @Autowired ProductService productService;
    @Autowired OrderPositionService orderPositionService;


    @Override
    public boolean supports(Class<?> clazz) {
        return Sp.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("inside SpValidator");
        Sp sp = (Sp) target;

        validateSpStatus(sp, errors);
        validateDeliveryPrice(sp, errors);

    }

    public void validateSpStatus(Sp sp, Errors errors) {
        System.out.println("validateSpStatus()");
        Set<Order> orders = sp.getOrders();
        ArrayList<OrderStatus> ordersStatuses = new ArrayList<>();
        if (orders != null && !orders.isEmpty()) {
            orders.forEach(order -> ordersStatuses.add(order.getStatus()));
        } else{
            if (sp.getStatus() != SpStatus.COLLECTING)
                errors.rejectValue("status", "sp.status.noOrders");
        }


        switch (sp.getStatus()){
            case COLLECTING: //Сбор
                System.out.println("case: COLLECTING");
                // Все заказы должны быть не оплачены или оплачены
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.UNPAID && orderStatus != OrderStatus.PAID){
                        errors.rejectValue("status", "sp.status.incompatibleOrdersStatusesForCollectingSpStatus");
                        break;
                    }
                }
                break;

            case CHECKOUT: //Оплата
                System.out.println("case: CHECKOUT");
                // Все заказы должны быть не оплачены или оплачены
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.UNPAID && orderStatus != OrderStatus.PAID){
                        errors.rejectValue("status", "sp.status.incompatibleOrdersStatusesForCheckoutSpStatus");
                        break;
                    }
                }
                break;

            case PACKING: //Комплектуется
                System.out.println("case: PACKING");
                // Все заказы должны быть оплачены
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.PAID && orderStatus != OrderStatus.PACKING){
                        errors.rejectValue("status", "sp.status.incompatibleOrdersStatusesForPackingSpStatus");
                        break;
                    }
                }
                break;

            case PAID: //Оплачен
                System.out.println("case: PAID");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.PACKING){
                        errors.rejectValue("status", "sp.status.incompatibleOrdersStatusesForPaidSpStatus");
                        break;
                    }
                }
                break;

            case SENT: //Отправлен
                System.out.println("case: SENT");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.PACKING && orderStatus != OrderStatus.SENT){
                        errors.rejectValue("status", "sp.status.incompatibleOrdersStatusesForSentSpStatus");
                        break;
                    }
                }
                break;

            case ARRIVED: //Получен
                System.out.println("case ARRIVED");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.SENT && orderStatus != OrderStatus.ARRIVED){
                        errors.rejectValue("status", "sp.status.incompatibleOrdersStatusesForArrivedSpStatus");
                        break;
                    }
                }
                break;

            case DISTRIBUTING: //Раздаётся
                System.out.println("case DISTRIBUTING");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.ARRIVED){
                        errors.rejectValue("status", "sp.status.incompatibleOrdersStatusesForDistributingSpStatus");
                        break;
                    }
                }
                if (sp.getDeliveryPrice() == null || sp.getDeliveryPrice().equals(new BigDecimal(0))) {
                    errors.rejectValue("status", "sp.status.noDeliveryPrice");
                    break;
                }
                break;

            case COMPLETED: //Завершён
                System.out.println("case COMPLETED");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.COMPLETED){
                        errors.rejectValue("status", "sp.status.incompatibleOrdersStatusesForCompletedSpStatus");
                        break;
                    }
                }
                break;
        }

    }

    public void validateDeliveryPrice(Sp sp, Errors errors){
        System.out.println("validateDeliveryPrice");
        Sp persistedSp = spService.getById(sp.getId());

        BigDecimal newDeliveryPrice = sp.getDeliveryPrice();
        BigDecimal oldDeliveryPrice = persistedSp.getDeliveryPrice();

        if (Objects.equals(newDeliveryPrice, oldDeliveryPrice)) return;

        if (sp.getStatus() != SpStatus.ARRIVED) {
            errors.rejectValue("deliveryPrice", "sp.deliveryPrice.unsuitableStatus");
            return;
        }

        List<OrderPosition> zeroWeightOrderPositions = orderPositionService.getZeroWeightOrderPositions(sp.getId());
        if (zeroWeightOrderPositions != null && !zeroWeightOrderPositions.isEmpty()){
            errors.rejectValue("deliveryPrice", "sp.deliveryPrice.zeroWeightOrderPositions", new String[]{sp.getId().toString()}, "");
        }

    }

}
