package beebooks.controller;

import beebooks.conf.VnpayConfig;
import beebooks.dto.Cart;
import beebooks.dto.CartItem;
import beebooks.dto.PaymentResDto;
import beebooks.entities.Product;
import beebooks.entities.Saleorder;
import beebooks.entities.SaleorderProducts;
import beebooks.service.ProductService;
import beebooks.service.SaleorderService;
import beebooks.ultilities.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Slf4j
public class PaymentController {
    private final ProductService productService;
    private final SaleorderService saleOrderService;

    public PaymentController(ProductService productService, SaleorderService saleOrderService) {
        this.productService = productService;
        this.saleOrderService = saleOrderService;
    }

    @RequestMapping(value = {"/api/payment/create_payment"}, method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> createPayment(HttpServletRequest req, Model model,
                                                             @RequestBody Map<String, String> payload) throws UnsupportedEncodingException {
        Map<String, Object> jsonResult = new HashMap<>();

        String customerFullName = payload.get("customer_name");
        String customerAddress = payload.get("customer_address");
        String customerEmail = payload.get("customer_email");
        String customerPhone = payload.get("customer_phone");
        log.info("--------customer_name : " + customerFullName);

        Saleorder saleOrder = new Saleorder();

        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Integer quantity = 0;
        Integer totalPrice = 0;
        BigDecimal totalPrices = BigDecimal.ZERO;
        if (cart != null) {
            for (CartItem cartItem : cart.getCartItems()) {
                quantity += cartItem.getQuanlity();
                totalPrice += cartItem.getPriceUnit().intValue() * cartItem.getQuanlity();

                SaleorderProducts saleOrderProducts = new SaleorderProducts();
                Product product = productService.getById(cartItem.getProductId());
                saleOrderProducts.setProduct(productService.getById(cartItem.getProductId()));
                saleOrderProducts.setQuality(cartItem.getQuanlity());
                saleOrderProducts.setTotal(cartItem.getPriceUnit().doubleValue() * cartItem.getQuanlity());

                saleOrder.setTotal(BigDecimal.valueOf(saleOrderProducts.getQuality()));
                // Giảm số lượng sản phẩm trong kho
                int remainingQuantity = product.getQuantity() - cartItem.getQuanlity();
                if (remainingQuantity < 0) {
                    jsonResult.put("error", "Xin lỗi , sản phẩm " + product.getTitle() + " này đã hết");
                } else {
                    product.setQuantity(remainingQuantity);
                    // Cập nhật sản phẩm trong cơ sở dữ liệu
                    productService.saveOrUpdate(product);
                    totalPrices = totalPrices.add(cartItem.getPriceUnit().multiply(BigDecimal.valueOf(cartItem.getQuanlity())));
                    saleOrder.addSaleOrderProducts(saleOrderProducts);
                }

                saleOrder.setCustomer_name(customerFullName);
                log.info("--------saleOrder.getCustomer_name : " + saleOrder.getCustomer_name());
                saleOrder.setCustomer_email(customerEmail);
                saleOrder.setCustomer_address(customerAddress);
                saleOrder.setCustomer_phone(customerPhone);

                saleOrder.setCode(StringUtil.generateOrderCode());
                saleOrder.setPaymentType(2);
                saleOrder.setPaymentStatus(1);
            }
            saleOrder.setTotalPrice(totalPrices);
            saleOrderService.saveOrUpdate(saleOrder);
        }
        //mua hàng xong , xóa session
        session.setAttribute("cart", null);
        session.setAttribute("totalItems", "0");

        String orderType = "other";
        long amount = totalPrice * 100;
        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnpayConfig.getIpAddress(req);
        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnpayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 10);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;

        PaymentResDto paymentResDto = new PaymentResDto();
        paymentResDto.setStatus("OK");
        paymentResDto.setMessage("Successfully");
        paymentResDto.setUrl(paymentUrl);

        model.addAttribute("paymentUrl", paymentUrl);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("paymentUrl", paymentUrl);

        // Trả về đối tượng ResponseEntity chứa thông tin phản hồi
        return ResponseEntity.ok(responseData);
    }
}
