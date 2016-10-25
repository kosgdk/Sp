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
@DynamicUpdate
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
	private OrderStatus status = OrderStatus.UNPAID;

	@NotNull(message = "{order.prepaid.isEmpty}")
	@Min(value = 0, message = "{order.prePaid.negative}")
	@Column(name = "prepaid")
	private BigDecimal prepaid = new BigDecimal(0);

	@NotNull(message = "{order.weight.isEmpty}")
	@Min(value = 0, message = "{order.weight.negative}")
	@Column(name = "weight")
	private Integer weight = 0;
	
	@Column(name = "delivery_price")
	private BigDecimal deliveryPrice = new BigDecimal(0);
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "place", referencedColumnName = "id")
	private Place place;
	
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (orderPositions != null ? !orderPositions.equals(order.orderPositions) : order.orderPositions != null)
            return false;
        if (client != null ? !client.equals(order.client) : order.client != null) return false;
        if (sp != null ? !sp.equals(order.sp) : order.sp != null) return false;
        if (note != null ? !note.equals(order.note) : order.note != null) return false;
        if (dateOrdered != null ? !dateOrdered.equals(order.dateOrdered) : order.dateOrdered != null) return false;
        if (status != order.status) return false;
        if (prepaid != null ? !prepaid.equals(order.prepaid) : order.prepaid != null) return false;
        if (weight != null ? !weight.equals(order.weight) : order.weight != null) return false;
        if (deliveryPrice != null ? !deliveryPrice.equals(order.deliveryPrice) : order.deliveryPrice != null)
            return false;
        if (place != null ? !place.equals(order.place) : order.place != null) return false;
        if (dateCompleted != null ? !dateCompleted.equals(order.dateCompleted) : order.dateCompleted != null)
            return false;
        if (summaryPrice != null ? !summaryPrice.equals(order.summaryPrice) : order.summaryPrice != null) return false;
        if (income != null ? !income.equals(order.income) : order.income != null) return false;
        if (debt != null ? !debt.equals(order.debt) : order.debt != null) return false;
        return total != null ? total.equals(order.total) : order.total == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (orderPositions != null ? orderPositions.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        result = 31 * result + (sp != null ? sp.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        result = 31 * result + (dateOrdered != null ? dateOrdered.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (prepaid != null ? prepaid.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (deliveryPrice != null ? deliveryPrice.hashCode() : 0);
        result = 31 * result + (place != null ? place.hashCode() : 0);
        result = 31 * result + (dateCompleted != null ? dateCompleted.hashCode() : 0);
        result = 31 * result + (summaryPrice != null ? summaryPrice.hashCode() : 0);
        result = 31 * result + (income != null ? income.hashCode() : 0);
        result = 31 * result + (debt != null ? debt.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        return result;
    }
}
