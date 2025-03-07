<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!--import JSTL-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- import SPRING-FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Giới thiệu</title>
<link rel="stylesheet"
	href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css"
	integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p"
	crossorigin="anonymous" />
<jsp:include page="/WEB-INF/views/khachhang/layouts/css.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" href="${base}/css/introduction.css">

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

				<li>Giới thiệu</li>

				
			</ul>
		</div>
		<!-- open content -->
		<div class="content">
			<div class="content-title">
				<h4>Danh mục trang</h4>
				<div class="layer">
					<ul>
						<li><a href="#">Tìm kiếm</a></li>
						<li><a href="${base }/introduction">Giới thiệu</a></li>
						<li><a href="#">Chính sách đổi trả</a></li>
						<li><a href="#">Chính sách bảo mật</a></li>
						<li><a href="#">Điều khoản dịch vụ</a></li>
					</ul>
				</div>
			</div>
			<div class="content-intro">
				<h3>Lời giới thiệu</h3>
				<div class="about-us">
					<p>BeeBooks là nhà sách được thành lập vào ngày 11/05/2019 với mục tiêu cung cấp các thể loại sách và
						bảo tồn những giá trị đọc của tất cả mọi người.</p>

					<p>Trong suốt quãng thời gian 5 năm hoạt động, BeeBooks đã trở thành một địa chỉ đáng tin cậy trong lĩnh
						vực bán sách, đặc biệt là sách cũ, với một sứ mệnh tôn vinh và bảo tồn giá trị của văn hóa đọc. .</p>

					<p>Đặc biệt, phần lớn sản phẩm của nhà sách là sách cũ, với một tập hợp đa dạng các đầu sách
						từ những thập kỷ trước đến những tác phẩm mới nhất. Chúng tôi tin rằng sách cũ mang lại
						không chỉ giá trị về nội dung mà còn là giá trị về lịch sử và tính cổ điển, đồng thời giúp tạo ra
						những trải nghiệm đọc sách độc đáo và đặc biệt cho độc giả.</p>

					<p>BeeBooks tự hào là nơi lưu giữ và cung cấp cho mọi người những tựa sách phong phú và đa dạng, từ
						sách giáo khoa, sách kinh tế, sách ngoại ngữ, sách thiếu nhi đến văn học Việt Nam và văn học nước ngoài.</p>

					<p>Hiện tại BeeBooks hoạt động và phát triển trên khắp các cả nước
						thông qua các kênh bán hàng như Facebook, Website....Các bạn có thể đặt hàng và liên hệ với chúng tôi qua
						các kênh trên như sau:</p>

					<p>Instagram: BeeBooks.VN</p>

					<p>Facebook: BeeBooks - Cửa hàng sách</p>

					<p>Email: beebookvn123@gmail.com.vn</p>

					<p>Số điện thoại liên lạc: 0968769276 hoặc 0382556065</p>

					<p>Địa chỉ: 180 Đ. Tây Mỗ, Tây Mỗ, Nam Từ Liêm, Hà Nội.</p>
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
<script type="text/javascript">
	
</script>
</html>