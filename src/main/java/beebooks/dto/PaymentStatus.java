package beebooks.dto;

import lombok.Data;

@Data
public class PaymentStatus {
    private Integer saleOrderId;
    private Integer status;
}
