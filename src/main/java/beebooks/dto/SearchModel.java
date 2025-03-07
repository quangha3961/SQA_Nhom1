package beebooks.dto;

import lombok.Data;

@Data
public class SearchModel extends BaseSearchModel {

	public String keyword;

	public Integer id;
}
