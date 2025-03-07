<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!-- SPRING FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<!-- JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>BeeBooks</title>

    <jsp:include page="/WEB-INF/views/khachhang/layouts/css.jsp"></jsp:include>
</head>
<body>
<main class="container">
    <div class="free">Miễn phí vận chuyển với đơn hàng trên 1.000.000VND</div>

    <jsp:include page="/WEB-INF/views/khachhang/layouts/header.jsp"></jsp:include>

    <%--open content --%>
    <div class="content">
        <!--banner-->
        <%--        <img src="${base }/img/bannerBooks.png" width="100%"--%>
        <%--             style="max-width: 85%; height: auto;max-height: 500px; display: block; margin: 0 auto;">--%>
        <div class="banner-container">
            <img src="${base }/img/bannerBooks.png" width="100%" alt="Banner 1" class="slide">
            <img src="${base }/img/slide_1.png" width="100%" alt="Banner 2" class="slide">
            <img src="${base }/img/slide_2.png" width="100%" alt="Banner 3" class="slide">
            <img src="${base }/img/slide_3.png" width="100%" alt="Banner 4" class="slide">
            <img src="${base }/img/slide_4.png" width="100%" alt="Banner 5" class="slide">
            <button class="prev" onclick="moveSlide(-1)">❮</button>
            <button class="next" onclick="moveSlide(1)">❯</button>
        </div>
        <!--open new item-->

        <div class="products">
            <div class="products-name">
                <a href="#"> SẢN PHẨM </a>
            </div>

            <div class="list-product" style="flex-wrap: wrap;">

                <c:forEach items="${productsWithPaging.data }" var="product">
                    <c:if test="${product.status eq true}">
                        <div class="item">

                            <div class="item-images">
                                <a href="${base }/product/details/${product.seo}"> <img
                                        src="${base }/upload/${product.avatar}"
                                        width="100%">
                                </a>
                            </div>
                            <div class="item-content">
                                <a href="${base }/product/details/${product.seo}">
                                        ${product.title } </a>
                                <div class="price" id="price${product.id}">
                                    <fmt:setLocale value="vi_VN"/>
                                    <fmt:formatNumber value="${product.price}" type="currency"/>
                                </div>

                            </div>
                        </div>

                        <script>
                            // Lấy ngày hiện tại
                            var currentDate = new Date();

                            // Chuyển đổi ngày bắt đầu và kết thúc từ dạng string sang đối tượng Date
                            var promotionStartDate = new Date("${product.promotion.startDate}");
                            var promotionEndDate = new Date("${product.promotion.endDate}");

                            // So sánh ngày hiện tại với ngày bắt đầu và kết thúc của chương trình khuyến mãi
                            var isInPromotion = currentDate >= promotionStartDate && currentDate <= promotionEndDate;

                            if (isInPromotion) {
                                if (${product.price} != ${product.price_sale}) {
                                    // Hiển thị giá khuyến mãi
                                    document.getElementById("price${product.id}").innerHTML = "<fmt:formatNumber value="${product.price_sale}" type="currency"/>";
                                    document.getElementById("price${product.id}").style.color = "red";
                                } else {
                                    // Hiển thị giá thông thường
                                    document.getElementById("price${product.id}").innerHTML = "<fmt:formatNumber value="${product.price}" type="currency"/>";
                                    document.getElementById("price${product.id}").style.color = "black";
                                }
                            }
                        </script>
                    </c:if>
                </c:forEach>
            </div>

            <!--open product-->


        </div>
        <!--close products-->

        <!--open about us -->
        <div class="about">
            <div class="about-us">
                <a href="${base }/introduction"> <img
                        src="${base }/img/qrcode.png" width="100%">
                </a>
                <h3 class="about-title">ABOUT US</h3>
                <div class="about-button">
                    <a href="${base }/introduction"> XEM NGAY </a>
                </div>
            </div>
            <div class="about-us">

                <p>BeeBooks là nhà sách được thành lập vào ngày 11/05/2019 với mục tiêu cung cấp các thể loại sách và
                    bảo tồn những giá trị đọc của tất cả mọi người.</p>

                <p>Trong suốt quãng thời gian 5 năm hoạt động, BeeBooks đã trở thành một địa chỉ đáng tin cậy trong lĩnh
                    vực bán sách, đặc biệt là sách cũ, với một sứ mệnh tôn vinh và bảo tồn giá trị của văn hóa đọc.
                    .</p>

                <p>Đặc biệt, phần lớn sản phẩm của nhà sách là sách cũ, với một tập hợp đa dạng các đầu sách
                    từ những thập kỷ trước đến những tác phẩm mới nhất. Chúng tôi tin rằng sách cũ mang lại
                    không chỉ giá trị về nội dung mà còn là giá trị về lịch sử và tính cổ điển, đồng thời giúp tạo ra
                    những trải nghiệm đọc sách độc đáo và đặc biệt cho độc giả.</p>

                <p>BeeBooks tự hào là nơi lưu giữ và cung cấp cho mọi người những tựa sách phong phú và đa dạng, từ
                    sách giáo khoa, sách kinh tế, sách ngoại ngữ, sách thiếu nhi đến văn học Việt Nam và văn học nước
                    ngoài.</p>

                <p>Hiện tại BeeBooks hoạt động và phát triển trên khắp các cả nước
                    thông qua các kênh bán hàng như Facebook, Website....Các bạn có thể đặt hàng và liên hệ với chúng
                    tôi qua
                    các kênh trên như sau:</p>

                <p>Instagram: BeeBooks.VN</p>

                <p>Facebook: BeeBooks - Cửa hàng sách</p>

                <p>Email: beebookvn123@gmail.com.vn</p>

                <p>Số điện thoại liên lạc: 0968769276 hoặc 0382556065</p>

                <p>Địa chỉ: 180 Đ. Tây Mỗ, Tây Mỗ, Nam Từ Liêm, Hà Nội.</p>
            </div>
        </div>
        <!--close about us-->

    </div>
    <!--close content-->

    <%--close content --%>
    <jsp:include page="/WEB-INF/views/khachhang/layouts/footer.jsp"></jsp:include>
    <%--    <div class="copyright">--%>
    <%--        Copyright <i class="far fa-copyright"></i> <a href="#">msic.</a> <a--%>
    <%--            href="#">Powered by Haravan</a>--%>
    <%--    </div>--%>
</main>

</body>
<jsp:include page="/WEB-INF/views/khachhang/layouts/js.jsp"></jsp:include>
</html>