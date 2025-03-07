package beebooks.Constants;

public class ServiceErrors {
    public static final ServiceError SUCCESS = new ServiceError(0, "Thành công");
    public static final ServiceError ERROR = new ServiceError(500, "Có lỗi xảy ra");
    public static final ServiceError TIMEOUT = new ServiceError(408, "Time Out");
    public static final ServiceError BAD_REQUEST = new ServiceError(1, "Yêu cầu không hợp lệ");
    public static final ServiceError FORBIDDEN = new ServiceError(403, "Bạn chưa có quyền truy cập vào hệ thống");
    public static final ServiceError UNAUTHORIZED = new ServiceError(401, "Phiên đăng nhập hết hạn");
}
