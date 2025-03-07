package beebooks.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tbl_blog")
public class Blog extends BaseEntity{
	
	@Column(name = "title", length = 100, nullable = false)
	private String title;

	
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
	@JoinColumn(name = "category_blog_id")
	private CategoriesBlog categoriesBlog;


	@OneToMany(cascade = CascadeType.ALL, mappedBy = "blog", fetch = FetchType.EAGER)
	private Set<BlogImage> blogImages = new HashSet<BlogImage>();


	public void addBlogImage(BlogImage _blogImage) {
		_blogImage.setBlog(this);
		blogImages.add(_blogImage);
	}

	public void deleteBlogImage(BlogImage _blogImage) {
		this.blogImages.remove(_blogImage);
		_blogImage.setBlog(null);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public CategoriesBlog getCategoriesBlog() {
		return categoriesBlog;
	}

	public void setCategoriesBlog(CategoriesBlog categoriesBlog) {
		this.categoriesBlog = categoriesBlog;
	}

	public Set<BlogImage> getBlogImages() {
		return blogImages;
	}

	public void setBlogImages(Set<BlogImage> blogImages) {
		this.blogImages = blogImages;
	}
}
