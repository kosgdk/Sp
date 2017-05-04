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
import sp.data.entities.OrderPosition;
import sp.data.entities.Product;
import sp.data.entities.formscontainers.ZeroWeightOrderPositionsForm;
import sp.data.services.interfaces.OrderPositionService;
import sp.data.services.interfaces.ProductService;
import sp.data.validators.ZeroWeightProductsFormValidator;

import java.util.List;
import java.util.ListIterator;

@Controller
public class ProductController {

	// TODO: 02.05.2017 Create @ConstructorAdvice class
	// TODO: 03.05.2017 При изменении веса Product'a обновлять OrderPosition.productWeight в СП со статусом "Сбор" и "Оплата"

	@Autowired ProductService productService;
	@Autowired OrderPositionService orderPositionService;
	@Autowired Validator validator;
	@Autowired ZeroWeightProductsFormValidator zeroWeightOrderPositionsFormValidator;


	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
		binder.setDisallowedFields("id");
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true)); // Converts empty strings into null when a form is submitted
	}
	

	// Переход на страницу позиций с неуказанным весом
	@RequestMapping(value = "/zeroweightorderpositions")
	public String PageZeroWeightOrderPositions(@RequestParam(name = "sp") Long spId,
										 		Model model){

		// TODO: 02.05.2017 Check if Sp with given id exists

		if (!model.containsAttribute("zeroWeightOrderPositionsForm")) {
			ZeroWeightOrderPositionsForm zeroWeightOrderPositionsForm = new ZeroWeightOrderPositionsForm(orderPositionService.getZeroWeightOrderPositions(spId));
			model.addAttribute("zeroWeightOrderPositionsForm", zeroWeightOrderPositionsForm);
		}

		model.addAttribute("spId", spId);

		return "zeroweightorderpositions";
	}

	// Обработка запроса на сохранение нового веса продуктов с неуказанным весом
	@RequestMapping(value = "/zeroweightorderpositions", params = {"action=save"}, method = RequestMethod.POST)
	public String SaveZeroWeightProducts(@RequestParam(name = "sp") Long spId,
										 @ModelAttribute ZeroWeightOrderPositionsForm zeroWeightOrderPositionsForm,
										 RedirectAttributes redirectAttributes,
										 Errors errors, Model model){

		// TODO: 02.05.2017 Perform security checks of products list (id, neme, etc.)
		// TODO: 03.05.2017 Обновить OrderPosition.productWeight в текущем СП

		zeroWeightOrderPositionsFormValidator.validate(zeroWeightOrderPositionsForm, errors);
		List<OrderPosition> newZeroWeightOrderPositions = zeroWeightOrderPositionsForm.getOrderPositions();

		for (final ListIterator<OrderPosition> iterator = newZeroWeightOrderPositions.listIterator(); iterator.hasNext();){
			OrderPosition formOrderPosition = iterator.next();
			OrderPosition orderPosition = orderPositionService.getById(formOrderPosition.getId());
			orderPosition.setProductWeight(formOrderPosition.getProductWeight());
			iterator.set(orderPosition);
			if (!errors.hasFieldErrors("orderPositions["+ (iterator.nextIndex() - 1) +"].productWeight") && orderPosition.getProductWeight() > 0) {
				orderPositionService.update(orderPosition);
				Product product = orderPosition.getProduct();
				product.setWeight(orderPosition.getProductWeight());
				productService.update(product);
				iterator.remove();
			}
		}

		if (errors.hasErrors()) {
			redirectAttributes.addFlashAttribute("zeroWeightOrderPositionsForm", zeroWeightOrderPositionsForm);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.zeroWeightOrderPositionsForm", errors);
		}

		return "redirect:/zeroweightorderpositions?sp=" + spId;
	}


}