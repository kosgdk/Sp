package sp.controllers;

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
import sp.data.entities.Client;
import sp.data.entities.Order;
import sp.data.entities.Sp;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.interfaces.*;
import sp.data.validators.SpValidator;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
@SessionAttributes(value = {"spStatuses"})
public class SpController {

	@Autowired SpService spService;
	@Autowired OrderService orderService;
	@Autowired ClientService clientService;
	@Autowired ProductService productService;
	@Autowired OrderPositionService orderPositionService;

	@Autowired Validator validator;
	@Autowired SpValidator spValidator;

	@Autowired private MessageSource messageSource;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Converts empty strings into null when a form is submitted
	}

	@ModelAttribute("spStatuses")
	public List<SpStatus> getSpStatusesEnum(){
		return Arrays.asList(SpStatus.values());
	}

	// Переход на страницу создания СП
	@RequestMapping("/createsp")
	public String pageCreateSp(Model model){
		System.out.println("Inside pageCreateSp()");

		Long nextSpNumber = spService.getLastNumber() + 1;
		System.out.println(nextSpNumber);
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
		return "redirect:/sp/" + sp.getId();
	}

	// Переход на страницу СП
	@RequestMapping(value="/sp/{spId}", method = RequestMethod.GET)
	public String pageSp(@PathVariable Long spId,
						 @RequestParam(name = "newClientName", required = false) String newClientName,
						 Model model) {
		System.out.println("inside spPage()");

		if(!model.containsAttribute("sp")){
			Sp sp = spService.getByIdWithAllChildren(spId);
			if (sp == null){
				model.addAttribute("message", "СП не найдено.");
				return "error";
			}
			model.addAttribute("sp", sp);
		}

		if(!model.containsAttribute("order")){
			model.addAttribute("order", new Order());
		}

		model.addAttribute("SpStatuses", Arrays.asList(SpStatus.values()));

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

		System.out.println("spStatus = " + sp.getStatus());

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

	// Обработка запроса на смену статуса СП
	@RequestMapping(value="/sp/{spId}", params = {"action=change_status"}, method = RequestMethod.GET)
	public String changeSpStatus(@PathVariable("spId") Long spId,
								 RedirectAttributes redirectAttributes,
								 Locale locale) {
		System.out.println("Inside changeSpStatus()");

		Sp sp = spService.getById(spId);

		SpStatus spStatus = sp.getStatus();
		if (!spStatus.equals(SpStatus.COMPLETED)) sp.setStatus(SpStatus.getById(spStatus.getId() + 1));
		else return "redirect:/sp/" + spId;

		Errors errors = new BeanPropertyBindingResult(sp, "sp");
		spValidator.validateSpStatus(sp, errors);

		if (errors.hasFieldErrors("status")){
//			redirectAttributes.addFlashAttribute("sp", sp);
			String errorMessage = messageSource.getMessage(errors.getFieldError("status").getCode(), null, null, locale);
			redirectAttributes.addFlashAttribute("statusError", errorMessage);
			return "redirect:/sp/" + spId;
		}

		spService.update(sp);
		spService.processOrdersStatuses(sp);

		return "redirect:/sp/" + spId;
	}

	// Autocomplete выбора клиента
	@RequestMapping(value = "/getClients", method = RequestMethod.GET)
	@ResponseBody
	public List<Client> getClients(@RequestParam("query") String clientName) {
		return clientService.searchByName(clientName);
	}

}