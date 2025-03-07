package beebooks.service;

import beebooks.dto.SearchModel;
import beebooks.entities.Author;
import beebooks.entities.Promotion;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class PromotionService extends BaseService<Promotion>{

	@Override
	protected Class<Promotion> clazz() {
		// TODO Auto-generated method stub
		return Promotion.class;
	}

	public PagerData<Promotion> search(SearchModel searchModel) {

		String sql = "SELECT * FROM tbl_promotion p WHERE 1=1";

		if (searchModel != null) {
			if (searchModel.id != null) {
				sql += " and id = " + searchModel.id;
			}

			if (!StringUtils.isEmpty(searchModel.keyword)) {
				sql += " and (p.name like '%" + searchModel.keyword + "%')";
			}
		}

		return executeByNativeSQL(sql, searchModel == null ? 0 : searchModel.getPage());
	}

}
