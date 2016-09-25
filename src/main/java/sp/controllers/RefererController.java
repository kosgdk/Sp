package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sp.data.entities.Referer;
import sp.data.services.interfaces.RefererService;
import sp.data.validators.RefererValidator;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RefererController {

	@Autowired
	RefererService refererService;



	@RequestMapping("/addreferer")
	public String addRefererPage(@ModelAttribute("referer") Referer referer){
		return "addreferer";
	}

	@RequestMapping(value = "/addreferer", method = RequestMethod.POST)
	public String addRefererRequest(@Valid @ModelAttribute Referer referer, Errors errors){
		if (errors.hasErrors()){
			return "addreferer";
		}

		refererService.save(referer);
		return "addreferer";
	}



}
