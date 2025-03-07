$("#btn-sidebar").on('click', function(event) {
	event.preventDefault();
	$('.menu').css('left', '0px');
});

$(document).mouseup(function(event) {
	var menu = $('.menu');
	if (!menu.is(event.target) && menu.has(event.target).length == 0) {
		menu.css('left', '-320px');
	}
});

let slideIndex = 0;
let slideInterval; // Biến để lưu trữ ID của hẹn giờ
let isAutoSlide = false; // Biến flag để kiểm soát chuyển đổi tự động

function startSlideShow() {
	isAutoSlide = true; // Kích hoạt chuyển đổi tự động
	slideInterval = setInterval(showSlides, 2000);
}

function showSlides() {
	if (!isAutoSlide) return; // Nếu chuyển đổi tự động đã bị tắt, dừng hiển thị ảnh

	let i;
	let slides = document.getElementsByClassName("slide");

	// Ẩn tất cả các ảnh
	for (i = 0; i < slides.length; i++) {
		slides[i].style.display = "none";
	}

	// Tăng chỉ số ảnh lên 1 (chuyển đến ảnh tiếp theo)
	slideIndex++;

	// Nếu đã đến ảnh cuối cùng, quay lại ảnh đầu tiên
	if (slideIndex > slides.length) {
		slideIndex = 1;
	}

	// Hiển thị ảnh hiện tại
	slides[slideIndex - 1].style.display = "block";
}

function stopSlideShow() {
	isAutoSlide = false; // Tắt chuyển đổi tự động
	clearInterval(slideInterval); // Xóa hẹn giờ
}

// Bắt đầu chuyển đổi tự động khi trang được tải
startSlideShow();



