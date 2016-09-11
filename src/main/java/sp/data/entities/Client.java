package sp.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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


@Entity
@Table(name="clients")
public class Client {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@JsonIgnore
	@OneToMany(mappedBy="client", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<Order> orders;
	
	@Column(name="name")
	private String name;
	
	@Column(name="real_name")
	private String realName;
	
	@Column(name="phone")
	private Long phone;
	
	@Column(name="note")
	private String note;
	
	@ManyToOne(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	@JoinColumn(name="referer", referencedColumnName="id", nullable=false)
	private Referer referer;
	
	
	public Client() {
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getRealName() {
		return realName;
	}


	public void setRealName(String realName) {
		this.realName = realName;
	}


	public Long getPhone() {
		return phone;
	}


	public void setPhone(Long phone) {
		this.phone = phone;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public Referer getReferer() {
		return referer;
	}


	public void setReferer(Referer referer) {
		this.referer = referer;
	}


	@Override
	public String toString() {
		return "Client [id=" + id + ",\n name=" + name + ",\n realName=" + realName + ",\n phone=" + phone
				+ ",\n note=" + note + ",\n referer=" + referer + "]";
	}
	

}
