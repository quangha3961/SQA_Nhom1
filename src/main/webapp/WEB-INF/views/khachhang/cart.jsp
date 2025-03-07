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
    <title>Giỏ Hàng - Msic</title>
    <link rel="stylesheet"
          href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css"
          integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p"
          crossorigin="anonymous"/>

    <jsp:include page="/WEB-INF/views/khachhang/layouts/css.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="${base}/css/cart.css">
    <style>
        #paymentLink {
            text-decoration: none;
            color: #ff5f17;
            display: inline-block;
            text-align: center;
        }
        .error-message {
            color: red;
            font-size: 13px;
            margin-top: 4px;
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

            <li>Giỏ hàng</li>
        </ul>
    </div>
    <div class="content">
        <h3>Giỏ hàng của bạn</h3>
<%--        <p class="content-title-p1">--%>
<%--            Có <span class="content-title-p2">${totalItems }</span> sản phẩm trong giỏ hàng--%>
<%--        </p>--%>
        <!-- start shopping cart table-->
        <div class="cart-table">
            <table id="table">
                <thead>
                <tr>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Sản phẩm</div>
                    </th>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Giá</div>
                    </th>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Số lượng</div>
                    </th>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Tổng số</div>
                    </th>
                    <th scope="col" class="border-0 bg-light">
                        <div class="py-2 text-uppercase">Xóa</div>
                    </th>

                </tr>
                </thead>
                <tbody>
                <c:forEach items="${cart.cartItems }" var="ci">
                    <tr>
                        <th scope="row" class="border-0">
                            <div class="p-2">
                                <img src="${ci.productPictures}" alt="" width="70"
                                     class="img-fluid rounded shadow-sm">
                                <div class="ml-3 d-inline-block align-middle">
                                    <h5 class="mb-0">
                                        <a href="#" class="text-dark "> ${ci.productName } </a>
                                    </h5>

                                </div>
                            </div>
                        </th>

                        <td class="border-0">
                            <strong>
                                <fmt:setLocale value="vi_VN"/>
                                <fmt:formatNumber value="${ci.priceUnit}" type="currency"/>
                            </strong>
                        </td>
                        <td class="border-0">
                            <button type="button" onclick="UpdateQuanlityCart('${base }',${ci.productId })"
                                    class="cong">+
                            </button>
                            <strong>
                                <span id="quanlity_${ci.productId }">${ci.quanlity }</span>
                            </strong>

                            <button type="button" onclick="TruQuanlityCart('${base }',${ci.productId })" class="tru">-
                            </button>
                        </td>
                        <td class="border-0">
                            <strong>
                                <fmt:setLocale value="vi_VN"/>
                                <fmt:formatNumber value="${ci.priceUnit * ci.quanlity}" type="currency"/>
                            </strong>
                        </td>
                        <td class="border-0">
                            <a href="${base }/cart/remove/${ci.productId}" class="text-dark">
                                <i class="fa fa-trash" id="card-icon"></i>
                            </a>
                        </td>
                        <td class="border-0">
                            <a type="button" class="a-update"
                               href="${base }/cart/view">Cập nhật</a>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>

        </div>


        <!-- End -->
        <form action="${base }/cart/checkout" method="post">
            <div>
                <div class="content-desc">
                    <div class="content-desc-p1">
                        <div class="note">
                            <h5>Ghi chú đơn hàng</h5>
                            <textarea rows="8" cols="40" name="note" id="note"
                                      placeholder="Ghi chú"></textarea>
                        </div>

                        <div class="sidebox">
                            <h5>Chính sách mua hàng</h5>
                            <ul>
                                <li><i class="fas fa-long-arrow-alt-right"></i>Sản phẩm
                                    được đổi 1 lần duy nhất, không hỗ trợ trả.
                                </li>
                                <li><i class="fas fa-long-arrow-alt-right"></i>Sản phẩm
                                    còn đủ tem mác, chưa qua sử dụng.
                                </li>
                                <li><i class="fas fa-long-arrow-alt-right"></i>Sản phẩm
                                    nguyên giá được đổi trong 30 ngày trên toàn hệ thống.
                                </li>
                            </ul>
                        </div>
                    </div>

                </div>

                <div class="content-desc">
                    <div class="form-row" style="box-sizing: border-box;">
                        <div class="form-group">
                            <h4>Thông tin khách hàng</h4>
                        </div>

                        <c:if test="${isLogined }">
                            <div class="form-group">
                                <label for="customer_name">Họ và tên</label><br> <input
                                    type="text" class="form-control" id="customer_name"
                                    name="customer_name" placeholder="Name" value="${userLogined.username }">
                            </div>
                            <div class="form-group">
                                <label for="customer_email">Địa chỉ email</label><br> <input
                                    type="email" class="form-control" id="customer_email"
                                    name="customer_email" placeholder="Enter email" value="${userLogined.email }">
                                <!-- <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small> -->
                            </div>
                            <div class="form-group">
                                <label for="customer_phone">Số điện thoại</label><br> <input
                                    type="text" class="form-control" id="customer_phone"
                                    name="customer_phone" placeholder="Phone" value="${userLogined.phone}">
                            </div>
                            <div class="form-group">
                                <label for="customer_address">Địa chỉ</label><br> <input
                                    type="text" class="form-control" id="customer_address"
                                    name="customer_address" placeholder="Address" value="${userLogined.address }">
                            </div>
                        </c:if>

                        <c:if test="${!isLogined }">
                            <div class="form-group">
                                <label for="customer_name">Họ và tên</label><br>
                                <input type="text" class="form-control" id="customer_name" name="customer_name"
                                       placeholder="Name" onblur="validateUsername()" required="required">
                                <div></div>
                                <div class="error-message"></div>
                            </div>
                            <div class="form-group">
                                <label for="customer_email">Địa chỉ email</label><br>
                                <input type="email" class="form-control" id="customer_email" name="customer_email"
                                       placeholder="Enter email" onblur="validateEmail()" required="required">
                                <div></div>
                                <div class="error-message"></div>
                            </div>
                            <div class="form-group">
                                <label for="customer_phone">Số điện thoại</label><br>
                                <input type="number" class="form-control" id="customer_phone" name="customer_phone"
                                       placeholder="Phone" onblur="validatePhone()" required="required">
                                <div></div>
                                <div class="error-message"></div>
                            </div>
                            <div class="form-group">
                                <label for="customer_address">Địa chỉ</label><br>
                                <input type="text" class="form-control" id="customer_address" name="customer_address"
                                       placeholder="Address" onblur="validateAddress()" required="required">
                                <div></div>
                                <div class="error-message"></div>
                            </div>
                        </c:if>
                    </div>
                    <div class="content-desc-p2">
                        <div class="p2-title">
                            <h4>Thông tin đơn hàng</h4>
                        </div>
                        <div class="p2-total">
                            <p>
                                Tổng tiền : <span class="total-price">
                                <fmt:setLocale value="vi_VN"/>
                                <fmt:formatNumber value="${cart.totalPrice }" type="currency"/>
                                </span>
                            </p>
                        </div>
                        <div class="p2-text">
                            <p>
                                Khi click đặt hàng, hệ thống sẽ mặc định bạn thanh toán khi nhận hàng<br> Bạn có thể
                                chọn thanh toán trực tiếp qua VNPay khi kích vào nút "Thanh toán qua VNPay.
                            </p>
                        </div>
                        <div class="p2-action">
                            <c:if test="${not empty cart.cartItems }">
                                <button type="submit" class="thanhtoan" id="cartBtn" onclick="return validateOrder();">
                                    ĐẶT HÀNG
                                </button>
                                <p>
                                    <a href="${base }/api/payment/create_payment" id="paymentLink">Thanh toán bằng VNPAY</a>
                                </p>

                            </c:if>
                            <p>
                                <a href="${base }/collection"><i class="fas fa-undo"></i>Tiếp
                                    tục mua hàng</a>
                            </p>
                        </div>
                        <div class="showOrder">
                            <div id="orderStatus"></div>
                        </div>
                    </div>
                </div>


            </div>
        </form>


    </div>

    <!--close content-->

    <!--open footer -->
    <jsp:include page="/WEB-INF/views/khachhang/layouts/footer.jsp"></jsp:include>
    <!--close footer-->
    <div class="copyright">
        Copyright <i class="far fa-copyright"></i> <a href="#">beebooks.</a> <a
            href="#">Powered by Haravan</a>
    </div>
</main>
</body>

<jsp:include page="/WEB-INF/views/khachhang/layouts/js.jsp"></jsp:include>
<script type="text/javascript">
    function cong() {
        var t = document.getElementById("textbox").value;
        document.getElementById("textbox").value = parseInt(t) + 1;
    }

    function tru() {
        var t = document.getElementById("textbox").value;
        document.getElementById("textbox").value = parseInt(t) - 1;
    }
</script>
<script type="text/javascript">
    document.getElementById('paymentLink').addEventListener('click', function (event) {
        event.preventDefault(); // Ngăn chặn hành vi mặc định của liên kết

        if(!validateOrder()){
            return;
        }

        let data = {
            customer_name: $("#customer_name").val(),
            customer_address: $("#customer_address").val(),
            customer_email: $("#customer_email").val(),
            customer_phone: $("#customer_phone").val()
        };

        console.log("Sending data:", data);


        // Gửi yêu cầu AJAX để lấy URL thanh toán
        var xhr = new XMLHttpRequest();
        xhr.open('POST', '${base}/api/payment/create_payment', true);
        xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    // Lấy URL thanh toán từ dữ liệu JSON phản hồi
                    var responseData = JSON.parse(xhr.responseText);
                    var paymentUrl = responseData.paymentUrl;

                    // Chuyển hướng đến URL thanh toán
                    window.location.href = paymentUrl;
                } else {
                    // Xử lý lỗi nếu cần
                }
            }
        };
        xhr.send(JSON.stringify(data));
    });

</script>
<script type="text/javascript">
    var error = "${error}";

    if (error !== "") {
        var orderStatusDiv = document.getElementById("orderStatus");

        // Hiển thị thông báo lỗi
        orderStatusDiv.innerHTML = "<p>" + error + "</p>";
        orderStatusDiv.style.color = "red";
    } else {
        // Ẩn thông báo nếu không có lỗi
        var orderStatusDiv = document.getElementById("orderStatus");
        orderStatusDiv.innerHTML = "";
    }

    // Kiểm tra xem có lỗi không, nếu có, ngăn việc gửi yêu cầu đặt hàng
    var cartBtn = document.getElementById("cartBtn");
    cartBtn.addEventListener("click", function(event) {
        if (error !== "") {
            event.preventDefault();
        }
    });
</script>
<script type="text/javascript">
    // Hàm kiểm tra tên tối thiểu 8 ký tự
    function validateUsername() {
        var usernameInput = document.getElementById("customer_name");
        var usernameValue = usernameInput.value.trim();
        if (usernameValue.length < 8) {
            setErrorFor(usernameInput, "Họ và tên cần tối thiểu 8 ký tự");
            return false;
        } else {
            setSuccessFor(usernameInput);
            return true;
        }
    }

    // Hàm kiểm tra định dạng email
    function validateEmail() {
        var emailInput = document.getElementById("customer_email");
        var emailValue = emailInput.value.trim();
        var emailRegex = /^[A-Za-z0-9]{6,32}@([a-zA-Z0-9]{2,12})(.[a-zA-Z]{2,12})+$/;
        if (!emailRegex.test(emailValue)) {
            setErrorFor(emailInput, "Email không hợp lệ");
            return false;
        } else {
            setSuccessFor(emailInput);
            return true;
        }
    }

    // Hàm kiểm tra số điện thoại có đúng 10 số
    function validatePhone() {
        var phoneInput = document.getElementById("customer_phone");
        var phoneValue = phoneInput.value.trim();
        if (phoneValue.length < 10 || phoneValue.length > 10) {
            setErrorFor(phoneInput, "Nhập đúng dịnh dang số điện thoại(10 số)");
            return false;
        } else {
            setSuccessFor(phoneInput);
            return true;
        }
    }

    // Hàm kiểm tra địa chỉ tối thiểu 20 ký tự
    function validateAddress() {
        var addressInput = document.getElementById("customer_address");
        var addressValue = addressInput.value.trim();
        if (addressValue.length < 15) {
            setErrorFor(addressInput, "Địa chỉ cần tối thiểu 15 ký tự");
            return false;
        } else {
            setSuccessFor(addressInput);
            return true;
        }
    }

    // Hàm hiển thị thông báo lỗi
    function setErrorFor(input, message) {
        var formControl = input.parentElement;
        var errorMessage = formControl.querySelector(".error-message");
        // formControl.className = "form-control error";
        errorMessage.innerText = message;
    }


    // Hàm hiển thị trạng thái thành công
    function setSuccessFor(input) {
        var formControl = input.parentElement;
        var errorMessage = formControl.querySelector(".error-message");
        errorMessage.innerText = '';
    }

    // Hàm kiểm tra toàn bộ biểu mẫu trước khi gửi
    function validateForm() {
        var isUsernameValid = validateUsername();
        var isEmailValid = validateEmail();
        var isPhoneValid = validatePhone();
        var isAddressValid = validateAddress();
        return isUsernameValid && isEmailValid && isPhoneValid && isAddressValid;
    }

    function validateOrder() {
        var isUsernameValid = validateUsername();
        var isEmailValid = validateEmail();
        var isPhoneValid = validatePhone();
        var isAddressValid = validateAddress();

        if (!isUsernameValid || !isEmailValid || !isPhoneValid || !isAddressValid) {
            return false;
        }
        return true;
    }

</script>

<script type="text/javascript">

</script>

</html>