package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import sp.data.entities.*;
import sp.data.services.interfaces.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class SpController {

	@Autowired
	Validator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@Autowired
	ProductService productService;

	@Autowired
	ClientService clientService;

	@Autowired
	SpService spService;

	@Autowired
	RefererService refererService;

	@Autowired
	OrderStatusService orderStatusService;

	@Autowired
	OrderService orderService;


	// Переход на страницу СП
	@RequestMapping(value="/sp/{spId}")
	public String spPage(@PathVariable Integer spId,
						 @ModelAttribute("order") Order order,
						 @RequestParam(name = "newClientName", required = false) String newClientName,
						 Model model) {

		Sp sp = spService.getByIdWithAllChildren(spId);
		model.addAttribute("sp", sp);

		if (newClientName != null){
			model.addAttribute("newClientName", newClientName);
		}

		return "sp";
	}


	// Создание заказа
	@RequestMapping(value="/sp/{spId}", params = {"action=addOrder"})
	public String addOrder(@Valid @ModelAttribute Order order,
						   Errors errors,
						   Model model) {

		System.out.println("Inside addOrder() method");

		if (errors.hasErrors()){
			Sp sp = spService.getByIdWithAllChildren(order.getSp().getId());
			model.addAttribute("sp", sp);
			return "sp";
		}

		orderService.save(order);
		return "redirect:/sp/" + order.getSp().getId();
	}


	// Autocomplete выбора продукта
	@RequestMapping(value = "/getProducts", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> getProducts(@RequestParam("query") String productName) {
		return productService.searchByName(productName);
	}

	// Autocomplete выбора клиента
	@RequestMapping(value = "/getClients", method = RequestMethod.GET)
	@ResponseBody
	public List<Client> getClients(@RequestParam("query") String clientName) {
		List<Client> clients = clientService.searchByName(clientName);
		System.out.println(clients.size());
		return clients;
	}

	// Создание заказа от нового клиента
	@RequestMapping(value = "/addorder", params = {"newClient"})
	public String addOrderFromNewClient(@ModelAttribute Client client, @RequestParam("spId") int spId, Model model) {
		System.out.println("Inside addOrderFromNewClient() method");
		clientService.save(client);
		Sp sp = spService.getById(spId);

		Order order = new Order();
		order.setClient(client);
		order.setSp(sp);
		order.setOrderStatus(orderStatusService.getById(1));
		order.setDateOrdered(new Date());

		orderService.save(order);

		return "redirect:/sp/" + sp.getNumber();
	}


	/*
	// Создание заказа
	@RequestMapping(value = "/addorder")
	public String addOrder(@RequestParam("spId") int spId, @RequestParam("name") String clientName, Model model) {
		System.out.println("Inside addOrder() method");
		Sp sp = spService.getById(spId);

		Order order = new Order();
		order.setClient(clientService.getByName(clientName));
		order.setSp(sp);
		order.setOrderStatus(orderStatusService.getById(1));
		order.setDateOrdered(new Date());

		orderService.save(order);

		return "redirect:/sp/" + sp.getNumber();
	}
	*/













	// Autocomplete
	/*
	@RequestMapping(value = "/addorder", params = {"newClient"})
	public String addOrder(Model model, @RequestParam("spId") Integer spId,
						   				@RequestParam("name") String name,
					  				    @RequestParam(value = "newClient", defaultValue = "off") String newClient,
									    @RequestParam("realName") String realName,
									    @RequestParam("phone") String phone,
									    @RequestParam("note") String note,
						   				@RequestParam("referer") Integer refererId) {
		Client client;

		if (newClient == "on"){
			client = new Client();
			client.setName(name);
			client.setRealName(realName);
			client.setPhone(Long.parseLong(phone));
			client.setNote(note);
			client.setReferer(refererService.getById(refererId));

		} else {
			client = clientService.getByName(name);
		}

		Sp sp = spService.getByNumber(spId);
		model.addAttribute("sp", sp);

		Order order = new Order();
		order.setClient(client);
		order.setSp(sp);
		order.setOrderStatus(orderStatusService.getById(1));
		order.setDateOrdered(new Date());

		orderService.save(order);

		System.out.println("inside addOrder() method");

		return "redirect:/sp/" + sp.getNumber();
	}
	*/

}
