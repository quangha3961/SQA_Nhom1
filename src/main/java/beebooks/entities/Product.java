package beebooks.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tbl_products")
public class Product extends BaseEntity{
	
	@Column(name = "title", length = 100, nullable = false)
	private String title;
	
	@Column(name = "price", precision = 13, scale = 2, nullable = false)
	private BigDecimal price;
	
	@Column(name = "price_sale", precision = 13, scale = 2, nullable = true)
	private BigDecimal price_sale;

	private String publicationYear;//năm xuất bản
	private Date releaseDate;//ngày phát hành
	private Integer quantity;//Số lượng sản phẩm
	private Integer productStatus;
	
	@Column(name = "short_description", length = 3000, nullable = false)
	private String shortDes;
	
	@Lob
	@Column(name = "detail_description", columnDefinition = "LONGTEXT", nullable = false)
	private String details;
	
	@Column(name = "avatar", nullable = true)
	private String avatar;
	
	@Column(name = "seo", length = 1000, nullable = true)
	private String seo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private Categories categories;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "manufacturer_id")
	private Manufacturer manufacturer;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	private Author author;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "promotion_id")
	private Promotion promotion;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
	private Set<ProductImage> productImage = new HashSet<ProductImage>();


	public void addProductImage(ProductImage _productImage) {
		_productImage.setProduct(this);
		productImage.add(_productImage);
	}

	public void deleteProductImage(ProductImage _productImage) {
		this.productImage.remove(_productImage);
		_productImage.setProduct(null);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPrice_sale() {
		return price_sale;
	}

	public void setPrice_sale(BigDecimal price_sale) {
		this.price_sale = price_sale;
	}

	public String getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getShortDes() {
		return shortDes;
	}

	public void setShortDes(String shortDes) {
		this.shortDes = shortDes;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSeo() {
		return seo;
	}

	public void setSeo(String seo) {
		this.seo = seo;
	}

	public Categories getCategories() {
		return categories;
	}

	public void setCategories(Categories categories) {
		this.categories = categories;
	}

	public Set<ProductImage> getProductImage() {
		return productImage;
	}

	public void setProductImage(Set<ProductImage> productImage) {
		this.productImage = productImage;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}
}
