package beebooks.entities;

import javax.persistence.*;

@Entity
@Table(name="tbl_blog_images")
public class BlogImage extends BaseEntity{
	@Column(name = "title", length = 500, nullable = false)
	private String title;
	
	@Column(name = "path", length = 200, nullable = false)
	private String path;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "blog_id")
	private Blog blog;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

}
