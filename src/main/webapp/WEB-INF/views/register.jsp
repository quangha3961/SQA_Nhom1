<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!--import JSTL-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- import SPRING-FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" type="text/css" href="${base}/css/register.css">
    <title>Register Form</title>
    <jsp:include page="/WEB-INF/views/common/variables.jsp"></jsp:include>
    <style>
        .error-message {
            color: red;
            font-size: 12px;
            margin-top: 4px;
        }
    </style>
</head>
<body>
<div class="container">
    <form action="${base }/register" method="POST" modelAttribute="user">
        <h1>Đăng ký tài khoản</h1>
        <div id="TB_REGISTER" class="form-control">

        </div>
        <div class="form-control">
            <input id="username" type="text" name="username" placeholder="Tên đăng nhập" onblur="validateUsername()">
            <div class="error-message"></div>
        </div>
        <div class="form-control">
            <input id="email" type="email" name="email" placeholder="Email" onblur="validateEmail()">
            <div class="error-message"></div>
        </div>
        <div class="form-control">
            <input id="phone" type="number" name="phone" placeholder="Số điện thoại" onblur="validatePhone()">
            <div class="error-message"></div>
        </div>
        <div class="form-control">
            <input id="password" type="password" name="password" placeholder="Mật khẩu" onblur="validatePassword()">
            <div class="error-message"></div>
        </div>
        <div class="form-control">
            <input id="address" type="text" name="address" placeholder="Địa chỉ" onblur="validateAddress()">
            <div class="error-message"></div>
        </div>


        <button type="submit" class="btn-submit" id="registerBtn" onclick="return validateOrder();">Đăng ký</button>

        <div class="signup-link">
            <a href="${base }/login">Trở về đăng nhập</a>
        </div>

        <div class="showOrder" style="display: flex; align-items: center; justify-content: center; margin: 15px 0px;">
            <div id="messageRegister"></div>
        </div>
    </form>


</div>

</body>
<script type="text/javascript">
    // Hàm kiểm tra tên tối thiểu 8 ký tự
    function validateUsername() {
        var usernameInput = document.getElementById("username");
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
        var emailInput = document.getElementById("email");
        var emailValue = emailInput.value.trim();
        var emailRegex = /^[A-Za-z0-9]{6,32}@([a-zA-Z0-9]{2,12})(.[a-zA-Z]{2,12})+$/;
        if (!emailRegex.test(emailValue) || emailInput.value == null) {
            setErrorFor(emailInput, "Email không hợp lệ");
            return false;
        } else {
            setSuccessFor(emailInput);
            return true;
        }
    }

    // Hàm kiểm tra số điện thoại có đúng 10 số
    function validatePhone() {
        var phoneInput = document.getElementById("phone");
        var phoneValue = phoneInput.value.trim();
        if (phoneValue.length < 10 || phoneValue.length > 10 || phoneInput.value == null) {
            setErrorFor(phoneInput, "Nhập đúng dịnh dang số điện thoại(10 số)");
            return false;
        } else {
            setSuccessFor(phoneInput);
            return true;
        }
    }

    // Hàm kiểm tra địa chỉ tối thiểu 20 ký tự
    function validateAddress() {
        var addressInput = document.getElementById("address");
        var addressValue = addressInput.value.trim();
        if (addressValue.length < 15  || addressInput.value == null) {
            setErrorFor(addressInput, "Địa chỉ cần tối thiểu 15 ký tự");
            return false;
        } else {
            setSuccessFor(addressInput);
            return true;
        }
    }

    function validatePassword() {
        var passInput = document.getElementById("password");
        var passValue = passInput.value.trim();
        if (passValue.length < 8) {
            setErrorFor(passInput, "Mật khẩu cần tối thiểu 8 ký tự");
            return false;
        } else {
            setSuccessFor(passInput);
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

    function validateOrder() {
        var isUsernameValid = validateUsername();
        var isEmailValid = validateEmail();
        var isPhoneValid = validatePhone();
        var isAddressValid = validateAddress();
        var isPassValid = validatePassword();

        if (!isUsernameValid || !isEmailValid || !isPhoneValid || !isAddressValid || !isPassValid) {
            return false;
        }
        return true;
    }

</script>

</html>
