package beebooks.exception;

public class ServiceErrors {
    public static final ServiceError SUCCESS = new ServiceError(0, "Thành công");
    public static final ServiceError ERROR = new ServiceError(99, "Lỗi hệ thống");
    public static final ServiceError BAD_REQUEST = new ServiceError(400, "Yêu cầu không hợp lệ");
    public static final ServiceError FORBIDDEN = new ServiceError(403, "Bạn chưa có quyền truy cập vào hệ thống");
    public static final ServiceError UNAUTHORIZED = new ServiceError(1, "Phiên đăng nhập không tồn tại");


}
