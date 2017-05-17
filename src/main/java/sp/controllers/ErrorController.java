package sp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

	// Переход на страницу ошибки
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String pageError(){
		return "error";
	}

}