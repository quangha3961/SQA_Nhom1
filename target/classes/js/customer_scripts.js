// Cập nhật số lượng sản phẩm trong giỏ hàng
function UpdateQuanlityCart(baseUrl, productId) {
    let data = {
        productId: productId,

    };

    jQuery.ajax({
        url: baseUrl + "/ajax/updateQuanlityCart",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(data),

        dataType: "json",
        success: function (jsonResult) {
            $("#quanlity_" + productId).html(jsonResult.currentProductQuality);
        },
        error: function (jqXhr, textStatus, errorMessage) {

        }
    });
}

function TruQuanlityCart(baseUrl, productId) {
    let data = {
        productId: productId,

    };

    jQuery.ajax({
        url: baseUrl + "/ajax/truQuanlityCart",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(data),

        dataType: "json",
        success: function (jsonResult) {

            $("#quanlity_" + productId).html(jsonResult.ciProductQuality);


        },
        error: function (jqXhr, textStatus, errorMessage) {

        }
    });
}


// Thêm sản phẩm vào trong giỏ hàng
function AddToCart(baseUrl, productId, quanlity) {
    let data = {
        productId: productId,
        quanlity: quanlity,
    };

    jQuery.ajax({
        url: baseUrl + "/ajax/addToCart",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(data),

        dataType: "json",
        success: function (jsonResult) {
            if (jsonResult.status === "out_of_stock") {
                alert("Sản phẩm này đã hết hàng.");
            } else {
                $("#iconShowTotalItemsInCart").html(jsonResult.totalItems);
                $([document.documentElement, document.body]).animate({
                    scrollTop: $("#iconShowTotalItemsInCart").offset().top
                }, 2000);
            }
        },
        error: function (jqXhr, textStatus, errorMessage) {

        }
    });
}

add = function (baseUrl) {
    let data = {
        maSP: jQuery("#validationCustom01").val(),
        tenSP: jQuery("#validationCustom02").val(),
        loaiSP: jQuery("#validationCustom03").val(),
        total: jQuery("#validationCustom04").val(),
    };

    jQuery.ajax({
        url: baseUrl + "/ajax/add-product",
        type: "post",
        contentType: "application/json",
        data: JSON.stringify(data),

        dataType: "json",
        success: function (jsonResult) {
            jQuery("#TB_AJAX").html(jsonResult.message);
        },
        error: function (jqXhr, textStatus, errorMessage) {
            // jQuery("#TB_AJAX").html(jsonResult.error);
        }
    });
}

//dành cho subcribe
home = function (baseUrl) {
    let data = {
        email: $("#email").val(),
    };

    if (validateEmail(data.email)) {
        $.ajax({
            url: baseUrl + "/ajax/home",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(data),

            dataType: "json",
            success: function (jsonResult) {
                $("#email").val("");
                $("#TB_AJAX").html(jsonResult.message);
            },
            error: function (errMessage) {
                $("#TB_AJAX").html(errMessage.err);
            }
        });
    } else {
        $("#TB_AJAX").html("Địa chỉ email không đúng định dạng / Trùng email đã đăng ký");
    }

}

//dành cho contact
contact = function (baseUrl) {
    let data = {
        email: $("#email").val(),
        name: $("#name").val(),
        massage: $("#massage").val(),
    };

    var flag = true;
    //email
    if (validateEmail(data.email)) {
        $.ajax({
            url: baseUrl + "/ajax/contact",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(data),
            dataType: "json",
            success: function (jsonResultCt) {
                $("#email").val("");
                $("#TB_AJAX_CONTACT").html(jsonResultCt.messages);
            },
            error: function (jsonResultCt) {
                $("#email").val("");
                $("#TB_AJAX_CONTACT").html(jsonResultCt.err);
            }
        });
    } else {
        $("#TB_AJAX_CONTACT").html("Địa chỉ email không đúng định dạng / Trùng email đã đăng ký");
    }


}

//dành cho register
register = function (baseUrl) {
    let dataRegister = {
        email: $("#email").val(),
        username: $("#username").val(),
    };
    if (validateEmail(dataRegister.email)) {
        $.ajax({
            url: baseUrl + "/register",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(dataRegister),
            dataType: "json",
            success: function (jsonResultCt) {
                $("#email").val("");
                $("#TB_REGISTER").html(jsonResultCt.messages);
            },
            error: function (jsonResultCt) {
            }
        });
    } else {
    }
}

function validateEmail(email) {
    const mailFormat = /^[A-Za-z0-9]{6,32}@([a-zA-Z0-9]{2,12})(.[a-zA-Z]{2,12})+$/;
    return mailFormat.test(String(email).toLowerCase());

}



