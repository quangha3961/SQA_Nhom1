package beebooks.controller.quantrivien;

import beebooks.controller.BaseController;
import beebooks.dto.SearchModel;
import beebooks.entities.Author;
import beebooks.entities.Product;
import beebooks.entities.ProductImage;
import beebooks.repository.AuthorRepository;
import beebooks.repository.ProductImageRepository;
import beebooks.repository.ProductRepository;
import beebooks.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Controller
public class AuthorController extends BaseController {

    private final AuthorService authorService;
    private final AuthorRepository authorRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public AuthorController(AuthorService authorService, AuthorRepository authorRepository,
                            ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.authorService = authorService;
        this.authorRepository = authorRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @RequestMapping(value = {"/admin/author/add"}, method = RequestMethod.GET)
    public String adminGet(final Model model) {

        Author addCate = new Author();
        model.addAttribute("addCate", addCate);

        return "quantrivien/add-author";
    }


    @RequestMapping(value = {"/admin/author/adds"}, method = RequestMethod.POST)
    public String adminAddSpringForm( final @ModelAttribute("addCate") Author author) {
        authorService.saveOrUpdate(author);

        return "redirect:/admin/author/list";
    }

    @RequestMapping(value = {"/admin/author/list"}, method = RequestMethod.GET)
    public String adminList(final Model model, final HttpServletRequest request) {

        SearchModel searchModel = new SearchModel();
        searchModel.keyword = request.getParameter("keyword");
        searchModel.setPage(getCurrentPage(request));

        model.addAttribute("withPaging", authorService.search(searchModel));
        model.addAttribute("searchModel", searchModel);

        return "quantrivien/author";
    }


    @RequestMapping(value = {"/admin/author/update/{authorId}"}, method = RequestMethod.GET)
    public String adminEdit(final Model model, @PathVariable("authorId") int authorId) {
        Author author = authorService.getById(authorId);
        model.addAttribute("addCate", author);

        return "quantrivien/add-author";
    }

    @GetMapping("/deleteAuthor/{id}")
    public String adminDelete(@PathVariable("id") Integer id) {

        Optional<Author> categoryOptional = authorRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Author category = categoryOptional.get();

            Set<Product> products = category.getProducts();
            for (Product product : products) {
                Set<ProductImage> productImages = product.getProductImage();
                for (ProductImage productImage : productImages) {
                    productImageRepository.delete(productImage);
                }
                productRepository.delete(product);
            }

            authorRepository.delete(category);
        }
        return "redirect:/admin/author/list";
    }

}
