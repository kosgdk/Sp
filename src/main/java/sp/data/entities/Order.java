package sp.data.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import sp.data.converters.OrderStatusConverter;
import sp.data.entities.enumerators.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;


@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
	private OrderStatus status;

	@NotNull(message = "{order.prepaid.isEmpty}")
	@Min(value = 0, message = "{order.prePaid.negative}")
	@Column(name = "prepaid")
	private BigDecimal prepaid;

	@NotNull(message = "{order.weight.isEmpty}")
	@Min(value = 0, message = "{order.weight.negative}")
	@Column(name = "weight")
	private Integer weight;
	
	@Column(name = "delivery_price")
	private BigDecimal deliveryPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place", referencedColumnName = "id")
	//@Fetch(FetchMode.JOIN)
	private Place place;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_completed")
	private Date dateCompleted;

	@Transient
	private BigDecimal summaryPrice;

	@Transient
	private BigDecimal income;

	
	public Order() {
	}


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


	// Getters and setters

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
		this.prepaid = prepaid;
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
		this.deliveryPrice = deliveryPrice;
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

	@Override
	public String toString() {
		return "Order [id=" + id + ",\n orderPositions=" + orderPositions + ",\n client=" + client + ",\n sp=" + sp
				+ ",\n dateOrdered=" + dateOrdered + ",\n status=" + status + ",\n prepaid=" + prepaid
				+ ",\n weight=" + weight + ",\n deliveryPrice=" + deliveryPrice + ",\n place=" + place
				+ ",\n datecompleted=" + dateCompleted + "]";
	}


	
	
}
