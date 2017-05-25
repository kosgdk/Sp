package sp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.entities.Properties;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.OrderService;
import sp.data.services.interfaces.PropertiesService;
import sp.data.services.interfaces.SpService;
import sp.data.validators.OrderValidator;

import javax.validation.Valid;
import java.util.*;

import static sp.data.utilities.FieldErrorsLogger.logErrors;

@Controller
@SessionAttributes(value = {"properties"})
public class OrderController {

	Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired SpService spService;
	@Autowired OrderService orderService;
	@Autowired PropertiesService propertiesService;

	@Autowired Validator validator;
	@Autowired OrderValidator orderValidator;

	@Autowired private MessageSource messageSource;

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
		logger.debug("Controller method: pageOrder()");

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
							  @Valid @ModelAttribute Order order,
							  Errors errors,
							  RedirectAttributes redirectAttributes,
							  Locale locale) {
		logger.debug("Controller method: createOrder()");

		Sp sp = spService.getById(spId);

		// Проверка возможности создания заказа в зависимости от статуса СП
		if (!sp.getStatus().equals(SpStatus.COLLECTING) && !sp.getStatus().equals(SpStatus.CHECKOUT)) {
			String errorCode = "sp.incompatibleStatusForOrderCreation";
			String errorMessage = messageSource.getMessage(errorCode, null, null, locale);
			logger.debug("Can't create new order: " + errorCode);
			redirectAttributes.addFlashAttribute("errorMessages", new ArrayList<>(Collections.singletonList(errorMessage)));
			return "redirect:/sp/" + spId;
		}

		if (errors.hasErrors()){
			logErrors(logger, errors);
			redirectAttributes.addFlashAttribute("order", order);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.order", errors);
			return "redirect:/sp/" + spId;
		}

		orderService.save(order);
		sp.getOrders().add(order);

		return "redirect:/order/" + order.getId();
	}

	// Обработка запроса на удаление заказа
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=delete"})
	public String deleteOrder(@PathVariable("orderId") Long orderId,
							  @RequestParam(name = "from", required = false) Long spId){
		logger.debug("Controller method: deleteOrder()");
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
	public String updateOrder(@ModelAttribute Order order,
							  Errors errors,
							  @PathVariable("orderId") Long orderId,
							  @RequestParam("goback") boolean goBack,
							  RedirectAttributes redirectAttributes){
		logger.debug("Controller method: updateOrder()");
		// TODO: 15.03.2017 Check if status of Order is suitable for editing some of Order fields

		Order persistedOrder = orderService.getById(orderId);
		order.setOrderPositions(persistedOrder.getOrderPositions());
		order.setClient(persistedOrder.getClient());
		order.setStatus(persistedOrder.getStatus());

		validator.validate(order, errors);
		orderValidator.validate(order, errors);
		if (errors.hasErrors()){
			logErrors(logger, errors);
			redirectAttributes.addFlashAttribute("order", order);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.order", errors);
			return "redirect:/order/" + order.getId();
		}

		orderService.processStatus(order);
		orderService.processSpStatus(order);
		orderService.update(order);


		return goBack ? "redirect:/sp/" + order.getSp().getId() : "redirect:/order/" + orderId;
	}

	// Обработка запроса на смену статуса заказа
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=change_status"},
					method = RequestMethod.GET)
	public String changeOrderStatus(@PathVariable("orderId") Long orderId,
							  		RedirectAttributes redirectAttributes,
									Locale locale){

		logger.debug("Controller method: changeOrderStatus()");

		Order order = orderService.getById(orderId);
		OrderStatus orderStatus = order.getStatus();
		if (!orderStatus.equals(OrderStatus.COMPLETED)) order.setStatus(OrderStatus.getById(orderStatus.getId() + 1));
		else return "redirect:/order/" + orderId;

		Errors errors = new BeanPropertyBindingResult(order, "order");
		orderValidator.validate(order, errors);
		if (errors.hasFieldErrors("status")){
			logErrors(logger, errors);
			String errorMessage = messageSource.getMessage(errors.getFieldError("status").getCode(), null, null, locale);
			redirectAttributes.addFlashAttribute("statusError", errorMessage);
			return "redirect:/order/" + orderId;
		}

		orderService.update(order);
		orderService.processSpStatus(order);

		return "redirect:/order/" + orderId;
	}

}