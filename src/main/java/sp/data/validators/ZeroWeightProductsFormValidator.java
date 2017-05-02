package sp.data.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sp.data.entities.Product;
import sp.data.entities.formscontainers.ZeroWeightProductsForm;
import sp.data.services.interfaces.ProductService;

import java.util.List;

@Component("ZeroWeightProductsFormValidator")
public class ZeroWeightProductsFormValidator implements Validator {

    @Autowired
    ProductService productService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ZeroWeightProductsForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        System.out.println("inside ZeroWeightProductsFormValidator");

        ZeroWeightProductsForm zeroWeightProductsForm = (ZeroWeightProductsForm) target;
        List<Product> formProducts = zeroWeightProductsForm.getProducts();

        for (Product zeroWeightProduct : formProducts) {
            if (zeroWeightProduct.getWeight() < 0) {
                errors.rejectValue("products[" + formProducts.indexOf(zeroWeightProduct) + "].weight", "product.weight.incorrect");
            }
        }


    }

}