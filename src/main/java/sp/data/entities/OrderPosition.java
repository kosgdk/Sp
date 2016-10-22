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
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderPosition {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof OrderPosition)) return false;

		OrderPosition that = (OrderPosition) o;

		if (id != that.id) return false;
		if (quantity != that.quantity) return false;
		if (weight != that.weight) return false;
		if (order != null ? !order.equals(that.order) : that.order != null) return false;
		if (product != null ? !product.equals(that.product) : that.product != null) return false;
		if (priceOrdered != null ? !priceOrdered.equals(that.priceOrdered) : that.priceOrdered != null) return false;
		if (priceVendor != null ? !priceVendor.equals(that.priceVendor) : that.priceVendor != null) return false;
		if (priceSp != null ? !priceSp.equals(that.priceSp) : that.priceSp != null) return false;
		if (note != null ? !note.equals(that.note) : that.note != null) return false;
		if (priceSpSummary != null ? !priceSpSummary.equals(that.priceSpSummary) : that.priceSpSummary != null)
			return false;
		return income != null ? income.equals(that.income) : that.income == null;

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (order != null ? order.hashCode() : 0);
		result = 31 * result + (product != null ? product.hashCode() : 0);
		result = 31 * result + (priceOrdered != null ? priceOrdered.hashCode() : 0);
		result = 31 * result + (priceVendor != null ? priceVendor.hashCode() : 0);
		result = 31 * result + (priceSp != null ? priceSp.hashCode() : 0);
		result = 31 * result + quantity;
		result = 31 * result + (note != null ? note.hashCode() : 0);
		result = 31 * result + (priceSpSummary != null ? priceSpSummary.hashCode() : 0);
		result = 31 * result + (income != null ? income.hashCode() : 0);
		result = 31 * result + weight;
		return result;
	}
}
