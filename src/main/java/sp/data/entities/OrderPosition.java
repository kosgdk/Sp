package sp.data.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="order_id")
	private Order order;

	@NotNull(message = "{orderPosition.product.isEmpty}")
	@ManyToOne(fetch = FetchType.EAGER/*, cascade = CascadeType.ALL*/)
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
	private Integer quantity = 1;

	@Size(max = 500)
	@Column(name="note")
	private String note;

	@NotNull(message = "{order.weight.isEmpty}")
	@Min(value = 0, message = "{order.weight.negative}")
	@Column(name = "product_weight")
	private Integer productWeight = 0;

	@Transient
	private BigDecimal priceSpSummary = new BigDecimal(0);
	
	@Transient
	private BigDecimal income = new BigDecimal(0);

	@Transient
	private int weight = 0;


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
		weight = productWeight * quantity;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
		if(priceOrdered != null) this.priceOrdered = priceOrdered.setScale(2, RoundingMode.HALF_DOWN);
	}

	public BigDecimal getPriceVendor() {
		return priceVendor;
	}

	public void setPriceVendor(BigDecimal priceVendor) {
		if(priceVendor != null) this.priceVendor = priceVendor.setScale(2, RoundingMode.HALF_DOWN);
	}

	public BigDecimal getPriceSp() {
		return priceSp;
	}

	public void setPriceSp(BigDecimal priceSp) {
		if(priceSp != null) this.priceSp = priceSp.setScale(2, RoundingMode.HALF_DOWN);
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(Integer productWeight) {
		this.productWeight = productWeight;
	}

	public BigDecimal getIncome() {
		calculateIncome();
		return income;
	}

	public BigDecimal getPriceSpSummary() {
		calculatePriceSpSummary();
		return priceSpSummary;
	}

	public int getWeight() {
		calculateWeight();
		return weight;
	}

	@Override
	public String toString() {
		return "OrderPosition{" +
				"id=" + id +
				", product=" + product.getName() +
				", quantity=" + quantity +
				", priceOrdered=" + priceOrdered +
				", priceSp=" + priceSp +
				", priceSpSummary=" + priceSpSummary +
				", income=" + income +
				", weight=" + weight +
				", note='" + note +
				'}';
	}
}
