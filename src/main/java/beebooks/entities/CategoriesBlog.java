package beebooks.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_category_blog")
public class CategoriesBlog extends BaseEntity {

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@Column(name = "description", length = 100, nullable = false)
	private String description;

	@Column(name = "seo", length = 1000, nullable = true)
	private String seo;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "categoriesBlog", fetch = FetchType.EAGER)
	private Set<Blog> blogs = new HashSet<Blog>();

	public void addBlog(Blog blog) {
		this.blogs.add(blog);
		blog.setCategoriesBlog(this);
	}

	public void deleteBlog(Blog blog) {
		this.blogs.remove(blog);
		blog.setCategoriesBlog(null);
	}
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "parent_id")
//	private Categories parent;
	
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "parent")
//	private Set<Categories> childs = new HashSet<Categories>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSeo() {
		return seo;
	}

	public void setSeo(String seo) {
		this.seo = seo;
	}

	public Set<Blog> getBlogs() {
		return blogs;
	}

	public void setBlogs(Set<Blog> blogs) {
		this.blogs = blogs;
	}
}
