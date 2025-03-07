package beebooks.controller.khachhang;

import beebooks.controller.BaseController;
import beebooks.dto.Cart;
import beebooks.dto.CartItem;
import beebooks.entities.Product;
import beebooks.entities.Saleorder;
import beebooks.entities.SaleorderProducts;
import beebooks.service.MailService;
import beebooks.service.ProductService;
import beebooks.service.SaleorderService;
import beebooks.ultilities.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class CartController extends BaseController {

    private final ProductService productService;
    private final SaleorderService saleOrderService;
    private final MailService mailService;

    public CartController(ProductService productService, SaleorderService saleOrderService, MailService mailService) {
        this.productService = productService;
        this.saleOrderService = saleOrderService;
        this.mailService = mailService;
    }

    @RequestMapping(value = {"/cart/checkout"}, method = RequestMethod.POST)
    public String cartFinish(final ModelMap model, final HttpServletRequest request, final HttpServletResponse response) {

        Map<String, Object> jsonResult = new HashMap<>();

        String customerFullName = request.getParameter("customer_name");
        String customerAddress = request.getParameter("customer_address");
        String customerEmail = request.getParameter("customer_email");
        String customerPhone = request.getParameter("customer_phone");
        if (customerFullName == null || customerFullName.isEmpty()) {
            jsonResult.put("error", "Vui lòng nhập họ tên");
        } else if(customerAddress == null || customerAddress.isEmpty()) {
            jsonResult.put("error", "Vui lòng nhập địa chỉ");
        } else if (customerEmail == null || customerEmail.isEmpty()) {
            jsonResult.put("error", "Vui lòng nhập email");
        } else if (customerPhone == null || customerPhone.isEmpty()) {
            jsonResult.put("error", "Vui lòng nhập số điện thoại");
        }

        Saleorder saleOrder = new Saleorder();

        saleOrder.setCustomer_name(customerFullName);
        saleOrder.setCustomer_email(customerEmail);
        saleOrder.setCustomer_address(customerAddress);
        saleOrder.setCustomer_phone(customerPhone);

        saleOrder.setCode(StringUtil.generateOrderCode());
        saleOrder.setPaymentType(1);
        saleOrder.setPaymentStatus(1);

        BigDecimal totalQuality = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart != null) {
            for (CartItem cartItem : cart.getCartItems()) {
                SaleorderProducts saleOrderProducts = new SaleorderProducts();
                Product product = productService.getById(cartItem.getProductId());
                saleOrderProducts.setProduct(productService.getById(cartItem.getProductId()));
                saleOrderProducts.setQuality(cartItem.getQuanlity());
                saleOrderProducts.setTotal(cartItem.getPriceUnit().doubleValue() * cartItem.getQuanlity());

                // Giảm số lượng sản phẩm trong kho
                int remainingQuantity = product.getQuantity() - cartItem.getQuanlity();
                if (remainingQuantity < 0) {
                    jsonResult.put("error", "Xin lỗi , sản phẩm " + product.getTitle() + " này đã hết");
                } else {
                    product.setQuantity(remainingQuantity);
                    // Cập nhật sản phẩm trong cơ sở dữ liệu
                    productService.saveOrUpdate(product);

                    totalQuality = totalQuality.add(BigDecimal.valueOf(cartItem.getQuanlity()));
                    totalPrice = totalPrice.add(cartItem.getPriceUnit().multiply(BigDecimal.valueOf(cartItem.getQuanlity())));
                    saleOrder.addSaleOrderProducts(saleOrderProducts);
                }
            }
        }
        saleOrder.setTotal(totalQuality);
        saleOrder.setTotalPrice(totalPrice);

        saleOrderService.saveOrUpdate(saleOrder);

        session.setAttribute("cart", null);
        session.setAttribute("totalItems", "0");

        //gửi email đơn hàng
        String to = saleOrder.getCustomer_email();
        String subject = "XÁC NHẬN ĐƠN HÀNG #" + saleOrder.getCode();
        String text = "Cảm ơn bạn đã đặt hàng. Mã đơn hàng của bạn là #" + saleOrder.getCode() + ".";
        mailService.sendEmail(to, subject, text);

        return "redirect:/home";
    }

    @RequestMapping(value = {"/cart/view"}, method = RequestMethod.GET)
    public String cartView(final Model model, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        return "khachhang/cart";
    }

    @GetMapping("cart/remove/{productId}")
    public String removeProduct(final HttpServletRequest request, @PathVariable("productId") int productId) {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        List<CartItem> cartItem = cart.getCartItems();
        Product product = productService.getById(productId);

        var index = 0;
        for (int i = 0; i < cartItem.size(); i++) {
            if (cartItem.get(i).getProductId() == productId) {
                index = i;
                break;
            }
        }
        cartItem.remove(index);
        this.calculateTotalPrice(request);
        return "redirect:/cart/view";
    }

    @RequestMapping(value = {"/ajax/updateQuanlityCart"}, method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> ajax_UpdateQuanlityCart(final Model model, final HttpServletRequest request,
                                                                       final HttpServletResponse response, final @RequestBody CartItem cartItem) {

        HttpSession session = request.getSession();

        Cart cart = null;
        if (session.getAttribute("cart") != null) {
            cart = (Cart) session.getAttribute("cart");
        } else {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        List<CartItem> cartItems = cart.getCartItems();

        int currentProductQuality = 0;
        for (CartItem item : cartItems) {
            if (item.getProductId() == cartItem.getProductId()) {
                currentProductQuality = item.getQuanlity() + 1;
                item.setQuanlity(currentProductQuality);
            }
        }

        this.calculateTotalPrice(request);

        Map<String, Object> jsonResult = new HashMap<String, Object>();
        jsonResult.put("code", 200);
        jsonResult.put("status", "TC");
        jsonResult.put("totalItems", getTotalItems(request));
        jsonResult.put("currentProductQuality", currentProductQuality);

        session.setAttribute("totalItems", getTotalItems(request));
        return ResponseEntity.ok(jsonResult);
    }

    @RequestMapping(value = {"/ajax/truQuanlityCart"}, method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> ajax_TruQuanlityCart(final Model model, final HttpServletRequest request,
                                                                    final HttpServletResponse response, final @RequestBody CartItem cartItem) {

        HttpSession session = request.getSession();

        Cart cart = null;
        if (session.getAttribute("cart") != null) {
            cart = (Cart) session.getAttribute("cart");
        } else {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        List<CartItem> cartItems = cart.getCartItems();

        int ciProductQuality = 0;
        for (CartItem item : cartItems) {
            if (item.getProductId() == cartItem.getProductId()) {
                ciProductQuality = item.getQuanlity() - 1;
                item.setQuanlity(ciProductQuality);
            }
        }

        this.calculateTotalPrice(request);

        Map<String, Object> jsonResult = new HashMap<String, Object>();
        jsonResult.put("code", 200);
        jsonResult.put("status", "TC");
        jsonResult.put("totalItems", getTotalItems(request));
        jsonResult.put("ciProductQuality", ciProductQuality);

        session.setAttribute("totalItems", getTotalItems(request));
        return ResponseEntity.ok(jsonResult);
    }


    @RequestMapping(value = {"/ajax/addToCart"}, method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> ajax_AddToCart(final Model model, final HttpServletRequest request,
                                                              final HttpServletResponse response, final @RequestBody CartItem cartItem) {
        Map<String, Object> jsonResult = new HashMap<String, Object>();

        // Kiểm tra số lượng tồn kho của sản phẩm
        Product productInDb = productService.getById(cartItem.getProductId());
        if (productInDb == null || productInDb.getQuantity() < cartItem.getQuanlity()) {
            jsonResult.put("code", 400);
            jsonResult.put("status", "out_of_stock");
            log.info("-------------json :" + jsonResult);
            return ResponseEntity.badRequest().body(jsonResult);
        }

        log.info("-------------json1 :" + jsonResult);
        HttpSession session = request.getSession();

        Cart cart = null;
        if (session.getAttribute("cart") != null) {
            cart = (Cart) session.getAttribute("cart");
        } else {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        List<CartItem> cartItems = cart.getCartItems();

        boolean isExists = false;
        for (CartItem item : cartItems) {
            if (item.getProductId() == cartItem.getProductId()) {
                isExists = true;
                item.setQuanlity(item.getQuanlity() + cartItem.getQuanlity());
            }
        }

        if (!isExists) {
//            Product productInDb = productService.getById(cartItem.getProductId());

            cartItem.setProductName(productInDb.getTitle());
            if (productInDb.getPrice_sale() != null){
                cartItem.setPriceUnit(productInDb.getPrice_sale());
            } else {
                cartItem.setPriceUnit(productInDb.getPrice());
            }

            cart.getCartItems().add(cartItem);
        }

        this.calculateTotalPrice(request);

//        Map<String, Object> jsonResult = new HashMap<String, Object>();
        jsonResult.put("code", 200);
        jsonResult.put("status", "TC");
        jsonResult.put("totalItems", getTotalItems(request));

        session.setAttribute("totalItems", getTotalItems(request));
        return ResponseEntity.ok(jsonResult);
    }

    private int getTotalItems(final HttpServletRequest request) {
        HttpSession httpSession = request.getSession();

        if (httpSession.getAttribute("cart") == null) {
            return 0;
        }

        Cart cart = (Cart) httpSession.getAttribute("cart");
        List<CartItem> cartItems = cart.getCartItems();

        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuanlity();
        }

        return total;
    }

    private void calculateTotalPrice(final HttpServletRequest request) {

        HttpSession session = request.getSession();

        Cart cart = null;
        if (session.getAttribute("cart") != null) {
            cart = (Cart) session.getAttribute("cart");
        } else {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        List<CartItem> cartItems = cart.getCartItems();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem ci : cartItems) {
            total = total.add(ci.getPriceUnit().multiply(BigDecimal.valueOf(ci.getQuanlity())));
        }

        cart.setTotalPrice(total);
    }
}
