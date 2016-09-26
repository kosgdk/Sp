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

		if (sp == null){
			model.addAttribute("message", "СП не найдено.");
			return "404";
		}

		model.addAttribute("sp", sp);

		if (newClientName != null){
			model.addAttribute("newClientName", newClientName);
		}

		return "sp";
	}


	// Создание заказа
	@RequestMapping(value="/sp/*", params = {"action=addOrder"})
	public String addOrder(@Valid @ModelAttribute Order order,
						   Errors errors,
						   Model model) {

		if (errors.hasErrors()){
			Sp sp = spService.getByIdWithAllChildren(order.getSp().getId());
			model.addAttribute("sp", sp);
			return "sp";
		}

		orderService.save(order);
		return "redirect:/sp/" + order.getSp().getId();
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
	public String addOrderFromNewClient(@ModelAttribute Client client, @RequestParam("spId") int spId) {

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


}
