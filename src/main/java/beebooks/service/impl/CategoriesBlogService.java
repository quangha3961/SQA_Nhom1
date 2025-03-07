package beebooks.service.impl;

import beebooks.dto.BlogSearchModel;
import beebooks.entities.CategoriesBlog;
import beebooks.service.BaseService;
import beebooks.service.PagerData;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class CategoriesBlogService extends BaseService<CategoriesBlog> {

	@Override
	protected Class<CategoriesBlog> clazz() {
		// TODO Auto-generated method stub
		return CategoriesBlog.class;
	}

	public PagerData<CategoriesBlog> search(BlogSearchModel searchModel) {

		String sql = "SELECT * FROM tbl_category_blog p WHERE 1=1";

		if (searchModel != null) {
			if (searchModel.categoryId != null) {
				sql += " and id = " + searchModel.categoryId;
			}

			if (!StringUtils.isEmpty(searchModel.seo)) {
				sql += " and p.seo = '" + searchModel.seo + "'";
			}
		}

		return executeByNativeSQL(sql, searchModel == null ? 0 : searchModel.getPage());
	}

}
