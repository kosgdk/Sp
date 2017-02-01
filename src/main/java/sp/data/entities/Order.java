package sp.data.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import sp.data.converters.OrderStatusConverter;
import sp.data.converters.PlaceConverter;
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
				summaryPrice = summaryPrice.add(orderPosition.getPriceSpSummary());
			}
		}
	}

	private void calculateWeight(){
		weight = 0;
		if (orderPositions != null) {
			for (OrderPosition orderPosition : orderPositions) {
				weight += (orderPosition.getProduct().getWeight() * orderPosition.getQuantity());
			}
		}
	}

	private void calculateIncome(){
		income = new BigDecimal(0);
		if (orderPositions != null) {
			for (OrderPosition orderPosition : orderPositions) {
				income = income.add(orderPosition.getIncome());
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
		this.prepaid = prepaid != null ? prepaid.setScale(2, RoundingMode.HALF_DOWN): null;
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
		this.deliveryPrice = deliveryPrice != null ? deliveryPrice.setScale(2, RoundingMode.HALF_DOWN) : null;
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

	public void setDateCompleted(Date datecompleted) {
		this.dateCompleted = datecompleted;
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


	/*
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Order)) return false;

		Order order = (Order) o;

		if (id != null ? !id.equals(order.id) : order.id != null) return false;
		if (orderPositions != null){
			if (order.getOrderPositions() == null) return false;
			if (order.getOrderPositions() != null && orderPositions.size() != order.getOrderPositions().size()) return false;
			if (orderPositions.isEmpty() && !order.getOrderPositions().isEmpty()) return false;
			if (!orderPositions.isEmpty() && order.getOrderPositions().isEmpty()) return false;
		} else {
			if (order.getOrderPositions() != null && !order.getOrderPositions().isEmpty())  return false;
		}
		if (!client.equals(order.client)) return false;
		//if (!sp.equals(order.sp)) return false;
		if (note != null ? !note.equals(order.note) : order.note != null) return false;
		if (!dateOrdered.equals(order.dateOrdered)) return false;
		if (status != order.status) return false;
		if (!prepaid.equals(order.prepaid)) return false;
		if (!weight.equals(order.weight)) return false;
		if (deliveryPrice != null ? !deliveryPrice.equals(order.deliveryPrice) : order.deliveryPrice != null)
			return false;
		if (place != order.place) return false;
		return dateCompleted != null ? dateCompleted.equals(order.dateCompleted) : order.dateCompleted == null;
	}
	*/

/*
	@Override
	public int hashCode() {
		System.out.println("inside Order-"+id+" hashCode()");
		int result = id != null ? id.hashCode() : 0;
		//result = 31 * result + (orderPositions != null ? orderPositions.hashCode() : 0);
		//result = 31 * result + (client != null ? client.hashCode() : 0);
		//result = 31 * result + (sp != null ? sp.hashCode() : 0);
		result = 31 * result + (note != null ? note.hashCode() : 0);
		result = 31 * result + (dateOrdered != null ? dateOrdered.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (prepaid != null ? prepaid.hashCode() : 0);
		result = 31 * result + (weight != null ? weight.hashCode() : 0);
		result = 31 * result + (deliveryPrice != null ? deliveryPrice.hashCode() : 0);
		result = 31 * result + (place != null ? place.hashCode() : 0);
		result = 31 * result + (dateCompleted != null ? dateCompleted.hashCode() : 0);
		return result;
	}
	*/
}
