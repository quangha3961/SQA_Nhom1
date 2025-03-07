<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!--import JSTL-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- import SPRING-FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html class="no-js" lang="en">

<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>Hủy đơn hàng</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="/WEB-INF/views/quantrivien/layouts/css.jsp"></jsp:include>

</head>

<body>
	<!--[if lt IE 8]>
            <p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
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
			<jsp:include page="/WEB-INF/views/quantrivien/layouts/title.jsp"></jsp:include>
			<!-- page title area end -->
			<div class="main-content-inner">
				<!-- sales report area start -->
				<div class="form-search">
					<div class="col-12">
						<div class="card mt-5">

							<c:if test="${not empty TB }">
								<div class="alert alert-primary" role="alert">${TB }</div>
							</c:if>

							<div class="card-body">
								<h4 class="header-title">Hủy đơn hàng</h4>
								<sf:form modelAttribute="addCate" class="needs-validation"
									novalidate="" action="${base }/admin/order-reject/adds"
									method="post" enctype="multipart/form-data">

									<sf:hidden path="id" />

									<div class="form-row">

									</div>
									<div class="form-row">
										<div class="col-md-6 mb-3">
											<label for="validationCustom03"></label>
											<sf:textarea autocomplete="off" path="reason" type="text"
												class="form-control" id="validationCustom03"
												placeholder="Lý do hủy đơn ..." name="address" required="required" rows="3" />
											<div class="invalid-feedback">Hãy nhập lý do!</div>
										</div>

									</div>

									<button class="btn btn-primary" type="submit">Lưu</button>
								</sf:form>
							</div>
						</div>
					</div>
				</div>

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
		<!-- internal javascript -->
		<script type="text/javascript">
			$(document).ready(function() {
				$('#validationCustom05').summernote();
			});
		</script>
</body>

</html>
