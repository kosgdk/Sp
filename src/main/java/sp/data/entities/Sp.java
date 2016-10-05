package sp.data.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import sp.data.converters.SpStatusEnumConverter;
import sp.data.entities.enumerators.SpStatus;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="sp")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sp {

	@Id
	@GeneratedValue(generator = "SpIdGenerator")
	@GenericGenerator(name = "SpIdGenerator", strategy = "sp.data.idgenerators.SpIdGenerator")
	@Column(name = "id")
	private int id;

	@OneToMany(mappedBy = "sp", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy(value = "id")
	private Set<Order> orders;

	@Column(name = "number")
	private int number;

	@NotNull
	@DecimalMin(value = "0", message = "{sp.percent.negative}")
	@DecimalMax(value = "0.15", message = "{sp.percent.max}")
	@Column(name = "percent")
	private BigDecimal percent;

	@Column(name = "status_enum")
	@Convert(converter = SpStatusEnumConverter.class)
	private SpStatus status;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_start")
	private Date dateStart;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_end")
	private Date dateEnd;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_to_pay")
	private Date dateToPay;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_sent")
	private Date dateSent;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_to_recieve")
	private Date dateToRecieve;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_recieved")
	private Date dateRecieved;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_to_distribute")
	private Date dateToDistribute;

	
	public Sp() {
	}

	public void calculateDateToPay(Date dateEnd){
		Calendar cal = Calendar.getInstance();
        cal.setTime(dateEnd);
        cal.add(Calendar.DATE, 3); // default - 3 days
        dateToPay = cal.getTime();
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public SpStatus getStatus() {
		return status;
	}

	public void setStatus(SpStatus status) {
		this.status = status;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
		calculateDateToPay(dateEnd);
	}

	public Date getDateToPay() {
		return dateToPay;
	}

	public void setDateToPay(Date dateToPay) {
		this.dateToPay = dateToPay;
	}

	public Date getDateSent() {
		return dateSent;
	}

	public void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}

	public Date getDateToRecieve() {
		return dateToRecieve;
	}

	public void setDateToRecieve(Date dateToRecieve) {
		this.dateToRecieve = dateToRecieve;
	}

	public Date getDateRecieved() {
		return dateRecieved;
	}

	public void setDateRecieved(Date dateRecieved) {
		this.dateRecieved = dateRecieved;
	}

	public Date getDateToDistribute() {
		return dateToDistribute;
	}

	public void setDateToDistribute(Date dateToDistribute) {
		this.dateToDistribute = dateToDistribute;
	}


	@Override
	public String toString() {
		return "СП-" + id;
	}

	

}
