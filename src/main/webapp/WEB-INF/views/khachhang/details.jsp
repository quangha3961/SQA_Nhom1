<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!--import JSTL-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- import SPRING-FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${product.title }</title>
    <link rel="stylesheet"
          href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css"
          integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p"
          crossorigin="anonymous"/>
    <link rel="stylesheet" type="text/css" href="${base}/css/details.css">
    <jsp:include page="/WEB-INF/views/khachhang/layouts/css.jsp"></jsp:include>
<%--    <link rel="stylesheet" type="text/css" href="${base}/css/details.css">--%>
</head>
<body>
<main class="container">

    <div class="free">Miễn phí vận chuyển với đơn hàng trên 1.000.000VND</div>
    <!--open header-->
    <jsp:include page="/WEB-INF/views/khachhang/layouts/header.jsp"></jsp:include>
    <!--close header-->

    <div class="navigation">
        <ul>
            <li><a href="${base }/home">Trang chủ </a></li>

            <li>/</li>

            <li><a href="#">${product.categories.name }</a></li>

            <li>/</li>

            <li>${product.title }</li>
        </ul>
    </div>
    <div class="content">

        <div class="content-cart">
            <div class="thongtin">
                <c:if test="${ product.quantity <= 0 }">
                    <div class="TB" style="color: red;">
                        Sản phẩm này trong kho đã hết!!!
                    </div>
                </c:if>
                <div class="tt-title">
                    <h1>${product.title }</h1>
                    <h4>Tác giả : ${product.author.name }</h4>
                    <h4>Nhà xuất bản : ${product.manufacturer.name }</h4>
                    <h4>Năm xuất bản : ${product.publicationYear}</h4>
                </div>
                <div class="tt-price" id="price${product.id}">
                    <fmt:setLocale value="vi_VN"/>
                    <fmt:formatNumber value="${product.price}" type="currency"/>
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

                <div class="tt-cart">

                    <div class="add">
                        <button type="button" class="add-cart"
                                onclick="javascript:AddToCart('${base }', ${product.id}, 1);">
                            THÊM VÀO GIỎ
                        </button>
                    </div>
                </div>
                <div class="tt-mota">
<%--                    <h3>Mô tả</h3>--%>
                    <p>${product.shortDes}</p>
                </div>
            </div>
            <div class="sanpham">
                <img src="${base }/upload/${product.avatar}" width="65%">
            </div>
        </div>

    </div>
    <!--close content-->

    <!--open footer -->
    <jsp:include page="/WEB-INF/views/khachhang/layouts/footer.jsp"></jsp:include>
    <!--close footer-->
    <div class="copyright">
        Copyright <i class="far fa-copyright"></i> <a href="#">msic.</a> <a
            href="#">Powered by Haravan</a>
    </div>
</main>
</body>

<jsp:include page="/WEB-INF/views/khachhang/layouts/js.jsp"></jsp:include>
</html>