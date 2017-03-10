package sp.data.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import sp.data.converters.attributeconverters.OrderStatusConverter;
import sp.data.converters.attributeconverters.PlaceConverter;
import sp.data.entities.enumerators.OrderStatus;
import sp.data.entities.enumerators.Place;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Entity
@Table(name = "orders")
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private List<OrderPosition> orderPositions;
	
	@NotNull(message = "{order.client.noSuchClient}")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "client")
	@Fetch(FetchMode.JOIN)
	private Client client;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sp")
	private Sp sp;

	@Size(max = 500, message = "{order.note.size}")
	@Column(name = "note")
	private String note;
	
	@NotNull(message = "{order.dateOrdered.invalid}")
	@Past(message = "{order.dateOrdered.shouldBePast}")
	@Temporal(TemporalType.DATE)
	@Column(name = "date_ordered")
	private Date dateOrdered;
	
	@NotNull
	@Column(name = "status")
	@Convert(converter = OrderStatusConverter.class)
	private OrderStatus status = OrderStatus.UNPAID;

	@NotNull(message = "{order.prepaid.isEmpty}")
	@Min(value = 0, message = "{order.prePaid.negative}")
	@Column(name = "prepaid", precision=7, scale=2)
	private BigDecimal prepaid = new BigDecimal(0);

	@NotNull(message = "{order.weight.isEmpty}")
	@Min(value = 0, message = "{order.weight.negative}")
	@Column(name = "weight")
	private Integer weight = 0;
	
	@Column(name = "delivery_price", precision=7, scale=2)
	private BigDecimal deliveryPrice = new BigDecimal(0);

	@Column(name = "place")
	@Convert(converter = PlaceConverter.class)
	private Place place;

	@Past
	@Temporal(TemporalType.DATE)
	@Column(name = "date_completed")
	private Date dateCompleted;

	@Transient
	private BigDecimal summaryPrice = new BigDecimal(0);

	@Transient
	private BigDecimal income = new BigDecimal(0);

	@Transient
	private BigDecimal debt = new BigDecimal(0);

	@Transient
	private BigDecimal total = new BigDecimal(0);

	
	public Order() {}


	private void calculateSummaryPrice(){
		summaryPrice = new BigDecimal(0);
		if (orderPositions != null){
			for (OrderPosition orderPosition : orderPositions) {
				summaryPrice = orderPosition!=null ? summaryPrice.add(orderPosition.getPriceSpSummary()) : summaryPrice;
			}
		}
	}

	private void calculateWeight(){
		weight = 0;
		if (orderPositions != null) {
			for (OrderPosition orderPosition : orderPositions) {
				weight += orderPosition != null ? orderPosition.getWeight() : 0;
			}
		}
	}

	private void calculateIncome(){
		income = new BigDecimal(0);
		if (orderPositions != null) {
			for (OrderPosition orderPosition : orderPositions) {
				income = orderPosition != null ? income.add(orderPosition.getIncome()) : income;
			}
		}
	}

	private void calculateDebt(){
		debt = getSummaryPrice().subtract(prepaid);
	}

	private void calculateTotal(){

		total = getDebt().add(deliveryPrice);
	}

	// Getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<OrderPosition> getOrderPositions() {
		return orderPositions;
	}

	public void setOrderPositions(List<OrderPosition> orderPositions) {
		this.orderPositions = orderPositions;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Sp getSp() {
		return sp;
	}

	public void setSp(Sp sp) {
		this.sp = sp;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getDateOrdered() {
		return dateOrdered;
	}

	public void setDateOrdered(Date dateOrdered) {
		this.dateOrdered = dateOrdered;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public BigDecimal getPrepaid() {
		return prepaid;
	}

	public void setPrepaid(BigDecimal prepaid) {
		if(prepaid != null) this.prepaid = prepaid.setScale(2, RoundingMode.HALF_DOWN);
	}

	public Integer getWeight() {
		calculateWeight();
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public BigDecimal getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(BigDecimal deliveryPrice) {
		if (deliveryPrice != null) this.deliveryPrice = deliveryPrice.setScale(2, RoundingMode.HALF_DOWN);
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateСompleted) {
		this.dateCompleted = dateСompleted;
	}

	public BigDecimal getSummaryPrice(){
		calculateSummaryPrice();
		return this.summaryPrice;
	}

	public BigDecimal getIncome() {
		calculateIncome();
		return income;
	}

	public BigDecimal getDebt() {
		calculateDebt();
		return debt;
	}

	public BigDecimal getTotal() {
		calculateTotal();
		return total;
	}


	@Override
	public String toString() {
		return "Order (id=" + id + ") of " + client.getName() + " in SP " + sp.getId();
	}

}
