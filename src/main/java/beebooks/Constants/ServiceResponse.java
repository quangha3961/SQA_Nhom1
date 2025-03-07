package beebooks.Constants;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ServiceResponse {
    private ServiceError error;
    private Object data;

    public ServiceResponse(ServiceError error) {
        this.error = error;
    }

    public ServiceResponse(ServiceError error, Object data) {
        this.error = error;
        this.data = data;
    }

    public ServiceResponse(int code, String message) {
        this.error = new ServiceError(code, message);
    }

    public ServiceResponse(Object data) {
        this.error = ServiceErrors.SUCCESS;
        this.data = data;
    }

    public ServiceResponse() {
        this.error = ServiceErrors.SUCCESS;
    }
}
