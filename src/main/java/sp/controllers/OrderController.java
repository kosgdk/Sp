package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.data.entities.*;
import sp.data.entities.Properties;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.*;
import sp.data.validators.OrderValidator;

import javax.validation.Valid;
import java.util.*;

@Controller
@SessionAttributes(value = {"properties"})
public class OrderController {

	@Autowired Validator validator;
	@Autowired OrderValidator orderValidator;
	@Autowired SpService spService;
	@Autowired OrderService orderService;
	@Autowired ClientService clientService;
	@Autowired ProductService productService;
	@Autowired PropertiesService propertiesService;
	@Autowired OrderPositionService orderPositionService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Converts empty strings into null when a form is submitted
	}

	@ModelAttribute("properties")
	public Properties getProperties(){
		return propertiesService.getProperties();
	}


	// Переход на страницу заказа
	@RequestMapping(value = "/order/{orderId}",
					method = RequestMethod.GET)
	public String pageOrder(@PathVariable("orderId") Long orderId,
							Model model){
		System.out.println("inside pageOrder()");

        model.addAttribute("orderStatuses", Arrays.asList(OrderStatus.values()));

		if(!model.containsAttribute("order")){
			model.addAttribute("order", orderService.getByIdWithAllChildren(orderId));
		}

		List<Long> availableSpIds = spService.getIdsByStatus(SpStatus.COLLECTING, SpStatus.CHECKOUT);

		Order order = (Order) model.asMap().get("order");
		Long spId = order.getSp().getId();

		if (!availableSpIds.contains(spId)) availableSpIds.add(spId);
		model.addAttribute("availableSpIds", availableSpIds);

		if(!model.containsAttribute("orderPosition")){
			model.addAttribute("orderPosition", new OrderPosition());
		}

		return "order";
	}

	// Обработка запроса на создание заказа
	@RequestMapping(value="/sp/{spId}",
					params = {"action=add_order"},
					method = RequestMethod.POST)
	public String createOrder(@PathVariable("spId") Long spId,
							  @Valid @ModelAttribute Order order, Errors errors,
							  RedirectAttributes redirectAttributes) {
		System.out.println("inside addOrder()");

		if (errors.hasErrors()){
			redirectAttributes.addFlashAttribute("order", order);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.order", errors);
			return "redirect:/sp/" + spId;
		}

		orderService.save(order);

		Sp sp = spService.getById(spId);
		sp.getOrders().add(order);

		return "redirect:/order/" + order.getId();
	}

	// Обработка запроса на удаление заказа
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=delete"})
	public String deleteOrder(@PathVariable("orderId") Long orderId,
							  @RequestParam(name = "from", required = false) Long spId){
		System.out.println("inside deleteOrder()");
		// TODO: 15.03.2017 Check if status of Order and Sp is suitable for Order delete

		Order order = orderService.getById(orderId);
		Sp sp = order.getSp();
		if (spId == null) spId = sp.getId();

		orderService.deleteById(orderId);

		return "redirect:/sp/" + spId;
	}


	// Обработка запроса на обновление заказа
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=update"})
	public String updateOrder(@Valid @ModelAttribute Order order,
							  Errors errors,
							  RedirectAttributes redirectAttributes){
		System.out.println("inside updateOrder()");
		// TODO: 15.03.2017 Check if status of Order is suitable for editing some of Order fields

		orderValidator.validate(order, errors);
		if (errors.hasErrors()){
			redirectAttributes.addFlashAttribute("order", order);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.order", errors);
			return "redirect:/order/" + order.getId();
		}

		orderService.processSpStatus(order);
		orderService.update(order);

		return "redirect:/order/" + order.getId();
	}


	// Обработка запроса на добавление позиции
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=add_position"},
					method = RequestMethod.POST)
	public String createOrderPosition(@PathVariable("orderId") Long orderId,
                                      @Valid @ModelAttribute OrderPosition orderPosition,
									  Errors errors,
									  RedirectAttributes redirectAttributes){

		System.out.println("inside createOrderPosition()");
		// TODO: 15.03.2017 Check if status of Order is suitable for OrderPosition add
		// TODO: 21.03.2017 Recalculate deliveryPrice for all orders in Sp if OrderPosition weight changed

		orderPosition.setProductWeight(orderPosition.getProduct().getWeight());

		if (errors.hasErrors()){
			redirectAttributes.addFlashAttribute("orderPosition", orderPosition);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderPosition", errors);
			System.out.println(errors);
			return "redirect:/order/" + orderId;
		}

		Order order = orderService.getById(orderId);
		order.getOrderPositions().add(orderPosition);
		orderPositionService.save(orderPosition);

		// TODO: implement merging of OrderPositions with same Product

		return "redirect:/order/" + orderPosition.getOrder().getId();
	}


	// Обработка запроса на удаление позиции
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=delete_position", "order_position_id"},
					method = RequestMethod.GET)
	public String deleteOrderPosition(@PathVariable("orderId") Long orderId,
									  @RequestParam("order_position_id") Long orderPositionId){

		System.out.println("inside deleteOrderPosition()");
		// TODO: 15.03.2017 Check if status of Order is suitable for OrderPosition delete

		Order order = orderService.getById(orderId);
		List<OrderPosition> orderPositions = order.getOrderPositions();

		orderPositions.remove(orderPositionService.getById(orderPositionId));

//		for (OrderPosition orderPosition : orderPositions) {
//			if (orderPosition.getId().equals(orderPositionId)) {
//				orderPositions.remove(orderPosition);
//				break;
//			}
//		}
//		orderService.update(order);

		orderPositionService.deleteById(orderPositionId);

		return "redirect:/order/" + orderId;
	}

	// TODO: 25.04.2017 Implement batch delete of order_positions


	// Переход на страницу редактирования позиции
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=edit_position", "order_position_id"},
					method = RequestMethod.GET)
	public String editOrderPositionPage(@PathVariable("orderId") Long orderId,
							@RequestParam("order_position_id") Long orderPositionId,
							@RequestParam("action") String action,
							Model model){

		System.out.println("inside editOrderPositionPage()");
		// TODO: 15.03.2017 Check if status of Order is suitable for OrderPosition edit

		Order order = orderService.getByIdWithAllChildren(orderId);

		model.addAttribute("order", order);
		model.addAttribute("orderStatuses", Arrays.asList(OrderStatus.values()));
		model.addAttribute("action", action);

		if(!model.containsAttribute("orderPosition")){
			model.addAttribute("orderPosition", orderPositionService.getById(orderPositionId));
		}
		return "order";
	}


	// Обработка запроса на обновление позиции
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=edit_position"},
					method = RequestMethod.POST)
	public String updateOrderPosition(@PathVariable("orderId") Integer orderId,
									  @Valid @ModelAttribute OrderPosition orderPosition,
									  Errors errors,
									  RedirectAttributes redirectAttributes){

		System.out.println("inside updateOrderPosition()");
		orderPosition.setProductWeight(orderPositionService.getById(orderPosition.getId()).getProductWeight());

		if (errors.hasErrors()){
			System.out.println("ERRORS!");
			List<ObjectError> allErrors = errors.getAllErrors();
			for (ObjectError error : allErrors) {
				System.out.println(error.toString());
			}
			redirectAttributes.addFlashAttribute("orderPosition", orderPosition);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderPosition", errors);
			return "redirect:/order/" + orderId + "?action=edit_position&order_position_id=" + orderPosition.getId();
		}

		System.out.println("no errors.");
		orderPositionService.update(orderPosition);
		return "redirect:/order/" + orderId;
	}


	// Autocomplete выбора продукта
	@RequestMapping(value = "/getProducts",
					method = RequestMethod.GET)
	@ResponseBody
	public List<Product> getProducts(@RequestParam("query") String productName) {
		return productService.searchByName(productName);
	}

}