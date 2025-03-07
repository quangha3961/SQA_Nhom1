package beebooks.service.paginate;

import org.springframework.stereotype.Service;

import beebooks.service.PagerData;

@Service
public interface IPaginatesService {
	public PagerData GetInfoPaginates(int totalItems, int limit, int currentPage);

}
