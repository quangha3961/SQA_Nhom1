package beebooks.service;

import beebooks.dto.OrderSearchModel;
import beebooks.entities.SaleorderProducts;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SaleorderProductsService extends BaseService<SaleorderProducts>{

	@Override
	protected Class<SaleorderProducts> clazz() {
		// TODO Auto-generated method stub
		return SaleorderProducts.class;
	}

	public PagerData<SaleorderProducts> search(OrderSearchModel searchModel) {
		String sql = "SELECT * FROM tbl_saleorder_products p WHERE 1=1";
		if (searchModel != null) {
			if (searchModel.orderId != null) {
				sql += " and p.saleorder_id = " + searchModel.orderId;
			}

			if (!StringUtils.isEmpty(searchModel.keyword)) {
//				sql += " and (p.saleorder_id like '%" + searchModel.keyword + "%')";
			}
		}

		return executeByNativeSQL(sql, searchModel == null ? 0 : searchModel.getPage());
	}

	public PagerData<SaleorderProducts> getById(OrderSearchModel searchModel) {
		String sql = "SELECT * FROM tbl_saleorder_products p WHERE ";
		if (searchModel != null) {
			if (searchModel.orderId != null) {
				sql += " p.saleorder_id = " + searchModel.orderId;
			}
		}

		return executeByNativeSQL(sql, searchModel == null ? 0 : searchModel.getPage());
	}

}
