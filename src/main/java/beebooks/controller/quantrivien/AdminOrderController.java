package beebooks.controller.quantrivien;


import beebooks.controller.BaseController;
import beebooks.dto.OrderSearchModel;
import beebooks.dto.PaymentStatus;
import beebooks.entities.*;
import beebooks.exception.CustomException;
import beebooks.exception.MessageCode;
import beebooks.repository.OrderProductRepository;
import beebooks.repository.OrderRepository;
import beebooks.repository.ProductRepository;
import beebooks.service.SaleorderProductsService;
import beebooks.service.SaleorderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@Slf4j
public class AdminOrderController extends BaseController{
	
	private final SaleorderService saleorderService;
	private final SaleorderProductsService saleorderProductsService;
	private final OrderProductRepository orderProductRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	public AdminOrderController(SaleorderService saleorderService, SaleorderProductsService saleorderProductsService,
								OrderProductRepository orderProductRepository, OrderRepository orderRepository, ProductRepository productRepository) {
		this.saleorderService = saleorderService;
		this.saleorderProductsService = saleorderProductsService;
		this.orderProductRepository = orderProductRepository;
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
	}

	@RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
	public String adminOrder(final Model model, final HttpServletRequest request)  {
		
		OrderSearchModel searchModel = new OrderSearchModel();
		searchModel.keyword = request.getParameter("keyword");
		searchModel.setPage(getCurrentPage(request));
		
		model.addAttribute("orderWithPaging", saleorderService.search(searchModel));
		model.addAttribute("searchModel", searchModel);

		return "quantrivien/order";
	}
	
	
	@RequestMapping(value = { "/admin/order-product" }, method = RequestMethod.GET)
	public String adminOrderProduct(final Model model, final HttpServletRequest request,
									@RequestParam("orderId") Integer orderId)  {
		
		OrderSearchModel searchModel1 = new OrderSearchModel();
		searchModel1.keyword = request.getParameter("keyword");
		searchModel1.setPage(getCurrentPage(request));
		searchModel1.orderId = super.getInteger(request, "orderId");
		Saleorder order = saleorderService.getById(orderId);

		model.addAttribute("order", saleorderProductsService.getById(searchModel1));
		model.addAttribute("orderProductWithPaging", saleorderProductsService.search(searchModel1));
		model.addAttribute("searchModel1", searchModel1);

		return "quantrivien/order-product";
	}

	@GetMapping("/delete-orderProduct/{id}")
	public String deleteOrderProduct(@PathVariable("id") Integer id) {
		orderRepository.deleteById(id);
		orderProductRepository.deleteBySaleOrder(id);

		Optional<Saleorder> saleorderOptional = orderRepository.findById(id);
		if (saleorderOptional.isPresent()) {
			Saleorder saleorder = saleorderOptional.get();

			Set<SaleorderProducts> saleOrderProducts = saleorder.getSaleOrderProducts();
			for (SaleorderProducts saleOrderProduct : saleOrderProducts) {
				orderProductRepository.delete(saleOrderProduct);
			}

			orderRepository.delete(saleorder);
		}
		return "redirect:/admin/order-product";
	}

	@PostMapping( "/admin/order-product/update-status")
	public String updatePaymentStatus(@RequestBody PaymentStatus paymentStatus){
		Saleorder saleorder = orderRepository.findById(paymentStatus.getSaleOrderId())
				.orElseThrow(() -> new CustomException(MessageCode.ORDER_IS_NULL));

		saleorder.setPaymentStatus(paymentStatus.getStatus());
		orderRepository.save(saleorder);
		return "redirect:/admin/order";
	}

	@RequestMapping(value = {"/admin/order-reject/update/{orderId}"}, method = RequestMethod.GET)
	public String adminEdit(final Model model, @PathVariable("orderId") int orderId) {
		Saleorder saleorder = saleorderService.getById(orderId);
		model.addAttribute("addCate", saleorder);

		return "quantrivien/reject-order";
	}

	@RequestMapping(value = {"/admin/order-reject/adds"}, method = RequestMethod.POST)
	public String adminAddSpringForm( final @ModelAttribute("addCate") Saleorder saleorder) {
		Saleorder saleorder1 = orderRepository.findById(saleorder.getId()).orElse(null);
		log.info("----------saleorder : " + saleorder);
		log.info("----------saleorder1 : " + saleorder1);
		saleorder1.setPaymentStatus(4);
		saleorder1.setReason(saleorder.getReason());

		List<SaleorderProducts> saleorderProducts = orderProductRepository.findBySaleOrder(saleorder1.getId());
		for (SaleorderProducts saleorderProduct : saleorderProducts) {
			Product product = saleorderProduct.getProduct(); // Lấy sản phẩm
			Integer quantity = saleorderProduct.getQuality(); // Lấy số lượng sản phẩm
			if (product != null && quantity != null) {
				product.setQuantity(product.getQuantity() + quantity);
				productRepository.save(product);
			}
		}
		orderRepository.save(saleorder1);

		return "redirect:/admin/order";
	}
}


