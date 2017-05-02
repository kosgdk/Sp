package sp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sp.data.entities.Product;
import sp.data.entities.formscontainers.ZeroWeightProductsForm;
import sp.data.services.interfaces.ProductService;
import sp.data.validators.ZeroWeightProductsFormValidator;

import java.util.List;
import java.util.ListIterator;

@Controller
public class ProductController {

	// TODO: 02.05.2017 Create @ConstructorAdvice class

	@Autowired
	Validator validator;

	@Autowired
	ZeroWeightProductsFormValidator zeroWeightProductsFormValidator;


	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
		binder.setDisallowedFields("id");
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Converts empty strings into null when a form is submitted
	}

	@Autowired
	ProductService productService;

	// Переход на страницу продуктов с неуказанным весом
	@RequestMapping(value = "/zeroweightproducts")
	public String PageZeroWeightProducts(@RequestParam(name = "sp") Long spId,
										 Model model){

		// TODO: 02.05.2017 Check if Sp with given id exists

		if (!model.containsAttribute("zeroWeightProductsForm")) {
			ZeroWeightProductsForm zeroWeightProductsForm = new ZeroWeightProductsForm(productService.getZeroWeightProducts(spId));
			model.addAttribute("zeroWeightProductsForm", zeroWeightProductsForm);
		}

		model.addAttribute("spId", spId);

		return "zeroweightproducts";
	}

	// Обработка запроса на сохранение нового веса продуктов с неуказанным весом
	@RequestMapping(value = "/zeroweightproducts", params = {"action=save"}, method = RequestMethod.POST)
	public String SaveZeroWeightProducts(@RequestParam(name = "sp") Long spId,
										 @ModelAttribute ZeroWeightProductsForm zeroWeightProductsForm,
										 RedirectAttributes redirectAttributes,
										 Errors errors, Model model){

		// TODO: 02.05.2017 Perform security checks of products list (id, neme, etc.)
		zeroWeightProductsFormValidator.validate(zeroWeightProductsForm, errors);
		List<Product> newZeroWeightProducts = zeroWeightProductsForm.getProducts();

		for (final ListIterator<Product> iterator = newZeroWeightProducts.listIterator(); iterator.hasNext();){
			Product productFromForm = iterator.next();
			Product product = productService.getById(productFromForm.getId());
			product.setWeight(productFromForm.getWeight());
			iterator.set(product);
			if (!errors.hasFieldErrors("products["+ (iterator.nextIndex() - 1) +"].weight") && product.getWeight() > 0) {
				productService.update(product);
				iterator.remove();
			}
		}

		if (errors.hasErrors()) {
			redirectAttributes.addFlashAttribute("zeroWeightProductsForm", zeroWeightProductsForm);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.zeroWeightProductsForm", errors);
		}

		return "redirect:/zeroweightproducts?sp=" + spId;
	}


}