<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!-- SPRING FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<!-- JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Blog</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:include page="/WEB-INF/views/quantrivien/layouts/css.jsp"></jsp:include>

</head>

<body>
<!--[if lt IE 8]>
<p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> to improve your experience.</p>
<![endif]-->
<!-- preloader area start -->
<div id="preloader">
    <div class="loader"></div>
</div>
<!-- preloader area end -->
<!-- page container area start -->
<div class="page-container">
    <!-- sidebar menu area start -->
    <jsp:include page="/WEB-INF/views/quantrivien/layouts/sidebar.jsp"></jsp:include>
    <!-- sidebar menu area end -->
    <!-- main content area start -->
    <div class="main-content">
        <!-- header area start -->
        <jsp:include page="/WEB-INF/views/quantrivien/layouts/header.jsp"></jsp:include>
        <!-- header area end -->
        <!-- page title area start -->
        <
        <jsp:include page="/WEB-INF/views/quantrivien/layouts/title.jsp"></jsp:include>
        <!-- page title area end -->
        <form class="list" action="${base }/admin/blog" method="get">
            <div class="main-content-inner">
                <!-- sales report area start -->

                <div class="card-body"
                     style="display: flex; justify-content: space-between">
                    <div style="display: flex; padding-right: 15px">
                        <input type="hidden" id="page" name="page" class="form-control">
                        <input type="text" id="keyword" name="keyword" class="form-control" placeholder="Tìm kiếm"
                               value="${searchModel.keyword }"
                               style="margin-right: 5px; height: 46px;">
                        <select class="form-control" name="categoryId" id="categoryId"
                                style="margin-right: 5px; height: 46px;">
                            <c:forEach items="${categoriesBlog}" var="category">
                                <option value="${category.id }">${category.name }</option>
                            </c:forEach>
                        </select>
                        <button type="submit" id="btnSearch" name="btnSearch"
                                value="Search" class="btn btn-flat btn-outline-secondary mb-3">Tìm kiếm
                        </button>
                    </div>
                    <div>
                        <button type="button"
                                class="btn btn-flat btn-outline-secondary mb-3">
                            <a href="${base }/admin/blog/add-blog"> Thêm mới Blog</a>
                        </button>

                    </div>
                </div>
            </div>
            <!-- Dark table start -->
            <!-- Dark table end -->

            <div class="single-table"
                 style="margin: 0px 30px; padding-bottom: 15px">
                <div class="table-responsive">
                    <table class="table text-center">
                        <thead class="text-uppercase bg-primary">
                        <tr class="text-white">
                            <th scope="col">STT</th>
                            <th scope="col">Tiêu đề</th>
                            <%--                            <th scope="col">Trạng thái</th>--%>
                            <th scope="col">Ngày tạo</th>
                            <th scope="col">Hình ảnh</th>
                            <th scope="col">Chức năng</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${blogsWithPaging.data}" var="blog" varStatus="loop">
                            <c:if test="${blog.status eq true}">
                                <tr>
                                    <th scope="row">${loop.index + 1}</th>
                                    <td>${blog.title }</td>

                                        <%--                                <td>--%>
                                        <%--                                    <input type="hidden" id="_product_status_${blog.id}" value="${blog.status}" />--%>
                                        <%--                                    <label class="status-toggle" for="_product_status_${blog.id}"></label>--%>
                                        <%--                                </td>--%>
                                    <td>${blog.createdDate }</td>

                                    <td><img src="${base }/upload/${blog.avatar}"
                                             width="100" height="100"></td>
                                    <td><a class="btn btn-primary"
                                           href="${base }/admin/blog/add-blog/${blog.id}" role="button">Sửa</a>
                                        <a class="btn btn-danger" role="button"
                                           href="${base }/deleteBlog/${blog.id}">Xóa</a></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>

                </div>
            </div>

            <!-- Paging -->
            <%--                    <div class="row">--%>
            <%--                        <div class="col-12 d-flex justify-content-center">--%>
            <%--                            <div id="paging">--%>
            <%--                                --%>
            <%--                            </div>--%>
            <%--                        </div>--%>
            <%--                    </div>--%>

        </form>

    </div>

    <!-- main content area end -->
    <!-- footer area start-->
    <footer>
        <div class="footer-area">
            <p>
                © Copyright 2018. All right reserved. Template by <a
                    href="https://colorlib.com/wp/">Colorlib</a>.
            </p>
        </div>
    </footer>
    <!-- footer area end-->
</div>
<!-- page container area end -->
<!-- offset area start -->
<jsp:include page="/WEB-INF/views/quantrivien/layouts/offset.jsp"></jsp:include>
<jsp:include page="/WEB-INF/views/quantrivien/layouts/js.jsp"></jsp:include>
</body>
<script type="text/javascript">

</script>

</html>
