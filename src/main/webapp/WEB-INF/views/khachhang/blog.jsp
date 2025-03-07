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
    <title>Blog</title>
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

            <li>BLOG</li>
        </ul>
    </div>
    <div class="content">

        <div class="content-products">
            <div class="products-name">
                <a href="#"> ĐÂY LÀ NHỮNG CHIA SẺ , CẢM NHẬN CỦA CHÚNG TÔI ĐÃ ĐƯỢC DIỄN TẢ BẰNG NHỮNG BLOG Ý NGHĨA </a>
            </div>
            <div class="list-product">

                <<c:forEach items="${blogsWithPaging.data }" var="blog">
                <c:if test="${blog.status eq true}">
                    <div class="item">

                        <div class="item-images">
                            <a href="${base }/blog/details/${blog.seo}"> <img
                                    src="${base }/upload/${blog.avatar}"
                                    width="100%">
                            </a>
                        </div>
                        <div class="item-content" style="text-align: center;">
                            <a href="${base }/blog/details/${blog.seo}">
                                    ${blog.title } </a>
                        </div>
                    </div>
                </c:if>

                <!-- Paging -->

            </c:forEach>

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