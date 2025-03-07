package beebooks.controller.quantrivien;

import beebooks.controller.BaseController;
import beebooks.dto.ProductSearchModel;
import beebooks.entities.Categories;
import beebooks.entities.Product;
import beebooks.entities.ProductImage;
import beebooks.repository.CategoriesProductRepository;
import beebooks.repository.ProductImageRepository;
import beebooks.repository.ProductRepository;
import beebooks.service.CategoriesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Controller
public class CategoryProductController extends BaseController {

    private final CategoriesService categoriesService;
    private final CategoriesProductRepository categoriesProductRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public CategoryProductController(CategoriesService categoriesService, CategoriesProductRepository categoriesProductRepository, ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.categoriesService = categoriesService;
        this.categoriesProductRepository = categoriesProductRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @RequestMapping(value = {"/admin/product/add-category"}, method = RequestMethod.GET)
    public String adminGetAddCategory(final Model model, final HttpServletRequest request) {

        Categories addCate = new Categories();
        model.addAttribute("addCate", addCate);

        return "quantrivien/add-category";
    }


    @RequestMapping(value = {"/admin/product/add-category"}, method = RequestMethod.POST)
    public String adminAddSpringForm(final Model model, final HttpServletRequest request, final @ModelAttribute("addCate") Categories categories
    ) {
        categoriesService.saveOrUpdate(categories);

        return "redirect:/admin/category-product";
    }

    @RequestMapping(value = {"/admin/product/list", "/admin/category-product"}, method = RequestMethod.GET)
    public String adminCategoriesList(final Model model, final HttpServletRequest request) {

        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.keyword = request.getParameter("keyword");
        searchModel.setPage(getCurrentPage(request));
        searchModel.categoryId = super.getInteger(request, "categoryId");

        model.addAttribute("categoriesWithPaging", categoriesService.search(searchModel));
        model.addAttribute("searchModel", searchModel);

        return "quantrivien/category-product";
    }


    @RequestMapping(value = {"/admin/product/add-category/{categoriesId}"}, method = RequestMethod.GET)
    public String adminCategoriesEdit(final Model model, @PathVariable("categoriesId") int categoriesId) {
        Categories categories = categoriesService.getById(categoriesId);
        model.addAttribute("addCate", categories);

        return "quantrivien/add-category";
    }

    @GetMapping("/deleteCategoryProduct/{id}")
    public String adminCategoriesDelete(@PathVariable("id") Integer id) {

        Optional<Categories> categoryOptional = categoriesProductRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Categories category = categoryOptional.get();

            Set<Product> products = category.getProducts();
            for (Product product : products) {
                Set<ProductImage> productImages = product.getProductImage();
                for (ProductImage productImage : productImages) {
                    productImageRepository.delete(productImage);
                }
                productRepository.delete(product);
            }

            categoriesProductRepository.delete(category);
        }
        return "redirect:/admin/category-product";
    }

}
