package sp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.data.entities.Order;
import sp.data.entities.OrderPosition;
import sp.data.entities.Product;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.services.interfaces.OrderPositionService;
import sp.data.services.interfaces.OrderService;
import sp.data.services.interfaces.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static sp.data.utilities.FieldErrorsLogger.logErrors;

@Controller
public class OrderPositionController {

	Logger logger = LoggerFactory.getLogger(OrderPositionController.class);

	@Autowired OrderService orderService;
	@Autowired ProductService productService;
	@Autowired OrderPositionService orderPositionService;
	@Autowired Validator validator;
	@Autowired private MessageSource messageSource;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Converts empty strings into null when a form is submitted
	}


	// Обработка запроса на добавление позиции
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=add_position"},
					method = RequestMethod.POST)
	public String createOrderPosition(@PathVariable("orderId") Long orderId,
                                      @Valid @ModelAttribute OrderPosition orderPosition,
									  Errors errors,
									  HttpServletRequest request,
									  RedirectAttributes redirectAttributes){
		// TODO: 21.03.2017 Recalculate deliveryPrice for all orders in Sp if OrderPosition weight changed
		// TODO: implement merging of OrderPositions with same Product
		logger.debug("inside createOrderPosition()");

		Order order = orderService.getById(orderId);

		if (order == null) {
			return processNullOrder(orderPosition, request, redirectAttributes);
		}

		if (!order.getStatus().equals(OrderStatus.UNPAID)) {
			createErrorRedirectAttribute("order.incompatibleStatusForOrderPositionCreate", redirectAttributes);
			return "redirect:/order/" + orderId;
		}

		if (errors.hasErrors()){
			logErrors(logger, errors);
			redirectAttributes.addFlashAttribute("orderPosition", orderPosition);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderPosition", errors);
			return "redirect:/order/" + orderId;
		}

		orderPosition.setProductWeight(orderPosition.getProductWeight() / orderPosition.getQuantity());

		order.getOrderPositions().add(orderPosition);
		orderPositionService.save(orderPosition);

		return "redirect:/order/" + orderPosition.getOrder().getId();
	}


	// Обработка запроса на удаление позиции
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=delete_position", "order_position_id"},
					method = RequestMethod.GET)
	public String deleteOrderPosition(@PathVariable("orderId") Long orderId,
									  @RequestParam("order_position_id") Long orderPositionId,
									  HttpServletRequest request,
									  RedirectAttributes redirectAttributes){
		logger.debug("Controller method: deleteOrderPosition()");

		Order order = orderService.getById(orderId);
		OrderPosition orderPosition = orderPositionService.getById(orderPositionId);

		if (order == null) {
			return processNullOrder(orderPosition, request, redirectAttributes);
		}

		//Валидация принадлежности позиции к заказу
		if (order.getOrderPositions().stream().noneMatch(op -> op.getId().equals(orderPositionId))) {
			createErrorRedirectAttribute("orderPosition.orderMismatch", redirectAttributes);
			return "redirect:/order/" + orderId;
		}

		if (!order.getStatus().equals(OrderStatus.UNPAID)) {
			createErrorRedirectAttribute("order.incompatibleStatusForOrderPositionRemoval", redirectAttributes);
			return "redirect:/order/" + orderId;
		}

		order.getOrderPositions().remove(orderPosition);
		orderPositionService.deleteById(orderPositionId);

		return "redirect:/order/" + orderId;
	}

	// TODO: 25.04.2017 Implement batch delete of order_positions


	// Переход на страницу редактирования позиции
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=edit_position", "order_position_id"},
					method = RequestMethod.GET)
	public String pageEditOrderPosition(@PathVariable Long orderId,
										@RequestParam("order_position_id") Long orderPositionId,
										@RequestParam String action,
										HttpServletRequest request,
										RedirectAttributes redirectAttributes){
		logger.debug("Controller method: pageEditOrderPosition()");

		Order order = orderService.getById(orderId);
		OrderPosition orderPosition = orderPositionService.getById(orderPositionId);

		if (order == null) {
			return processNullOrder(orderPosition, request, redirectAttributes);
		}

		if (orderPosition == null) {
			createErrorRedirectAttribute("orderPosition.noSuchOrderPosition", redirectAttributes);
			return "redirect:/order/" + orderId;
		}

		//Валидация принадлежности позиции к заказу
		if (order.getOrderPositions().stream().noneMatch(op -> op.getId().equals(orderPositionId))) {
			createErrorRedirectAttribute("orderPosition.orderMismatch", redirectAttributes);
			return "redirect:/order/" + orderId;
		}

		// Проверка возможности редактирования позиции
		if (!order.getStatus().equals(OrderStatus.UNPAID) && !order.getStatus().equals(OrderStatus.PAID)) {
			createErrorRedirectAttribute("order.incompatibleStatusForOrderPositionEdit", redirectAttributes);
			return "redirect:/order/" + orderId;
		}

		redirectAttributes.addFlashAttribute("action", action);
		redirectAttributes.addFlashAttribute("orderPosition", orderPositionService.getById(orderPositionId));

		return "redirect:/order/" + orderId;
	}


	// Обработка запроса на обновление позиции
	@RequestMapping(value = "/order/{orderId}",
					params = {"action=edit_position"},
					method = RequestMethod.POST)
	public String updateOrderPosition(@PathVariable("orderId") Long orderId,
									  @Valid @ModelAttribute OrderPosition orderPosition,
									  Errors errors,
									  HttpServletRequest request,
									  RedirectAttributes redirectAttributes){
		// TODO: 18.05.2017 - валидация значений полей, рассчитанных в UI
		logger.debug("Controller method: updateOrderPosition()");

		Order order = orderService.getById(orderId);
		OrderPosition persistedOrderPosition = orderPositionService.getById(orderPosition.getId());

		if (order == null) {
			return processNullOrder(persistedOrderPosition, request, redirectAttributes);
		}

		if (persistedOrderPosition == null) {
			createErrorRedirectAttribute("orderPosition.noSuchOrderPosition", redirectAttributes);
			return "redirect:/order/" + orderId;
		}

		//Валидация принадлежности позиции к заказу
		if (order.getOrderPositions().stream().noneMatch(op -> op.getId().equals(orderPosition.getId()))) {
			createErrorRedirectAttribute("orderPosition.orderMismatch", redirectAttributes);
			return "redirect:/order/" + orderId;
		}

		// Проверка возможности редактирования позиции
		if (!order.getStatus().equals(OrderStatus.UNPAID) && !order.getStatus().equals(OrderStatus.PAID)) {
			createErrorRedirectAttribute("order.incompatibleStatusForOrderPositionEdit", redirectAttributes);
			return "redirect:/order/" + orderId;
		}

		orderPosition.setProductWeight(orderPositionService.getById(orderPosition.getId()).getProductWeight());

		if (errors.hasErrors()){
			logErrors(logger, errors);
			redirectAttributes.addFlashAttribute("orderPosition", orderPosition);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderPosition", errors);
			return "redirect:/order/" + orderId + "?action=edit_position&order_position_id=" + orderPosition.getId();
		}

		orderPositionService.update(orderPosition);
		return "redirect:/order/" + orderId;
	}


	// Автозаполнение формы продукта
	@RequestMapping(value = "/getProducts",
					method = RequestMethod.GET)
	@ResponseBody
	public List<Product> getProducts(@RequestParam("query") String productName) {
		return productService.searchByName(productName);
	}


	private void createErrorRedirectAttribute(String errorCode, RedirectAttributes redirectAttributes) {
		String errorMessage = messageSource.getMessage(errorCode, null, null, LocaleContextHolder.getLocale());
		logger.debug("Error: " + errorCode + " - " + errorMessage);
		redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
	}

	private String processNullOrder(OrderPosition orderPosition, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		createErrorRedirectAttribute("order.noSuchOrder", redirectAttributes);
		String referrer = request.getHeader("Referer");
		if (referrer != null) return "redirect:" + referrer;
		if (orderPosition != null) return "redirect:/order/" + orderPosition.getOrder().getId();
		return "redirect:/error";
	}

}