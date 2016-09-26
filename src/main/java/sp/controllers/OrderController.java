package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import sp.data.entities.*;
import sp.data.services.interfaces.*;

import java.util.List;

@Controller
@SessionAttributes("properties")
public class OrderController {

	@Autowired
	PropertiesService propertiesService;

	@ModelAttribute("properties")
	public Properties getProperties(){
		return propertiesService.getProperties();
	}

	@Autowired
	Validator validator;

	@Autowired
	OrderService orderService;

	@Autowired
	ClientService clientService;

	@Autowired
	ProductService productService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Converts empty strings into null when a form is submitted
	}


	@RequestMapping("/order/{orderId}")
	public String orderPage(@PathVariable("orderId") Integer orderId,
							@ModelAttribute("orderPosition") OrderPosition orderPosition,
							Model model){

		Order order = orderService.getById(orderId);
		model.addAttribute("order", order);
		return "order";
	}

	// Autocomplete выбора продукта
	@RequestMapping(value = "/getProducts", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> getProducts(@RequestParam("query") String productName) {
		return productService.searchByName(productName);
	}

}