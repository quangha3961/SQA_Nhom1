package beebooks.controller.khachhang;

import beebooks.controller.BaseController;
import beebooks.dto.ProductSearchModel;
import beebooks.entities.Product;
import beebooks.entities.Subcribe;
import beebooks.service.MailService;
import beebooks.service.PagerData;
import beebooks.service.ProductService;
import beebooks.service.SubcribeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class HomeController extends BaseController {
	
	private final ProductService productService;
	private final SubcribeService subcribeService;
	private final MailService mailService;

	public HomeController(ProductService productService, SubcribeService subcribeService, MailService mailService) {
		this.productService = productService;
		this.subcribeService = subcribeService;
		this.mailService = mailService;
	}


	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String home(final Model model, final HttpServletRequest request)
			throws IOException {

		Subcribe subcribe = new Subcribe();
		model.addAttribute("subcribe", subcribe);
		
		ProductSearchModel searchModel = new ProductSearchModel();
		searchModel.keyword = request.getParameter("keyword");
		
		model.addAttribute("productsWithPaging", productService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		return "khachhang/index";
	}

	@RequestMapping(value = { "/ajax/home", "/"}, method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> ajax_subcribe(final Model model,
			final HttpServletRequest request,
			final HttpServletResponse response, 
			final @RequestBody Subcribe subcribe) {
		Map<String, Object> jsonResult = new HashMap<String, Object>();
		model.addAttribute("subcribe", "");

		List<Subcribe> subcribes = subcribeService.checkEmailSubcribe(subcribe);
		if(subcribes.size() == 0) {
			subcribeService.saveOrUpdate(subcribe);
			jsonResult.put("code", 200);
			jsonResult.put("message", "Cảm ơn, " + subcribe.getEmail() + " đã đăng kí thành công!");
		} else {
			jsonResult.put("code",400);
			jsonResult.put("err", "Bạn chưa nhập email / Trùng email");
		}

		log.info("json result : " + jsonResult);
		//gửi email thông báo
		String to = subcribe.getEmail();
		String subject = "XÁC NHẬN " + subcribe.getEmail() + " ĐÃ ĐĂNG KÝ THÀNH CÔNG!";
		String text = "Chúng tôi sẽ gửi cho bạn những thông tin mới nhất về Beebooks"  + ".";
		mailService.sendEmail(to, subject, text);

		return ResponseEntity.ok(jsonResult);
	}
	
	@RequestMapping(value = { "/product/details/{productSeo}"}, method = RequestMethod.GET)
	public String productDetails(final Model model, final HttpServletRequest request,
			@PathVariable("productSeo") String productSeo) {
		
		ProductSearchModel searchModel = new ProductSearchModel();
		searchModel.seo = productSeo;
		
		PagerData<Product> products = productService.search(searchModel);
		Product product = products.getData().get(0);
		
		model.addAttribute("product", product);
		return "khachhang/details";
	}
}
