package sp.data.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Entity
@Table(name="clients")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class Client {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@JsonIgnore
	@OneToMany(mappedBy="client", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@OrderBy(value = "id desc")
	private Set<Order> orders;

	@NotNull(message = "{client.name.isEmpty}")
	@Size(min = 3, max = 50, message = "{client.name.length}")
	@Column(name="name", unique = true)
	@JsonProperty
	private String name;

	@Size(min = 2, max = 50, message = "{client.realName.length}")
	@Column(name="real_name")
	private String realName;

	@Pattern(regexp = "\\+\\d\\(\\d{3}\\)\\d{3}\\-\\d{2}\\-\\d{2}", message = "{client.phone.invalid}")
	@Column(name="phone")
	private String phone;

	@Size(max = 500, message = "{client.note.length}")
	@Column(name="note")
	private String note;

	@NotNull(message = "{client.referer.isEmpty}")
	@JsonIgnore
	@ManyToOne(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
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


	public Set<Order> getOrders() {
		return orders;
	}


	public void setOrders(Set<Order> orders) {
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


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
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
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Client)) return false;

		Client client = (Client) o;

		if (id != client.id) return false;
		//if (orders != null ? !orders.equals(client.orders) : client.orders != null) return false;
		if (name != null ? !name.equals(client.name) : client.name != null) return false;
		if (realName != null ? !realName.equals(client.realName) : client.realName != null) return false;
		if (phone != null ? !phone.equals(client.phone) : client.phone != null) return false;
		if (note != null ? !note.equals(client.note) : client.note != null) return false;
		//if (referer != null ? referer.getId() != client.getId() : client.referer != null) return false;

		return true;

	}

	@Override
	public int hashCode() {
		int result = id;
		//result = 31 * result + (orders != null ? orders.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (realName != null ? realName.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		result = 31 * result + (note != null ? note.hashCode() : 0);
		result = 31 * result + (referer != null ? referer.hashCode() : 0);
		return result;
	}
}
