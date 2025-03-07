package beebooks.dto;

import lombok.Data;

@Data
public class OrderSearchModel extends BaseSearchModel {

	public String keyword;
	public Integer orderId;

}
