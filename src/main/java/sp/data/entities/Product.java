package sp.data.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

}
