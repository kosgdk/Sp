package sp.data.entities;

import java.util.Calendar;
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
@Table(name="sp")
public class Sp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@OneToMany(mappedBy = "sp", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Order> orders;

	@Column(name = "number")
	private int number;
	
	@Column(name = "percent")
	private int percent = 15;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "status", referencedColumnName = "id")
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
	
	
	
	public Sp(int number, Date dateStart, Date dateEnd) {
		super();
		this.number = number;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.dateToPay = calculateDateToPay(dateEnd);
	}


	public Date calculateDateToPay(Date dateEnd){
		Calendar cal = Calendar.getInstance();
        cal.setTime(dateEnd);
        cal.add(Calendar.DATE, 3); // default - 3 days
        return cal.getTime();
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
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
		this.dateToPay = calculateDateToPay(dateEnd);
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
		return "Sp [id=" + id + ",\n name=" + number + "\n percent=" + percent + "%" + ",\n status=" + status + ",\n dateStart=" + dateStart
				+ ",\n dateEnd=" + dateEnd + ",\n dateToPay=" + dateToPay + ",\n dateSent=" + dateSent
				+ ",\n dateToRecieve=" + dateToRecieve + ",\n dateRecieved=" + dateRecieved + ",\n dateToDistribute="
				+ dateToDistribute + "]";
	}

	

}
