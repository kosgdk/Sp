package sp.data.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="order_position")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderPosition {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;
	
	@ManyToOne(cascade=CascadeType.PERSIST, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="product_id")
	private Product product;
	
	@Column(name="price_ordered")
	private BigDecimal priceOrdered;

	@Column(name="price_vendor")
	private BigDecimal priceVendor;

	@Column(name="price_sp")
	private BigDecimal priceSp;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="note")
	private String note;
	
	@Transient
	private BigDecimal income;


	public OrderPosition() {
	}
	
	public OrderPosition(Product product, int quantity) {
		this.product = product;
		this.priceOrdered = product.getPrice();
		this.quantity = quantity;
	}

	private void calculateIncome(){
		income = (priceSp.subtract(priceVendor)).multiply(new BigDecimal(quantity));
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

	public BigDecimal getPriceVendor() {
		return priceVendor;
	}

	public void setPriceVendor(BigDecimal priceVendor) {
		this.priceVendor = priceVendor;
	}

	public BigDecimal getPriceSp() {
		return priceSp;
	}

	public void setPriceSp(BigDecimal priceSp) {
		this.priceSp = priceSp;
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

	public BigDecimal getIncome() {
		calculateIncome();
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	@Override
	public String toString() {
		return "OrderPosition [id=" + id + ",\n product=" + product + ",\n priceOrdered=" + priceOrdered
				+ ",\n quantity=" + quantity + ",\n note=" + note + "]";
	}


	
}
