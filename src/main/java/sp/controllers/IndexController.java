package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sp.data.services.interfaces.SpService;

@Controller
public class IndexController {

	@Autowired
	SpService spService;

	
	// Index
	@RequestMapping("/")
	public String pageIndex(Model model){
		model.addAttribute("sp", spService.getLastSp());
		return "index";
	}

}
