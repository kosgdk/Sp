package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sp.data.entities.*;
import sp.data.services.ClientServiceImpl;
import sp.data.services.RefererServiceImpl;

import java.util.List;

@Controller
public class ClientController {

	@Autowired
	RefererServiceImpl refererService;

	@Autowired
	ClientServiceImpl clientService;

	Client client1 = new Client();


	// Переход на страницу клиента
	@RequestMapping("/client/{clientId}")
	public String clientPage(@PathVariable("clientId") int id, Model model){
		System.out.println("asdasdasd");
		Client client = clientService.getById(id);
		System.out.println("Referer = " + client.getReferer().getName());

		List<Referer> referers = refererService.getAll();
		model.addAttribute("client", client);
		model.addAttribute("referers", referers);
		return "client";
	}

	// Переход на страницу добавления клиента
	@RequestMapping("/createclient")
	public String saveClientPage(Model model){
		List<Referer> referers = refererService.getAll();
		model.addAttribute("referers", referers);
		return "createclient";
	}

	// Обработка запроса на создание клиента
	@RequestMapping(value="/createclient", method = RequestMethod.POST)
	public String saveClient(@ModelAttribute Client client, Model model){
		clientService.save(client);
		return "redirect:/client/" + client.getId();
	}

	// Обработка запроса на обновление клиента
	@RequestMapping(value="/updateclient", method = RequestMethod.POST)
	public String updateClient(@ModelAttribute Client client, Model model){
		System.out.println(client.toString());
		clientService.update(client);
		return "redirect:/client/" + client.getId();
	}

}
