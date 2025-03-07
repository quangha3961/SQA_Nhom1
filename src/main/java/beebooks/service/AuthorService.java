package beebooks.service;

import beebooks.dto.SearchModel;
import beebooks.entities.Author;
import beebooks.entities.Manufacturer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class AuthorService extends BaseService<Author>{

	@Override
	protected Class<Author> clazz() {
		// TODO Auto-generated method stub
		return Author.class;
	}

	public PagerData<Author> search(SearchModel searchModel) {

		String sql = "SELECT * FROM tbl_author p WHERE 1=1";

		if (searchModel != null) {
			if (searchModel.id != null) {
				sql += " and id = " + searchModel.id;
			}

			if (!StringUtils.isEmpty(searchModel.keyword)) {
				sql += " and (p.name like '%" + searchModel.keyword + "%'" + " or p.biography like '%"
						+ searchModel.keyword + "%')";
			}
		}

		return executeByNativeSQL(sql, searchModel == null ? 0 : searchModel.getPage());
	}

}
