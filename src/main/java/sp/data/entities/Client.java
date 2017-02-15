package sp.data.entities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import sp.data.converters.attributeconverters.ClientReferrerConverter;
import sp.data.entities.enumerators.ClientReferrer;

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
	private Long id;

	@JsonIgnore
	@OneToMany(mappedBy="client", fetch=FetchType.LAZY)
	@OrderBy(value = "id desc")
	private Set<Order> orders;

	@NotNull(message = "{client.name.isEmpty}")
	@Size(min = 3, max = 50, message = "{client.name.length}")
	@Column(name="name", unique = true, updatable = false)
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

	@NotNull(message = "{client.clientReferrer.isEmpty}")
	@Column(name = "referrer")
	@Convert(converter = ClientReferrerConverter.class)
	private ClientReferrer clientReferrer;
	
	
	public Client() {}


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

	public ClientReferrer getClientReferrer() {
		return clientReferrer;
	}

	public void setClientReferrer(ClientReferrer referrer) {
		this.clientReferrer = referrer;
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

		return name != null ? name.equals(client.name) : client.name == null;

	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : super.hashCode();
	}
}
