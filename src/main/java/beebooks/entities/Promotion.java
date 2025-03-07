package beebooks.entities;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_promotion")
public class Promotion extends BaseEntity {

	@Column(name = "name", length = 1000, nullable = false)
	private String name;

	private Double percent;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "promotion", fetch = FetchType.EAGER)
	private Set<Product> products = new HashSet<Product>();

	private Date startDate;

    private Date endDate;

	public void addProduct(Product product) {
		this.products.add(product);
		product.setPromotion(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
