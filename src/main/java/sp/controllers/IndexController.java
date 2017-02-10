package sp.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import sp.data.entities.*;
import sp.data.entities.enumerators.SpStatus;
import sp.data.services.ClientServiceImpl;
import sp.data.services.ProductServiceImpl;
import sp.data.services.SpServiceImpl;
import sp.data.services.interfaces.ClientService;
import sp.data.services.interfaces.ProductService;
import sp.data.services.interfaces.SpService;

@Controller
public class IndexController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	SpService spService;

	@Autowired
	ClientService clientService;
	
	
	// Index
	@RequestMapping("/")
	public String index(Model model){
		model.addAttribute("sp", spService.getLastSp());
		return "index";
	}
	
	// Переход на страницу создания СП
	@RequestMapping("/createsp")
	public String createSp(Model model){
		Long nextSpNumber = spService.getLastNumber() + 1;
		model.addAttribute("nextSpNumber", nextSpNumber);
		return "createsp";
	}
	
	// Обработчик создания СП
	@RequestMapping(value="/savesp")
	public String sp(@ModelAttribute Sp sp, Model model){
		sp.setStatus(SpStatus.COLLECTING);
		spService.save(sp);
		model.addAttribute("sp", sp);
		return "forward:/sp/" + sp.getNumber();
	}
	
	@RequestMapping("/addposition")
	public String addPosition(){
		return "addposition";
	}

	@RequestMapping(value="/addposition", params={"query"})
	public String searchPosition(@RequestParam("query") String query, Model model){
		List<Product> products = productService.searchByName(query);
		model.addAttribute("query", query);
		model.addAttribute("products", products);
		System.out.println(query);
		return "selectproduct";
	}

	@RequestMapping(value="/addposition", params={"productid"})
	public String searchPosition(@RequestParam("productid") Long id, Model model){
		System.out.println(id);
		OrderPosition orderPosition = new OrderPosition();
		orderPosition.setProduct(productService.getById(id));
		model.addAttribute("orderPosition", orderPosition);
		return "addposition";
	}

	
}
