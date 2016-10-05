package sp.data.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


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
	@ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
	@JoinColumn(name = "status", referencedColumnName = "id")
	private OrderStatus orderStatus;
	
	@Column(name = "prepaid")
	private BigDecimal prepaid;

	@NotNull(message = "{order.weight.isEmpty}")
	@Pattern(regexp = "\\d+", message="{order.weight.negative}")
	@Column(name = "weight")
	private int weight;
	
	@Column(name = "delivery_price")
	private BigDecimal deliveryPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place", referencedColumnName = "id")
	//@Fetch(FetchMode.JOIN)
	private Place place;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_completed")
	private Date datecompleted;

	@Transient
	private BigDecimal summaryPrice;
	
	
	public Order() {
	}
	
	public Order(Client client, Sp sp, Date dateOrdered, OrderStatus orderStatus) {
		super();
		this.client = client;
		this.sp = sp;
		this.dateOrdered = dateOrdered;
		this.orderStatus = orderStatus;
	}

	private void calculateSummaryPrice(){
		BigDecimal summaryPrice = new BigDecimal(0);
		if (orderPositions != null){
			for (OrderPosition orderPosition : orderPositions) {
				summaryPrice = summaryPrice.add(orderPosition.getPriceSp());
			}
		}
		this.summaryPrice = summaryPrice;
	}

	private void calculateWeight(){
		weight = 0;
		for (OrderPosition orderPosition : orderPositions) {
			weight +=orderPosition.getProduct().getWeight();
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

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getPrepaid() {
		return prepaid;
	}

	public void setPrepaid(BigDecimal prepaid) {
		this.prepaid = prepaid;
	}

	public int getWeight() {
		calculateWeight();
		return weight;
	}

	public void setWeight(int weight) {
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

	public Date getDatecompleted() {
		return datecompleted;
	}

	public void setDatecompleted(Date datecompleted) {
		this.datecompleted = datecompleted;
	}

	public BigDecimal getSummaryPrice(){
		calculateSummaryPrice();
		return this.summaryPrice;
	}



	@Override
	public String toString() {
		return "Order [id=" + id + ",\n orderPositions=" + orderPositions + ",\n client=" + client + ",\n sp=" + sp
				+ ",\n dateOrdered=" + dateOrdered + ",\n orderStatus=" + orderStatus + ",\n prepaid=" + prepaid
				+ ",\n weight=" + weight + ",\n deliveryPrice=" + deliveryPrice + ",\n place=" + place
				+ ",\n datecompleted=" + datecompleted + "]";
	}


	
	
}
