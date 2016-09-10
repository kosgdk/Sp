package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sp.data.entities.Product;
import sp.data.services.ProductServiceImpl;
import java.util.List;

@Controller
public class AutocompleteController {

	@Autowired
	ProductServiceImpl productService;


	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET)
	public String getPages() {
		return "autocomplete";
	}

	@RequestMapping(value = "/getProducts", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> getProducts(@RequestParam String productName) {
		return productService.searchByName(productName);
	}


}
