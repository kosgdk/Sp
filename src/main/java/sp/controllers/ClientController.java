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
import sp.data.entities.*;
import sp.data.services.interfaces.ClientService;
import sp.data.services.interfaces.RefererService;

import javax.validation.Valid;
import java.util.List;

@Controller
@SessionAttributes("referers")
public class ClientController {

	@ModelAttribute("referers")
	public List<Referer> getReferers(){
		return refererService.getAll();
	}

	@Autowired
	Validator validator;

	@Autowired
	RefererService refererService;

	@Autowired
	ClientService clientService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Converts empty strings into null when a form is submitted
	}


	// Переход на страницу создания клиента
	@RequestMapping(value = "/create_client", method = RequestMethod.GET)
	public String createClientPage(@ModelAttribute("client") Client client,
								   @RequestParam(name = "sp", required = false) Integer spId,
								   Model model){

		model.addAttribute("spId", spId);
		model.addAttribute("action", "create");
		return "client";
	}


	// Обработка запроса на создание клиента
	@RequestMapping(value="/create_client", method = RequestMethod.POST)
	public String saveClient(@Valid @ModelAttribute Client client,
							 Errors errors,
							 @RequestParam(name = "spId", required = false) Integer spId,
							 Model model,
							 RedirectAttributes redirectAttributes){

		if (errors.hasErrors()){
			model.addAttribute("spId", spId);
			model.addAttribute("action", "create");
			return "/client";
		}

		clientService.save(client);

		if (spId != null){
			redirectAttributes.addAttribute("newClientName", client.getName());
			return "redirect:/sp/" + spId;
		}

		return "redirect:/client/" + client.getId();
	}


	// Переход на страницу профиля клиента
	@RequestMapping("/client/{clientId}")
	public String clientProfilePage(@PathVariable("clientId") Integer clientId,
								   Model model){

		Client client = clientService.getByIdWithAllChildren(clientId);
		model.addAttribute("client", client);
		model.addAttribute("action", "profile");
		return "client";
	}


	// Обработка запроса на обновление клиента
	@RequestMapping(value="/client/*", method = RequestMethod.POST)
	public String updateClient(@Valid @ModelAttribute Client client,
							   Errors errors,
							   Model model){

		if (errors.hasErrors()){
			model.addAttribute("action", "profile");
			return "client";
		}

		clientService.update(client);
		return "redirect:/client/" + client.getId();
	}

}