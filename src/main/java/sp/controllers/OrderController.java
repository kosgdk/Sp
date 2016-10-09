package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import sp.data.entities.*;
import sp.data.entities.Properties;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.services.interfaces.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@SessionAttributes(value = {"properties", "orderStatuses"})
public class OrderController {

	@Autowired
	PropertiesService propertiesService;

	@ModelAttribute("properties")
	public Properties getProperties(){
		return propertiesService.getProperties();
	}

	@ModelAttribute("orderStatuses")
	public List<OrderStatus> getOrderStatuses(){
		return Arrays.asList(OrderStatus.values());
	}

	@Autowired
	Validator validator;

	@Autowired
	OrderService orderService;

	@Autowired
	ClientService clientService;

	@Autowired
	ProductService productService;

	@Autowired
	OrderPositionService orderPositionService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Converts empty strings into null when a form is submitted
	}


	// Переход на страницу заказа
	@RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
	public String orderPage(@PathVariable("orderId") Integer orderId,
							@ModelAttribute("orderPosition") OrderPosition orderPosition,
							Model model){

		System.out.println("inside orderPage()");

		Order order = orderService.getByIdWithAllChildren(orderId);
		model.addAttribute("order", order);
		return "order";
	}


	// Autocomplete выбора продукта
	@RequestMapping(value = "/getProducts", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> getProducts(@RequestParam("query") String productName) {
		return productService.searchByName(productName);
	}


	// Обработка запроса на добавление позиции
	@RequestMapping(value = "/order/{orderId}", params = {"action=add_position"})
	public String createOrderPosition(@Valid @ModelAttribute OrderPosition orderPosition,
									  Errors errors, Model model){

		System.out.println("inside createOrderPosition()");

		if (errors.hasErrors()){
			Order order = orderService.getById(orderPosition.getOrder().getId());
			model.addAttribute("order", order);
			return "order";
		}

		orderPositionService.save(orderPosition);
		return "redirect:/order/" + orderPosition.getOrder().getId();
	}


	// Обработка запроса на удаление позиции
	@RequestMapping(value = "/order_position/{orderPositionId}", params = {"action=delete", "from_order"})
	public String deleteOrderPosition(@PathVariable("orderPositionId") Integer orderPositionId,
									  @RequestParam("from_order") Integer orderId){
		System.out.println("inside deleteOrderPosition()");
		orderPositionService.deleteById(orderPositionId);
		return "redirect:/order/" + orderId;
	}


	// Обработка запроса на обновление заказа
	@RequestMapping(value = "/order/{orderId}", params = {"action=update"})
	public String updateOrder(@Valid @ModelAttribute Order order, Errors errors, @ModelAttribute("orderPosition") OrderPosition orderPosition, Model model){

		System.out.println("inside updateOrder()");

		if (errors.hasErrors()){
			return "order";
		}

		orderService.update(order);
		return "redirect:/order/" + order.getId();
	}


}