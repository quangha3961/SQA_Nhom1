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
<title>Thêm mới chương trình KM</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="/WEB-INF/views/quantrivien/layouts/css.jsp"></jsp:include>

</head>
<style>
	.input-group .input-group-text {
		background-color: #e9ecef;
		border-left: 0;
	}
	.input-group .form-control {
		border-right: 0;
	}
</style>


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
								<h4 class="header-title">Thêm mới chương trình KM</h4>
								<sf:form modelAttribute="addCate" class="needs-validation"
									novalidate="" action="${base }/admin/promotion/adds"
									method="post" enctype="multipart/form-data">

									<sf:hidden path="id" />

									<div class="form-row">

										<div class="col-md-4 mb-3">
											<label for="validationCustom02">Tên chương trình KM</label>
											<sf:input path="name" type="text" class="form-control"
												id="validationCustom02" placeholder="..."
												required="required" name="name" />
											<div class="valid-feedback">Looks good!</div>
										</div>

										<div class="col-md-4 mb-3">
											<label for="validationCustom02">Ngày bắt đầu</label>
											<sf:input path="startDate" type="date" class="form-control"
													  id="validationCustom02" required="required"/>
											<div class="valid-feedback">Looks good!</div>
										</div>

										<div class="col-md-4 mb-3">
											<label for="validationCustom03">Ngày kết thúc</label>
											<sf:input path="endDate" type="date" class="form-control"
													  id="validationCustom04" required="required"/>
											<div class="valid-feedback">Looks good!</div>
										</div>

									</div>
									<div class="form-row">
										<div class="col-md-6 mb-3">
											<label for="validationCustom03">Chiết khấu (%)</label>
											<div class="input-group" style="width: 30%;">
												<sf:input autocomplete="off" path="percent" type="text"
														  class="form-control" id="validationCustom03"
														  placeholder="00.00" name="percent" required="required" />
												<div class="input-group-append">
													<span class="input-group-text">%</span>
												</div>
												<div class="invalid-feedback">Hãy nhập chiết khấu đúng định dạng!</div>
											</div>
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

			document.addEventListener("DOMContentLoaded", function() {
				const discountInput = document.getElementById("validationCustom03");

				discountInput.addEventListener("input", function(event) {
					let value = event.target.value;

					value = value.replace(/[^0-9.]/g, '');

					const parts = value.split('.');
					if (parts.length > 2) {
						value = parts[0] + '.' + parts.slice(1).join('');
					}

					if (parts[1] && parts[1].length > 2) {
						value = parts[0] + '.' + parts[1].slice(0, 2);
					}

					if (parts[0].length > 2) {
						parts[0] = parts[0].slice(0, 2);
						value = parts[0] + (parts[1] ? '.' + parts[1] : '');
					}

					event.target.value = value;
				});

				discountInput.addEventListener("blur", function(event) {
					let value = event.target.value;

					if (value.startsWith('.')) {
						value = '0' + value;
					}

					if (!value.includes('.')) {
						value += '.00';
					} else {
						const parts = value.split('.');
						if (parts[1].length === 1) {
							value += '0';
						}
					}

					event.target.value = value;
				});
			});
		</script>
</body>

</html>
