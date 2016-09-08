package sp.data.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="orders")
public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@OneToMany(/*mappedBy="order",*/ cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="order_id")
	private List<OrderPosition> orderPositions;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="client")
	private Client client;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sp")
	private Sp sp;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_ordered")
	private Date dateOrdered;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="status", referencedColumnName="id")
	private OrderStatus orderStatus;
	
	@Column(name="prepaid")
	private BigDecimal prepaid;
	
	@Column(name="weight")
	private int weight;
	
	@Column(name="delivery_price")
	private BigDecimal deliveryPrice;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="place", referencedColumnName="id")
	private Place place;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_completed")
	private Date datecompleted;
	
	
	public Order() {
	}
	
	public Order(Client client, Sp sp, Date dateOrdered, OrderStatus orderStatus) {
		super();
		this.client = client;
		this.sp = sp;
		this.dateOrdered = dateOrdered;
		this.orderStatus = orderStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Order [id=" + id + ",\n orderPositions=" + orderPositions + ",\n client=" + client + ",\n sp=" + sp
				+ ",\n dateOrdered=" + dateOrdered + ",\n orderStatus=" + orderStatus + ",\n prepaid=" + prepaid
				+ ",\n weight=" + weight + ",\n deliveryPrice=" + deliveryPrice + ",\n place=" + place
				+ ",\n datecompleted=" + datecompleted + "]";
	}


	
	
}
