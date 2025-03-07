package beebooks.dto;

import lombok.Data;

@Data
public class BlogSearchModel extends BaseSearchModel {

	public String keyword;

	public Integer categoryId;

	public String seo;

}
