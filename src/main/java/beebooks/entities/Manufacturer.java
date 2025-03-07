package beebooks.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_manufacturer")
public class Manufacturer extends BaseEntity {

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "address", length = 1000, nullable = false)
	private String address;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "manufacturer", fetch = FetchType.EAGER)
	private Set<Product> products = new HashSet<Product>();

	public void addProduct(Product product) {
		this.products.add(product);
		product.setManufacturer(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}
