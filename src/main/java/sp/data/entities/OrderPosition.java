package sp.data.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name="order_position")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderPosition {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;

	@NotNull(message = "{orderPosition.product.isEmpty}")
	@ManyToOne(cascade=CascadeType.PERSIST, fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="product_id")
	private Product product;

	@NotNull
	@Min(0)
	@Column(name="price_ordered")
	private BigDecimal priceOrdered;

	@NotNull
	@Min(0)
	@Column(name="price_vendor")
	private BigDecimal priceVendor;

	@NotNull(message = "{orderPosition.priceSp.invalid}")
	@Min(value = 0, message = "{orderPosition.priceSp.invalid}")
	@Column(name="price_sp")
	private BigDecimal priceSp;

	@NotNull
	@Min(1)
	@Column(name="quantity")
	private int quantity;

	@Size(max = 300)
	@Column(name="note")
	private String note;

	@Transient
	private BigDecimal priceSpSummary;
	
	@Transient
	private BigDecimal income;

	@Transient
	private int weight;


	public OrderPosition() {
	}


	private void calculateIncome(){
		if(priceSp != null & priceVendor != null ) {
			income = (priceSp.subtract(priceVendor)).multiply(new BigDecimal(quantity));
		}
	}

	private void calculatePriceSpSummary(){
		if(priceSp != null){
			priceSpSummary = (priceSp.multiply(new BigDecimal(quantity)));
		}
	}

	private void calculateWeight() {
		if (product != null){
			weight = product.getWeight() * quantity;
		}
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

	public BigDecimal getPriceSpSummary() {
		calculatePriceSpSummary();
		return priceSpSummary;
	}

	public void setPriceSpSummary(BigDecimal priceSpSummary) {
		this.priceSpSummary = priceSpSummary;
	}

	public int getWeight() {
		calculateWeight();
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "OrderPosition [id=" + id + ",\n product=" + product + ",\n priceOrdered=" + priceOrdered
				+ ",\n quantity=" + quantity + ",\n note=" + note + "]";
	}


	
}
