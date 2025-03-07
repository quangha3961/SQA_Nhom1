package beebooks.controller.khachhang;

import beebooks.controller.BaseController;
import beebooks.dto.BlogSearchModel;
import beebooks.entities.Blog;
import beebooks.service.PagerData;
import beebooks.service.impl.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BlogUIController extends BaseController {

	private final BlogService blogService;


	public BlogUIController(BlogService blogService) {
		this.blogService = blogService;
	}

	@RequestMapping(value = { "/blog" }, method = RequestMethod.GET)
	public String getAllBlog(final Model model, final HttpServletRequest request, final HttpServletResponse response) {
		BlogSearchModel searchModel = new BlogSearchModel();
		searchModel.keyword = request.getParameter("keyword");

		model.addAttribute("blogsWithPaging", blogService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		return "khachhang/blog";
	}

	@RequestMapping(value = { "/blog/details/{blogSeo}"}, method = RequestMethod.GET)
	public String blogDetails(final Model model, final HttpServletRequest request, final HttpServletResponse response,
								 @PathVariable("blogSeo") String blogSeo) {

		BlogSearchModel searchModel = new BlogSearchModel();
		searchModel.seo = blogSeo;

		PagerData<Blog> blogs = blogService.search(searchModel);
		Blog blog = blogs.getData().get(0);

		model.addAttribute("blog", blog);
		return "khachhang/details-blog";
	}

}
