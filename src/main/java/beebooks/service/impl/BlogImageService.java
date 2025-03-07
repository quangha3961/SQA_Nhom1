package beebooks.service.impl;

import beebooks.entities.BlogImage;
import beebooks.service.BaseService;
import org.springframework.stereotype.Service;

@Service
public class BlogImageService extends BaseService<BlogImage> {

	@Override
	protected Class<BlogImage> clazz() {
		// TODO Auto-generated method stub
		return BlogImage.class;
	}

}
