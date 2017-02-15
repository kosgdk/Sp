package sp.data.entities;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import sp.data.converters.SpStatusConverter;
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
import javax.validation.constraints.*;

@Entity
@Table(name="sp")
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sp {

	@Id
	@GenericGenerator(name="incrementGenerator",strategy="increment")
	@GeneratedValue(generator="incrementGenerator")
	@Column(name = "id")
	private Long id;

	@OneToMany(mappedBy = "sp", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@OrderBy(value = "id")
	private Set<Order> orders;

	@NotNull
	@DecimalMin(value = "0", message = "{sp.percent.outOfRange}")
	@DecimalMax(value = "1", message = "{sp.percent.outOfRange}")
	@Column(name = "percent")
	private BigDecimal percent;

	@NotNull(message = "{sp.status.isEmpty}")
	@Column(name = "status")
	@Convert(converter = SpStatusConverter.class)
	private SpStatus status;

	@NotNull(message = "{sp.dateStart.isEmpty}")
	@Temporal(TemporalType.DATE)
	@Column(name = "date_start")
	private Date dateStart;

	@NotNull(message = "{sp.dateEnd.isEmpty}")
	@Temporal(TemporalType.DATE)
	@Column(name = "date_end")
	private Date dateEnd;

	@NotNull(message = "{sp.dateToPay.isEmpty}")
	@Temporal(TemporalType.DATE)
	@Column(name = "date_to_pay")
	private Date dateToPay;

	@Past(message = "{sp.dateSent.shouldBePast}")
	@Temporal(TemporalType.DATE)
	@Column(name = "date_sent")
	private Date dateSent;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_to_receive")
	private Date dateToReceive;

	@Past(message = "{sp.dateReceived.shouldBePast}")
	@Temporal(TemporalType.DATE)
	@Column(name = "date_received")
	private Date dateReceived;

	@Temporal(TemporalType.DATE)
	@Column(name = "date_to_distribute")
	private Date dateToDistribute;

	
	public Sp() {
	}


	private void calculateDateToPay(Date dateEnd){
		if (dateEnd!=null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateEnd);
			cal.add(Calendar.DATE, 3); // default - 3 days
			dateToPay = cal.getTime();
		}else {
			dateToPay = null;
		}
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		if (percent != null) this.percent = percent.setScale(2, BigDecimal.ROUND_HALF_DOWN);
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

	public Date getDateToReceive() {
		return dateToReceive;
	}

	public void setDateToReceive(Date dateToRecieve) {
		this.dateToReceive = dateToRecieve;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateRecieved) {
		this.dateReceived = dateRecieved;
	}

	public Date getDateToDistribute() {
		return dateToDistribute;
	}

	public void setDateToDistribute(Date dateToDistribute) {
		this.dateToDistribute = dateToDistribute;
	}


	@Override
	public String toString() {
		return "Sp{" +
				"id=" + id +
				", percent=" + percent +
				", status=" + status +
				", dateStart=" + dateStart +
				", dateEnd=" + dateEnd +
				", dateToPay=" + dateToPay +
				", dateSent=" + dateSent +
				", dateToReceive=" + dateToReceive +
				", dateReceived=" + dateReceived +
				", dateToDistribute=" + dateToDistribute +
				'}';
	}

}