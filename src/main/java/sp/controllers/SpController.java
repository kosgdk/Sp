package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.data.entities.Client;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.*;
import sp.data.validators.SpValidator;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
@SessionAttributes(value = {"spStatuses"})
public class SpController {

	@ModelAttribute("spStatuses")
	public List<SpStatus> getSpStatusesEnum(){
		return Arrays.asList(SpStatus.values());
	}

	@Autowired
	Validator validator;

	@Autowired
	SpValidator spValidator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Converts empty strings into null when a form is submitted
	}

	@Autowired
	ProductService productService;

	@Autowired
	ClientService clientService;

	@Autowired
	SpService spService;

	@Autowired
	OrderService orderService;

	@Autowired
	OrderPositionService orderPositionService;


	// Переход на страницу создания СП
	@RequestMapping("/createsp")
	public String pageCreateSp(Model model){
		System.out.println("Inside pageCreateSp()");

		Long nextSpNumber = spService.getLastNumber() + 1;
		model.addAttribute("nextSpNumber", nextSpNumber);
		return "createsp";
	}

	// Обработка запроса на создание СП
	@RequestMapping(value="/savesp")
	public String createSp(@ModelAttribute Sp sp, Model model){
		System.out.println("Inside createSp()");

		sp.setStatus(SpStatus.COLLECTING);
		spService.save(sp);
		model.addAttribute("sp", sp);
		return "forward:/sp/" + sp.getId();
	}

	// Переход на страницу СП
	@RequestMapping(value="/sp/{spId}")
	public String pageSp(@PathVariable Long spId,
						 @RequestParam(name = "newClientName", required = false) String newClientName,
						 Model model) {
		System.out.println("inside spPage()");

		if(!model.containsAttribute("sp")){
			Sp sp = spService.getByIdWithAllChildren(spId);
			if (sp == null){
				model.addAttribute("message", "СП не найдено.");
				return "404";
			}
			model.addAttribute("sp", sp);
		}
		Sp sp = (Sp) model.asMap().get("sp");

		if(!model.containsAttribute("order")){
			model.addAttribute("order", new Order());
		}

		model.addAttribute("SpStatuses", Arrays.asList(SpStatus.values()));
		model.addAttribute("currentSpStatus", sp.getStatus());

		if (newClientName != null){
			model.addAttribute("newClientName", newClientName);
		}

		return "sp";
	}

	// Обработка запроса на обновление СП
	@RequestMapping(value="/sp/{spId}", params = {"action=edit_sp"}, method = RequestMethod.POST)
	public String updateSp(@PathVariable("spId") Long spId,
						   @Valid @ModelAttribute Sp sp, Errors errors,
						   RedirectAttributes redirectAttributes) {
        System.out.println("Inside updateSp()");

		sp.setOrders(spService.getById(spId).getOrders());

		spValidator.validate(sp, errors);
		if (errors.hasErrors()){
			redirectAttributes.addFlashAttribute("sp", sp);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.sp", errors);
			return "redirect:/sp/" + spId;
		}

        spService.update(sp);
        spService.processOrdersStatuses(sp);

		return "redirect:/sp/" + spId;
	}

	// Обработка запроса на создание заказа
	@RequestMapping(value="/sp/{spId}", params = {"action=add_order"}, method = RequestMethod.POST)
	public String createOrder(@PathVariable("spId") Integer spId,
							  @Valid @ModelAttribute Order order, Errors errors,
							  RedirectAttributes redirectAttributes) {
		System.out.println("inside addOrder()");

		if (errors.hasErrors()){
			redirectAttributes.addFlashAttribute("order", order);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.order", errors);
			return "redirect:/sp/" + spId;
		}

		orderService.save(order);
		return "redirect:/order/" + order.getId();
	}


	// Autocomplete выбора клиента
	@RequestMapping(value = "/getClients", method = RequestMethod.GET)
	@ResponseBody
	public List<Client> getClients(@RequestParam("query") String clientName) {
		return clientService.searchByName(clientName);
	}

}