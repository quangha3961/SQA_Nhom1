package beebooks.controller.quantrivien;


import beebooks.controller.BaseController;
import beebooks.dto.BlogSearchModel;
import beebooks.entities.Blog;
import beebooks.entities.Product;
import beebooks.repository.BlogRepository;
import beebooks.service.impl.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BlogController extends BaseController {

    private final BlogService blogService;
    private final BlogRepository blogRepository;


    public BlogController(BlogService blogService, BlogRepository blogRepository) {
        this.blogService = blogService;
        this.blogRepository = blogRepository;
    }

    @RequestMapping(value = {"/admin/blog/list", "/admin/blog"}, method = RequestMethod.GET)
    public String adminBlogList(final Model model, final HttpServletRequest request) {

        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.keyword = request.getParameter("keyword");
        searchModel.setPage(getCurrentPage(request));
        searchModel.categoryId = super.getInteger(request, "categoryId");

        model.addAttribute("blogsWithPaging", blogService.search(searchModel));
        model.addAttribute("searchModel", searchModel);

        return "quantrivien/blog";
    }

    @RequestMapping(value = {"/admin/blog/add-blog"}, method = RequestMethod.GET)
    public String adminAdd(final Model model, final HttpServletRequest request) {

        model.addAttribute("add", new Blog());

        return "quantrivien/add-blog";
    }

    @RequestMapping(value = {"/admin/blog/add-blog/{blogId}"}, method = RequestMethod.GET)
    public String adminBlogEdit(final Model model, @PathVariable("blogId") int blogId) {
        Blog blog = blogService.getById(blogId);
        model.addAttribute("add", blog);

        return "quantrivien/add-blog";
    }

    @GetMapping("/deleteBlog/{id}")
    public String adminDelete(@PathVariable("id") Integer id) {

//        blogRepository.deleteById(id);
        Blog blog = blogRepository.findById(id).orElse(null);
        blog.setStatus(false);
        blogRepository.save(blog);
        return "redirect:/admin/blog";
    }


    @RequestMapping(value = {"/admin/blog/add-blog"}, method = RequestMethod.POST)
    public String adminAddPostSpringForm(final Model model, final HttpServletRequest request, final @ModelAttribute("add") Blog blog,
                                         @RequestParam("productAvatar") MultipartFile productAvatar, // hứng file đẩy lên
                                         @RequestParam("productPictures") MultipartFile[] productPictures) throws Exception { // hứng file đẩy lên)

        if (blog.getId() == null || blog.getId() <= 0) {
            blogService.add(blog, productAvatar, productPictures);
            blogService.saveOrUpdate(blog);
            return "redirect:/admin/blog";
        }
        blogService.update(blog, productAvatar, productPictures);

        return "redirect:/admin/blog";
    }

}
