package beebooks.controller.quantrivien;

import beebooks.controller.BaseController;
import beebooks.dto.SearchModel;
import beebooks.entities.Manufacturer;
import beebooks.entities.Product;
import beebooks.entities.ProductImage;
import beebooks.repository.ManufacturerRepository;
import beebooks.repository.ProductImageRepository;
import beebooks.repository.ProductRepository;
import beebooks.service.ManufacturerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Controller
public class ManufacturerController extends BaseController {

    private final ManufacturerService manufacturerService;
    private final ManufacturerRepository manufacturerRepository;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public ManufacturerController(ManufacturerService manufacturerService, ManufacturerRepository manufacturerRepository,
                                  ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.manufacturerService = manufacturerService;
        this.manufacturerRepository = manufacturerRepository;
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @RequestMapping(value = {"/admin/manufacturer/add"}, method = RequestMethod.GET)
    public String adminGet(final Model model) {

        Manufacturer addCate = new Manufacturer();
        model.addAttribute("addCate", addCate);

        return "quantrivien/add-manufacturer";
    }


    @RequestMapping(value = {"/admin/manufacturer/adds"}, method = RequestMethod.POST)
    public String adminAddSpringForm( final @ModelAttribute("addCate") Manufacturer manufacturer) {
        manufacturerService.saveOrUpdate(manufacturer);

        return "redirect:/admin/manufacturer/list";
    }

    @RequestMapping(value = {"/admin/manufacturer/list"}, method = RequestMethod.GET)
    public String adminList(final Model model, final HttpServletRequest request) {

        SearchModel searchModel = new SearchModel();
        searchModel.keyword = request.getParameter("keyword");
        searchModel.setPage(getCurrentPage(request));

        model.addAttribute("withPaging", manufacturerService.search(searchModel));
        model.addAttribute("searchModel", searchModel);

        return "quantrivien/manufacturer";
    }


    @RequestMapping(value = {"/admin/manufacturer/update/{manufacturerId}"}, method = RequestMethod.GET)
    public String adminEdit(final Model model, @PathVariable("manufacturerId") int manufacturerId) {
        Manufacturer manufacturer = manufacturerService.getById(manufacturerId);
        model.addAttribute("addCate", manufacturer);

        return "quantrivien/add-manufacturer";
    }

    @GetMapping("/deleteManufacturer/{id}")
    public String adminDelete(@PathVariable("id") Integer id) {

        Optional<Manufacturer> categoryOptional = manufacturerRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Manufacturer category = categoryOptional.get();

            Set<Product> products = category.getProducts();
            for (Product product : products) {
                Set<ProductImage> productImages = product.getProductImage();
                for (ProductImage productImage : productImages) {
                    productImageRepository.delete(productImage);
                }
                productRepository.delete(product);
            }

            manufacturerRepository.delete(category);
        }
        return "redirect:/admin/manufacturer/list";
    }

}
