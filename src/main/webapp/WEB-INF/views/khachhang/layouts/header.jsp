<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!-- import JSTL -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- import SPRING-FORM -->
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<div class="main-header">

    <div class="header">

        <div class="title">
            <a href="${base }/home"> <img src="${base }/img/beebooks.png">
            </a>
        </div>


        <div class="search-header">

                <form class="list" action="${base }/collection" method="get">
                    <input type="text" id="keyword" name="keyword" class="searchTerm" placeholder="Tìm kiếm sản phẩm..."
                           value="${searchModel.keyword }" style="border: 1px solid #ced4da; width: 75%">
                    <button type="submit" class="searchButton">
                        <i class="fa fa-search"></i>
                    </button>
                </form>

        </div>

        <div class="form-login">
            <div class="item-icon">
                <a href="${base }/login"> <i class="far fa-address-book"></i>
                </a>
            </div>

            <div class="form-login-content">
                <form class="cart-flex">

                    <button type="submit"
                            style="background-color: white; border: 1px solid white;">
                        <c:if test="${!isLogined }">
                            <div class="sign-in">
                                <a href="${base }/login" style="text-decoration: none;">Đăng
                                    nhập / </a>
                                <a href="${base }/register" style="text-decoration: none">Đăng ký</a>
                            </div>
                        </c:if>

                    </button>

                    <c:if test="${isLogined }">
                        <button type="submit"
                                style="background-color: white; border: 1px solid white;">
                            <div class="acount" style="margin-bottom:4px;">
                                    ${userLogined.username }
                            </div>
                            <a href="${base }/logout" style="text-decoration: none; border: 1px solid black; border-radius: 10px;
                            padding: 2px 4px; background-color: #303030; color: white;">Đăng xuất</a>
                        </button>
                    </c:if>

                </form>


            </div>
        </div>
        <div class="carts">

            <form class="cart-flex">
                <button type="submit"
                        style="background-color: white; border: 1px solid white;">
                    <ul>
                        <li><a href="${base }/cart/view"><i
                                class="fas fa-cart-plus"></i></a></li>
                        <li><a href="${base }/cart/view">Giỏ hàng</a></li>
                        <li><span id="iconShowTotalItemsInCart">${totalItems }</span>
                        </li>
                    </ul>
                </button>


            </form>

        </div>
    </div>


    <!--open nav-->
    <div class="nav">
        <ul class="nav-chinh">
            <li><a href="${base}/home">Trang chủ</a></li>
            <li id="all-product">
                <a href="${base }/collection">
                    Tất cả sản phẩm
                    <i class="fas fa-angle-down"></i>
                </a>
            </li>
            <li><a href="${base }/blog">Blog</a></li>
            <li><a href="${base }/introduction">Giới thiệu</a></li>
            <li><a href="${base }/contact">Liên hệ</a></li>
            <li><a href="${base }/checkOrder">Kiểm tra đơn hàng</a></li>
        </ul>


    </div>
    <!--close nav-->
</div>
