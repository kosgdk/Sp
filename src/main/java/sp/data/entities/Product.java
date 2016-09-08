package sp.data.entities;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="products")
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
		
	@Column(name="name")
	private String name;
	
	@Column(name="price")
	private BigDecimal price;
	
	@Column(name="link")
	private String link;
	
	@Column(name="photo")
	private byte[] photo;
	
	@Column(name="vkid")
	private int vkId;
	
	@Column(name="imagelink")
	private String imageLink;
	
	@Column(name="deleted")
	private int deleted;
	
	@Column(name="vkphotoid")
	private int vkPhotoId;
	
		
	public Product() {
	}

	public Product(String name, BigDecimal price, String link) {
		super();
		this.name = name;
		this.price = price;
		this.link = link;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getVkId() {
		return vkId;
	}

	public void setVkId(int vkId) {
		this.vkId = vkId;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public int getVkPhotoId() {
		return vkPhotoId;
	}

	public void setVkPhotoId(int vkPhotoId) {
		this.vkPhotoId = vkPhotoId;
	}

	
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", photo=" + Arrays.toString(photo)
				+ ", link=" + link + ", vkId=" + vkId + ", imageLink=" + imageLink + ", deleted=" + deleted
				+ ", vkPhotoId=" + vkPhotoId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + deleted;
		result = prime * result + id;
		result = prime * result + ((imageLink == null) ? 0 : imageLink.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(photo);
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + vkId;
		result = prime * result + vkPhotoId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (deleted != other.deleted)
			return false;
		if (id != other.id)
			return false;
		if (imageLink == null) {
			if (other.imageLink != null)
				return false;
		} else if (!imageLink.equals(other.imageLink))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (!Arrays.equals(photo, other.photo))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (vkId != other.vkId)
			return false;
		if (vkPhotoId != other.vkPhotoId)
			return false;
		return true;
	}
	
	

}
