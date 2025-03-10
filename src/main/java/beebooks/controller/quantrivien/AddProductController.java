package beebooks.controller.quantrivien;


import beebooks.controller.BaseController;
import beebooks.dto.AddProduct;
import beebooks.dto.PaymentStatus;
import beebooks.dto.ProductSearchModel;
import beebooks.entities.Product;
import beebooks.exception.CustomException;
import beebooks.exception.MessageCode;
import beebooks.repository.ProductRepository;
import beebooks.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class AddProductController extends BaseController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    public AddProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }


    @RequestMapping(value = {"/admin/product/list", "/admin/product"}, method = RequestMethod.GET)
    public String adminProductList(final Model model, final HttpServletRequest request) {

        ProductSearchModel searchModel = new ProductSearchModel();
        searchModel.keyword = request.getParameter("keyword");
        searchModel.setPage(getCurrentPage(request));
        searchModel.categoryId = super.getInteger(request, "categoryId");

        model.addAttribute("productsWithPaging", productService.search(searchModel));
        model.addAttribute("searchModel", searchModel);

        return "quantrivien/product";
    }

    @RequestMapping(value = {"/admin/product/add-product"}, method = RequestMethod.GET)
    public String admin_add(final Model model, final HttpServletRequest request) {

        model.addAttribute("add", new Product());

        return "quantrivien/add-product";
    }

    @RequestMapping(value = {"/admin/product/add-product/{productId}"}, method = RequestMethod.GET)
    public String adminProductEdit(final Model model, @PathVariable("productId") int productId) {
        Product product = productService.getById(productId);
        model.addAttribute("add", product);

        return "quantrivien/add-product";
    }

    @GetMapping("/deleteProduct/{id}")
    @Transactional
    public String adminDelete(@PathVariable("id") Integer id) {
        log.info("--------Delete product with id: {}", id);
        Product product = productRepository.findById(id).orElse(null);
        product.setStatus(false);
        productRepository.save(product);
        return "redirect:/admin/product";
    }


    @RequestMapping(value = {"/admin/product/add-product"}, method = RequestMethod.POST)
    public String admin_addpost_spring_form(final Model model, final HttpServletRequest request, final @ModelAttribute("add") Product product,
                                            @RequestParam("productAvatar") MultipartFile productAvatar, // hứng file đẩy lên
                                            @RequestParam("productPictures") MultipartFile[] productPictures) throws Exception { // hứng file đẩy lên)
//        if (product.getPromotion() != null) {
//            Integer promotionId = product.getPromotion().getId();
//            Double promotionPercent = product.getPromotion().getPercent();
//
//            if (promotionId == 2 && promotionPercent != null && promotionPercent == 0) {
//                product.setPrice_sale(null);
//            } else if (promotionId != 2 && promotionPercent != null) {
//                BigDecimal discountAmount = product.getPrice()
//                        .multiply(BigDecimal.valueOf(promotionPercent))
//                        .divide(BigDecimal.valueOf(100));
//                product.setPrice_sale(product.getPrice().subtract(discountAmount));
//            }
//        }

        if (product.getId() == null || product.getId() <= 0) {
            productService.add(product, productAvatar, productPictures);
            productService.saveOrUpdate(product);
            return "redirect:/admin/product";
        }
        productService.update(product, productAvatar, productPictures);

        return "redirect:/admin/product";
    }

    @RequestMapping(value = {"/ajax/add-product"}, method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> ajax_contact(final @RequestBody AddProduct add) {
        Map<String, Object> jsonResult = new HashMap<String, Object>();
        jsonResult.put("code", 200);
        jsonResult.put("message", "Cảm ơn, bạn đã thêm " + add.getTenSP() + " thành công!");
        return ResponseEntity.ok(jsonResult);
    }

    @PostMapping( "/admin/product/update-status")
    public String updateStatus(@RequestBody PaymentStatus paymentStatus){
        Product product = productRepository.findById(paymentStatus.getSaleOrderId())
                .orElseThrow(() -> new CustomException(MessageCode.PRODUCT_IS_NULL));

        product.setProductStatus(paymentStatus.getStatus());
        productRepository.save(product);
        return "redirect:/admin/product";
    }

}
