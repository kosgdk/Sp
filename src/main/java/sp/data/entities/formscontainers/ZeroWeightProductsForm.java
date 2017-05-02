package sp.data.entities.formscontainers;

import sp.data.entities.Product;
import java.util.List;

public class ZeroWeightProductsForm {

	private List<Product> products;

	public ZeroWeightProductsForm() {
	}

	public ZeroWeightProductsForm(List<Product> products) {
		this.products = products;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
