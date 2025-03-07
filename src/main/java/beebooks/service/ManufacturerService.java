package beebooks.service;

import beebooks.dto.SearchModel;
import beebooks.entities.Manufacturer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class ManufacturerService extends BaseService<Manufacturer>{

	@Override
	protected Class<Manufacturer> clazz() {
		// TODO Auto-generated method stub
		return Manufacturer.class;
	}

	public PagerData<Manufacturer> search(SearchModel searchModel) {

		String sql = "SELECT * FROM tbl_manufacturer p WHERE 1=1";
		if (searchModel != null) {
			if (searchModel.id != null) {
				sql += " and id = " + searchModel.id;
			}

			if (!StringUtils.isEmpty(searchModel.keyword)) {
				sql += " and (p.name like '%" + searchModel.keyword + "%'" + " or p.address like '%"
						+ searchModel.keyword + "%')";
			}
		}

		return executeByNativeSQL(sql, searchModel == null ? 0 : searchModel.getPage());
	}

}
