<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<!doctype html>
<html class="no-js" lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Thống kê</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Include CSS files -->
    <jsp:include page="/WEB-INF/views/quantrivien/layouts/css.jsp"></jsp:include>
    <style>
        .report-item {
            margin-right: 15px;
        }

        .report-item .title {
            font-weight: bold;
            font-style: italic;
            margin-bottom: 5px;
        }

        .report-item .number {
            font-size: 18px;
            font-weight: bold;
            color : #ff2407;
        }

    </style>
</head>
<body>
<div class="page-container">
    <!-- Include sidebar -->
    <jsp:include page="/WEB-INF/views/quantrivien/layouts/sidebar.jsp"></jsp:include>

    <div class="main-content">
        <!-- Include header -->
        <jsp:include page="/WEB-INF/views/quantrivien/layouts/header.jsp"></jsp:include>
        <!-- Include title -->
        <jsp:include page="/WEB-INF/views/quantrivien/layouts/title.jsp"></jsp:include>
        <div class="main-content-inner">
            <!-- Your statistics content goes here -->
            <div class="card mt-5">
                <div class="card-body">
                    <h4 class="header-title">Thống kê</h4>
                    <form class="list" action="${base }/admin/report" method="get">
                        <div class="main-content-inner">
                            <!-- sales report area start -->

                            <div class="card-body"
                                 style="display: flex; justify-content: space-between">
                                <div style="display: flex; padding-right: 15px">

                                    <div class="report-item">
                                        <div class="title">TỔNG SỐ LƯỢNG SẢN PHẨM HIỆN TẠI </div>
                                        <div class="number">${productWithPaging}</div>
                                    </div>
                                    <div class="report-item">
                                        <div class="title">TỔNG SỐ ĐƠN HÀNG </div>
                                        <div class="number">${orderWithPaging}</div>
                                    </div>
                                    <div class="report-item">
                                        <div class="title">TỔNG SỐ LƯỢNG SẢN PHẨM ĐÃ BÁN </div>
                                        <div class="number">${saleOrderWithPaging}</div>
                                    </div>
                                    <div class="report-item">
                                        <div class="title">TỔNG DOANH THU </div>
                                        <div class="number">${saleOrderWithPagings}</div>
                                    </div>

                                </div>

                            </div>
                        </div>
                        <!-- Dark table start -->
                        <!-- Dark table end -->


                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Include footer -->
</div>

<!-- Include offset area -->
<jsp:include page="/WEB-INF/views/quantrivien/layouts/offset.jsp"></jsp:include>
<!-- Include JS files -->
<jsp:include page="/WEB-INF/views/quantrivien/layouts/js.jsp"></jsp:include>
<!-- Include internal JavaScript -->
<script type="text/javascript">

</script>

</body>
</html>
