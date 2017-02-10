package sp.data.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;

import java.util.ArrayList;
import java.util.Set;


@Component("SpValidator")
public class SpValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return Sp.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        System.out.println("inside SpValidator");

        Sp sp = (Sp) target;
        Set<Order> orders = sp.getOrders();
        ArrayList<OrderStatus> ordersStatuses = new ArrayList<>();

        for (Order order : orders) {
            ordersStatuses.add(order.getStatus());
        }

        // Status validation

        switch (sp.getStatus()){
            case COLLECTING: //Сбор
                
                break;
            case CHECKOUT: //Оплата

                break;

            case PACKING: //Комплектуется
                System.out.println("case (Комплектуется)");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.PAID){
                        errors.rejectValue("status", "sp.status.notAllOrdersPaid", "есть неоплаченные заказы");
                        break;
                    }
                }
                break;

            case PAID: //Оплачен
                System.out.println("case (Оплачен)");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.PAID){
                        errors.rejectValue("status", "sp.status.notAllOrdersPaid", "есть неоплаченные заказы");
                        break;
                    }
                }
                break;

            case SENT: //Отправлен
                System.out.println("case (Отправлен)");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.PACKING){
                        errors.rejectValue("status", "sp.status.notAllOrdersPacked", "не все заказы были скомплектованы");
                        break;
                    }
                }
                break;

            case ARRIVED: //Получен
                System.out.println("case (Получен)");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.SENT){
                        errors.rejectValue("status", "sp.status.notAllOrdersSent", "есть неотправленные заказы");
                        break;
                    }
                }
                break;

            case DISTRIBUTING: //Раздаётся
                System.out.println("case (Раздаётся)");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.ARRIVED){
                        errors.rejectValue("status", "sp.status.notAllOrdersArrived", "не все заказы приехали");
                        break;
                    }
                }
                break;

            case COMPLETED: //Завершён
                System.out.println("case (Завершён)");
                for (OrderStatus orderStatus : ordersStatuses) {
                    if (orderStatus != OrderStatus.COMPLETED){
                        errors.rejectValue("status", "sp.status.notAllOrdersCompleted", "есть незакрытые заказы");
                        break;
                    }
                }
                break;
        }



    }

}
