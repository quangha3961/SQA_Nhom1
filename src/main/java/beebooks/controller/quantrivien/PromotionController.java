package beebooks.controller.quantrivien;

import beebooks.controller.BaseController;
import beebooks.dto.SearchModel;
import beebooks.entities.Product;
import beebooks.entities.ProductImage;
import beebooks.entities.Promotion;
import beebooks.repository.ProductImageRepository;
import beebooks.repository.ProductRepository;
import beebooks.repository.PromotionRepository;
import beebooks.service.PromotionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Controller
public class PromotionController extends BaseController {

    private final PromotionService promotionService;
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public PromotionController(PromotionService promotionService, PromotionRepository promotionRepository,
                               ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.promotionService = promotionService;
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @RequestMapping(value = {"/admin/promotion/add"}, method = RequestMethod.GET)
    public String adminGet(final Model model) {

        Promotion addCate = new Promotion();
        model.addAttribute("addCate", addCate);

        return "quantrivien/add-promotion";
    }


    @RequestMapping(value = {"/admin/promotion/adds"}, method = RequestMethod.POST)
    public String adminAddSpringForm( final @ModelAttribute("addCate") Promotion promotion) {
        promotionService.saveOrUpdate(promotion);

        return "redirect:/admin/promotion/list";
    }

    @RequestMapping(value = {"/admin/promotion/list"}, method = RequestMethod.GET)
    public String adminList(final Model model, final HttpServletRequest request) {

        SearchModel searchModel = new SearchModel();
        searchModel.keyword = request.getParameter("keyword");
        searchModel.setPage(getCurrentPage(request));

        model.addAttribute("withPaging", promotionService.search(searchModel));
        model.addAttribute("searchModel", searchModel);

        return "quantrivien/promotion";
    }


    @RequestMapping(value = {"/admin/promotion/update/{promotionId}"}, method = RequestMethod.GET)
    public String adminEdit(final Model model, @PathVariable("promotionId") int promotionId) {
        Promotion promotion = promotionService.getById(promotionId);
        model.addAttribute("addCate", promotion);

        return "quantrivien/add-promotion";
    }

    @GetMapping("/deletePromotion/{id}")
    public String adminDelete(@PathVariable("id") Integer id) {

        Optional<Promotion> categoryOptional = promotionRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Promotion category = categoryOptional.get();

            Set<Product> products = category.getProducts();
            for (Product product : products) {
                Set<ProductImage> productImages = product.getProductImage();
                for (ProductImage productImage : productImages) {
                    productImageRepository.delete(productImage);
                }
                productRepository.delete(product);
            }

            promotionRepository.delete(category);
        }
        return "redirect:/admin/promotion/list";
    }

}
