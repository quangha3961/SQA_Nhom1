package beebooks.exception;

import lombok.Getter;

public class CustomException extends RuntimeException {
    @Getter
    private final ServiceError serviceError;

    public CustomException(String message) {
        super(message);
        serviceError = new ServiceError(ServiceErrors.ERROR.getCode(), message);
    }

    public CustomException(ServiceError serviceError) {
        super(serviceError.getMessage());
        this.serviceError = serviceError;
    }
}
