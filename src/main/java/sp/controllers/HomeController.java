package sp.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import sp.data.dao.interfaces.ProductDao;
import sp.data.dao.interfaces.SpStatusDao;
import sp.data.entities.OrderPosition;
import sp.data.entities.Product;
import sp.data.entities.Referer;
import sp.data.entities.Sp;
import sp.data.entities.SpStatus;
import sp.data.services.ProductServiceImpl;
import sp.data.services.RefererServiceImpl;
import sp.data.services.SpServiceImpl;
import sp.data.services.interfaces.ProductService;
import sp.data.services.interfaces.RefererService;

@Controller
public class HomeController {
	
	@Autowired
	ProductServiceImpl productService;
	
	@Autowired
	SpServiceImpl spService;
	
	@Autowired
	SpStatusDao spStatusDao;
	
	@Autowired
	RefererServiceImpl refererService;
	
	
	// Index
	@RequestMapping("/")
	public String goHome(Model model){
		model.addAttribute("sp", spService.getLastSp());
		return "index";
	}
	
	// ������� �� �������� �������� ��
	@RequestMapping("/createsp")
	public String createSp(Model model){
		int nextSpNumber = spService.getLastNumber() + 1;
		model.addAttribute("nextSpNumber", nextSpNumber);
		return "createsp";
	}
	
	// ���������� �������� ��
	@RequestMapping(value="/savesp")
	public String sp(@ModelAttribute Sp sp, Model model){
		sp.setDateToPay(sp.calculateDateToPay(sp.getDateEnd()));
		sp.setStatus(spStatusDao.getById(1));
		spService.save(sp);
		model.addAttribute("sp", sp);
		return "forward:/sp/" + sp.getNumber();
	}
	
	// ������� �� �������� ��
	@RequestMapping(value="/sp/{spNumber}")
	public String sp(Model model, @PathVariable int spNumber){
		model.addAttribute("sp", spService.getByNumber(spNumber));
		return "sp";
	}
	
	
	
	
	// ������� �� �������� ���������� �������
	@RequestMapping("/createclient")
	public String clientPage(Model model){
		List<Referer> referers = refererService.getAll();
		model.addAttribute("referers", referers);
		return "createclient";
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
