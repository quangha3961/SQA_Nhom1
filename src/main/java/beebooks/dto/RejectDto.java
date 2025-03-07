package beebooks.dto;

import lombok.Data;

@Data
public class RejectDto {
    private Integer saleOrderId;
    private String reason;
    private Integer status;
}
