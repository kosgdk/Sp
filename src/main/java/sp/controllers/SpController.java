package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sp.data.entities.Client;
import sp.data.entities.Product;
import sp.data.services.ClientServiceImpl;
import sp.data.services.ProductServiceImpl;

import java.util.List;

@Controller
public class SpController {

	@Autowired
	ProductServiceImpl productService;

	@Autowired
	ClientServiceImpl clientService;


	@RequestMapping(value = "/getProducts", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> getProducts(@RequestParam("query") String productName) {
		return productService.searchByName(productName);
	}

	@RequestMapping(value = "/getClients", method = RequestMethod.GET)
	@ResponseBody
	public List<Client> getClients(@RequestParam("query") String clientName) {
		List<Client> clients = clientService.searchByName(clientName);
		System.out.println(clients.size());
		return clients;
	}


}
