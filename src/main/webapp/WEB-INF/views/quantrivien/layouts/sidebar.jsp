<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>

<!-- sidebar menu area start -->
<div class="sidebar-menu">
    <div class="sidebar-header">
        <div class="logo">
            <a href="${base }/admin" style="color: white; font-size: 35px;">BEEBOOKS</a>
        </div>
    </div>
    <div class="main-menu">
        <div class="menu-inner">
            <nav>
                <ul class="metismenu" id="menu">

                    <li>
                        <a href="javascript:void(0)" aria-expanded="true"> <span>
								Hệ thống </span>
                        </a>
                        <ul class="collapse">
                            <li>
                                <a href="${base }/admin/user">
                                    <i class="fa fa-user"></i>
                                    <span>Tài khoản hệ thống </span>
                                </a>
                            </li>
                        </ul>
                        <ul class="collapse">
                            <li>
                                <a href="${base }/admin/report">
                                    <i class="fa fa-chart-bar"></i>
                                    <span>Thống kê </span>
                                </a>
                            </li>
                        </ul>
                    </li>

                    <li>
                        <a href="javascript:void(0)" aria-expanded="true">
                            <span>Liên hệ / Đơn hàng</span>
                        </a>
                        <ul class="collapse">
                            <li>
                                <a href="${base }/admin/order">
                                    <i class="fas fa-shopping-cart"></i>
                                    <span>Đơn hàng</span>
                                </a>
                            </li>

<%--                            <li>--%>
<%--                                <a href="${base }/admin/order-product">--%>
<%--                                    <i class="fas fa-list-alt"></i>--%>
<%--                                    <span>Chi tiết Đơn hàng</span>--%>
<%--                                </a>--%>
<%--                            </li>--%>

                            <li>
                                <a href="${base }/admin/contact">
                                    <i class="fas fa-envelope"></i>
                                    <span>Liên hệ</span>
                                </a>
                            </li>

                            <li>
                                <a href="${base }/admin/subscribe">
                                    <i class="fas fa-user-plus"></i>
                                    <span>Đăng ký</span>
                                </a>
                            </li>
                        </ul>
                    </li>

                    <li>
                        <a href="javascript:void(0)" aria-expanded="true">
                            <span>Sản phẩm </span>
                        </a>
                        <ul class="collapse">
                            <li>
                                <a href="${base }/admin/product">
                                    <i class="fas fa-box"></i>
                                    <span>Sản phẩm</span>
                                </a>
                            </li>

                            <li>
                                <a href="${base }/admin/category-product">
                                    <i class="fas fa-book"></i>
                                    <span>Danh mục Sản phẩm</span>
                                </a>
                            </li>

                            <li>
                                <a href="${base }/admin/manufacturer/list">
                                    <i class="fas fa-building"></i>
                                    <span>Nhà xuất bản</span>
                                </a>
                            </li>

                            <li>
                                <a href="${base }/admin/author/list">
                                    <i class="fas fa-user-edit"></i>
                                    <span>Tác giả</span>
                                </a>
                            </li>

                            <li>
                                <a href="${base }/admin/promotion/list">
                                    <i class="fas fa-tags"></i>
                                    <span>Chương trình KM</span>
                                </a>
                            </li>
                        </ul>
                    </li>

                    <li>
                        <a href="javascript:void(0)" aria-expanded="true">
                            <span>Blog </span>
                        </a>
                        <ul class="collapse">
                            <li>
                                <a href="${base }/admin/blog">
                                    <i class="fas fa-pencil-alt"></i>
                                    <span>Blog</span>
                                </a>
                            </li>

                            <li>
                                <a href="${base }/admin/category-blog">
                                    <i class="fas fa-book"></i>
                                    <span>Danh mục Blog</span>
                                </a>
                            </li>
                        </ul>
                    </li>


                </ul>
            </nav>
        </div>
    </div>
</div>