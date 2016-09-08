package sp.data.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="order_position")
public class OrderPosition {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@ManyToOne()
	@JoinColumn(name="order_id")
	private Order order;
	
	@ManyToOne()
	@JoinColumn(name="product_id")
	private Product product;
	
	@Column(name="price_ordered")
	private BigDecimal priceOrdered;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="note")
	private String note;
	
	
	public OrderPosition() {
	}
	
	public OrderPosition(Product product) {
		this.product = product;
		this.priceOrdered = product.getPrice();
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getPriceOrdered() {
		return priceOrdered;
	}

	public void setPriceOrdered(BigDecimal priceOrdered) {
		this.priceOrdered = priceOrdered;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "OrderPosition [id=" + id + ",\n product=" + product + ",\n priceOrdered=" + priceOrdered
				+ ",\n quantity=" + quantity + ",\n note=" + note + "]";
	}


	
}