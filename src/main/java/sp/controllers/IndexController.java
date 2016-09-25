package sp.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import sp.data.dao.interfaces.SpStatusDao;
import sp.data.entities.*;
import sp.data.services.ClientServiceImpl;
import sp.data.services.ProductServiceImpl;
import sp.data.services.RefererServiceImpl;
import sp.data.services.SpServiceImpl;

@Controller
public class IndexController {
	
	@Autowired
	ProductServiceImpl productService;
	
	@Autowired
	SpServiceImpl spService;
	
	@Autowired
	SpStatusDao spStatusDao;
	
	@Autowired
	RefererServiceImpl refererService;

	@Autowired
	ClientServiceImpl clientService;
	
	
	// Index
	@RequestMapping("/")
	public String index(Model model){
		model.addAttribute("sp", spService.getLastSp());
		return "index";
	}
	
	// Переход на страницу создания СП
	@RequestMapping("/createsp")
	public String createSp(Model model){
		int nextSpNumber = spService.getLastNumber() + 1;
		model.addAttribute("nextSpNumber", nextSpNumber);
		return "createsp";
	}
	
	// Обработчик создания СП
	@RequestMapping(value="/savesp")
	public String sp(@ModelAttribute Sp sp, Model model){
		sp.setDateToPay(sp.calculateDateToPay(sp.getDateEnd()));
		sp.setStatus(spStatusDao.getById(1));
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
	public String searchPosition(@RequestParam("productid") Integer id, Model model){
		System.out.println(id);
		OrderPosition orderPosition = new OrderPosition();
		orderPosition.setProduct(productService.getById(id));
		model.addAttribute("orderPosition", orderPosition);
		return "addposition";
	}

	
}
