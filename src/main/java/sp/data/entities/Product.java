package sp.data.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

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
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
		
	@Column(name="name")
	private String name;
	
	@Column(name="link")
	private String link;

	@Column(name="weight")
	private int weight = 0;

	@Column(name="price")
	private BigDecimal price;

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

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
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
		return name;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Product)) return false;

		Product product = (Product) o;

		if (id != product.id) return false;
		if (weight != product.weight) return false;
		if (vkId != product.vkId) return false;
		if (deleted != product.deleted) return false;
		if (vkPhotoId != product.vkPhotoId) return false;
		if (name != null ? !name.equals(product.name) : product.name != null) return false;
		if (link != null ? !link.equals(product.link) : product.link != null) return false;
		if (price != null ? !price.equals(product.price) : product.price != null) return false;
		if (!Arrays.equals(photo, product.photo)) return false;
		return imageLink != null ? imageLink.equals(product.imageLink) : product.imageLink == null;

	}

	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (link != null ? link.hashCode() : 0);
		result = 31 * result + weight;
		result = 31 * result + (price != null ? price.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(photo);
		result = 31 * result + vkId;
		result = 31 * result + (imageLink != null ? imageLink.hashCode() : 0);
		result = 31 * result + deleted;
		result = 31 * result + vkPhotoId;
		return result;
	}
}
