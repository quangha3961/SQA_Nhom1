package beebooks.controller.quantrivien;


import beebooks.controller.BaseController;
import beebooks.dto.BlogSearchModel;
import beebooks.entities.Blog;
import beebooks.entities.Categories;
import beebooks.entities.CategoriesBlog;
import beebooks.entities.Product;
import beebooks.repository.BlogRepository;
import beebooks.repository.CategoriesBlogRepository;
import beebooks.service.impl.CategoriesBlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Controller
public class CategoryBlogController extends BaseController {

    private final CategoriesBlogService categoriesBlogService;
    private final CategoriesBlogRepository categoriesBlogRepository;
    private final BlogRepository blogRepository;

    public CategoryBlogController(CategoriesBlogService categoriesBlogService, CategoriesBlogRepository categoriesBlogRepository, BlogRepository blogRepository) {
        this.categoriesBlogService = categoriesBlogService;
        this.categoriesBlogRepository = categoriesBlogRepository;
        this.blogRepository = blogRepository;
    }

    @RequestMapping(value = {"/admin/blog/add-category-blog"}, method = RequestMethod.GET)
    public String adminAddCategory(final Model model, final HttpServletRequest request) {

        CategoriesBlog addCate = new CategoriesBlog();
        model.addAttribute("addCate", addCate);

        return "quantrivien/add-category-blog";
    }


    @RequestMapping(value = {"/admin/blog/add-category-blog"}, method = RequestMethod.POST)
    public String adminAddSpringForm(final Model model, final HttpServletRequest request, final @ModelAttribute("addCate") CategoriesBlog categoriesBlog
    ) {
        categoriesBlogService.saveOrUpdate(categoriesBlog);

        return "redirect:/admin/category-blog";
    }

    @RequestMapping(value = {"/admin/category-blog/list", "/admin/category-blog"}, method = RequestMethod.GET)
    public String adminCategoriesBlogList(final Model model, final HttpServletRequest request) {

        BlogSearchModel searchModel = new BlogSearchModel();
        searchModel.keyword = request.getParameter("keyword");
        searchModel.setPage(getCurrentPage(request));
        searchModel.categoryId = super.getInteger(request, "categoryId");

        model.addAttribute("blogsWithPaging", categoriesBlogService.search(searchModel));
        model.addAttribute("searchModel", searchModel);

        return "quantrivien/category-blog";
    }


    @RequestMapping(value = {"/admin/blog/add-category-blog/{categoriesId}"}, method = RequestMethod.GET)
    public String adminCategoriesBlogEdit(final Model model, @PathVariable("categoriesId") int categoriesId) {
        CategoriesBlog blog = categoriesBlogService.getById(categoriesId);
        model.addAttribute("addCate", blog);

        return "quantrivien/add-category-blog";
    }

    @GetMapping("/deleteCategoryBlog/{id}")
    public String adminCategoriesDelete(@PathVariable("id") Integer id) {

        Optional<CategoriesBlog> categoryOptional = categoriesBlogRepository.findById(id);
        if (categoryOptional.isPresent()) {
            CategoriesBlog category = categoryOptional.get();

            Set<Blog> products = category.getBlogs();
            for (Blog product : products) {
                blogRepository.delete(product);
            }

            categoriesBlogRepository.delete(category);
        }
        return "redirect:/admin/category-blog";
    }

}
