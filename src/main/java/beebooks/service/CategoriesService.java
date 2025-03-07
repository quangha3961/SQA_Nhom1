package beebooks.service;

import beebooks.dto.BlogSearchModel;
import beebooks.dto.ProductSearchModel;
import beebooks.entities.Categories;
import beebooks.entities.CategoriesBlog;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class CategoriesService extends BaseService<Categories>{

	@Override
	protected Class<Categories> clazz() {
		// TODO Auto-generated method stub
		return Categories.class;
	}

	public PagerData<Categories> search(ProductSearchModel searchModel) {

		String sql = "SELECT * FROM tbl_category p WHERE 1=1";

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
