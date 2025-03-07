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
    <title>Tất cả sản phẩm</title>
    <link rel="stylesheet"
          href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css"
          integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p"
          crossorigin="anonymous"/>

    <jsp:include page="/WEB-INF/views/khachhang/layouts/css.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="${base}/css/collection.css">
    <style>
        /* Adjust the input field */
        .rounded-input {
            border-radius: 15px;
            padding: 10px 15px;
            border: 1px solid #ced4da;
            width: 30%;
        }

        /* Adjust the search button */
        .btn-search {
            border-radius: 15px;
            padding: 10px 20px;
            background-color: #007bff; /* Set your desired background color */
            color: #fff;
            transition: background-color 0.3s;
            border: none;
        }

        .btn-search:hover {
            background-color: #0056b3; /* Change to desired hover color */
        }

        /* Adjust the search icon */
        .btn-search i {
            margin-right: 5px;
        }

    </style>
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

            <li><a href="#">Danh mục</a></li>

            <li>/</li>

            <li>Tất cả sản phẩm</li>
        </ul>
    </div>
    <!-- open content -->
    <div class="content product-list">


        <form class="list" action="${base }/collection" method="get" style="width: 100%; display: flex;">
            <div class="content-bar">
                <div class="title-bar">
                    <div class="title-bar-p1">Danh mục sản phẩm</div>
                    <div class="filter-price">
                        <c:forEach items="${categories}" var="category">
                            <%--                            <a class="btn-category" id="categoryId"--%>
                            <%--                               href="${base}/collectionDetail?categoryId=${category.id}">${category.name }</a>--%>
                            <button type="button" class="btn-category" data-category-id="${category.id}"
                                    style="background: white; border: none;">${category.name }</button>
                        </c:forEach>
                    </div>
                </div>
            </div>


            <div class="content-collection">
                <div class="collection-title">
                    <div class="all-coll">
                        <h3>Tất cả sản phẩm</h3>
                    </div>
                </div>

                <div class="list-product" id="list-product" style="flex-wrap: wrap;">

                    <c:forEach items="${productsWithPaging.data }" var="product">
                        <c:if test="${product.status eq true}">
                            <div class="item" style="margin-left: 52px;">

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

            </div>
        </form>


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
<script type="text/javascript">


</script>
</html>