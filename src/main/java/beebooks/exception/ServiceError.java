package beebooks.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class ServiceError {
    private Integer code;
    private String message;
}
