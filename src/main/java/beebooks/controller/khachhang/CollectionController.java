package beebooks.controller.khachhang;

import beebooks.controller.BaseController;
import beebooks.dto.ProductSearchModel;
import beebooks.entities.Categories;
import beebooks.entities.Product;
import beebooks.repository.ProductRepository;
import beebooks.service.CategoriesService;
import beebooks.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class CollectionController extends BaseController {

	private final ProductService productService;
	private final CategoriesService categoriesService;
	private final ProductRepository productRepository;

	public CollectionController(ProductService productService, CategoriesService categoriesService, ProductRepository productRepository) {
		this.productService = productService;
		this.categoriesService = categoriesService;
		this.productRepository = productRepository;
	}

	@RequestMapping(value = { "/collection" }, method = RequestMethod.GET)
	public String collectionView(final Model model, final HttpServletRequest request) {
		ProductSearchModel searchModel = new ProductSearchModel();
		searchModel.keyword = request.getParameter("keyword");
		searchModel.setPage(getCurrentPage(request));
		searchModel.categoryId = super.getInteger(request, "categoryId");

//		model.addAttribute("product", productService.getByCategoryId(searchModel));
		model.addAttribute("productsWithPaging", productService.search(searchModel));
		model.addAttribute("searchModel", searchModel);

//		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
//			return "khachhang/collection :: list-product";
//		}
		return "khachhang/collection";
	}

	@RequestMapping(value = { "/collection/{categoryId}" }, method = RequestMethod.GET)
	public String collectionByCategory(final Model model, @PathVariable("categoryId") Integer categoryId) {

		List<Product> product = productRepository.findByCategories(categoryId);
		model.addAttribute("product", product);

		return "khachhang/collection";
	}

//	@RequestMapping(value = { "/collectionDetail" }, method = RequestMethod.GET)
//	public String collectionViewDetail(final Model model, final HttpServletRequest request) {
//		ProductSearchModel searchModel = new ProductSearchModel();
//		searchModel.keyword = request.getParameter("keyword");
//		searchModel.setPage(getCurrentPage(request));
//		searchModel.categoryId = super.getInteger(request, "categoryId");
//
//		model.addAttribute("productsWithPaging", productService.search(searchModel));
//		return "khachhang/collectionDetail";
//	}

}
