package sp.data.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import sp.data.converters.attributeconverters.ProductStatusConverter;
import sp.data.entities.enumerators.ProductStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name="products")
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

	@NotNull
	@Size(max = 300)
	@Column(name="name")
	private String name;

	@NotNull
	@Size(max = 300)
	@Column(name="link")
	private String link;

	@NotNull
	@Min(0)
	@Column(name="weight")
	private Integer weight = 0;

	@NotNull
	@Min(0)
	@Column(name="price")
	private BigDecimal price = new BigDecimal(0);

	@Min(0)
	@Column(name="vkid")
	private Integer vkId;

	@Size(max = 300)
	@Column(name="imagelink")
	private String imageLink;

	@NotNull
	@Column(name="status")
	@Convert(converter = ProductStatusConverter.class)
	private ProductStatus status;

	@Min(0)
	@Column(name="vkphotoid")
	private Integer vkPhotoId;
	
		
	public Product() {}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
		if (price != null) this.price = price.setScale(2, RoundingMode.HALF_DOWN);
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getVkId() {
		return vkId;
	}

	public void setVkId(Integer vkId) {
		this.vkId = vkId;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	public Integer getVkPhotoId() {
		return vkPhotoId;
	}

	public void setVkPhotoId(Integer vkPhotoId) {
		this.vkPhotoId = vkPhotoId;
	}


	@Override
	public String toString() {
		return name;
	}

}