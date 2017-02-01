package sp.data.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import sp.data.converters.ProductStatusConverter;
import sp.data.converters.SpStatusConverter;
import sp.data.entities.enumerators.ProductStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

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
	private BigDecimal price;

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
		this.price = price != null ? price.setScale(2, RoundingMode.HALF_DOWN) : null;
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

	/*
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Product)) return false;

		Product product = (Product) o;

		if (id != null ? !id.equals(product.id) : product.id != null) return false;
		if (!name.equals(product.name)) return false;
		if (!link.equals(product.link)) return false;
		if (!weight.equals(product.weight)) return false;
		if (!price.equals(product.price)) return false;
		if (vkId != null ? !vkId.equals(product.vkId) : product.vkId != null) return false;
		if (imageLink != null ? !imageLink.equals(product.imageLink) : product.imageLink != null) return false;
		if (status != null ? !status.equals(product.status) : product.status != null) return false;
		return vkPhotoId != null ? vkPhotoId.equals(product.vkPhotoId) : product.vkPhotoId == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + name.hashCode();
		result = 31 * result + link.hashCode();
		result = 31 * result + weight.hashCode();
		result = 31 * result + price.hashCode();
		result = 31 * result + (vkId != null ? vkId.hashCode() : 0);
		result = 31 * result + (imageLink != null ? imageLink.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		result = 31 * result + (vkPhotoId != null ? vkPhotoId.hashCode() : 0);
		return result;
	}
	*/
}