package beebooks.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_author")
public class Author extends BaseEntity {

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "biography", length = 10000, nullable = false)
	private String biography;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.EAGER)
	private Set<Product> products = new HashSet<Product>();

	public void addProduct(Product product) {
		this.products.add(product);
		product.setAuthor(this);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBiography() {
		return biography;
	}

	public void setBiography(String biography) {
		this.biography = biography;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}
