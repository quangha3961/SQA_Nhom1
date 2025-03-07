package beebooks.service.impl;

import beebooks.dto.BlogSearchModel;
import beebooks.entities.Blog;
import beebooks.entities.BlogImage;
import beebooks.service.BaseService;
import beebooks.service.PagerData;
import com.github.slugify.Slugify;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;


@Service
public class BlogService extends BaseService<Blog> {

    public String UPLOAD_FOLDER_ROOT = "C:/upload/";

    private final BlogImageService blogImageService;


    public BlogService(BlogImageService blogImageService) {
        this.blogImageService = blogImageService;

	}

    @Override
    protected Class<Blog> clazz() {
        // TODO Auto-generated method stub
        return Blog.class;
    }

    private boolean isEmptyUploadFile(MultipartFile[] images) {
        if (images == null || images.length <= 0)
            return true;

        if (images.length == 1 && images[0].getOriginalFilename().isEmpty())
            return true;

        return false;
    }

    private boolean isEmptyUploadFile(MultipartFile image) {
        return image == null || image.getOriginalFilename().isEmpty();
    }

    public PagerData<Blog> search(BlogSearchModel searchModel) {

        String sql = "SELECT * FROM tbl_blog p WHERE 1=1";

        if (searchModel != null) {
            if (searchModel.categoryId != null) {
                sql += " and category_blog_id = " + searchModel.categoryId;
            }

            if (!StringUtils.isEmpty(searchModel.seo)) {
                sql += " and p.seo = '" + searchModel.seo + "'";
            }

            if (!StringUtils.isEmpty(searchModel.keyword)) {
                sql += " and (p.title like '%" + searchModel.keyword + "%'" + " or p.detail_description like '%"
                        + searchModel.keyword + "%'" + " or p.short_description like '%" + searchModel.keyword + "%')";
            }
        }

        return executeByNativeSQL(sql, searchModel == null ? 0 : searchModel.getPage());
    }

    /**
     * Dùng để thêm mới sản phẩm
     */
    @Transactional
    public Blog add(Blog p, MultipartFile productAvatar, MultipartFile[] productPictures)
            throws IllegalStateException, IOException {

        // kiểm tra xem admin có đẩy avatar lên không ???
        if (!isEmptyUploadFile(productAvatar)) {
            // tạo đường dẫn tới folder chứa avatar
            String pathToFile = UPLOAD_FOLDER_ROOT + "product/avatar/" + productAvatar.getOriginalFilename();

            // lưu avatar vào đường dẫn trên
            productAvatar.transferTo(new File(pathToFile));

            p.setAvatar("product/avatar/" + productAvatar.getOriginalFilename());

        }

        // có đẩy pictures(product_images) ???
        if (!isEmptyUploadFile(productPictures)) {

            // nếu admin đẩy lên thì duyệt tất cả file đẩy lên và lưu trên server
            for (MultipartFile pic : productPictures) {
                // lưu ảnh admin đẩy lên vào server
                pic.transferTo(new File(UPLOAD_FOLDER_ROOT + "product/pictures/" + pic.getOriginalFilename()));

                // tạo mới 1 bản ghi product_images
                BlogImage pi = new BlogImage();
                pi.setPath("product/pictures/" + pic.getOriginalFilename());
                pi.setTitle(pic.getOriginalFilename());

                p.addBlogImage(pi);
            }
        }

        p.setSeo(new Slugify().slugify(p.getTitle()));

        return super.saveOrUpdate(p);
    }

    /**
     * Cập nhật sản phẩm
     *
     */
    @Transactional
    public Blog update(Blog p, MultipartFile productAvatar, MultipartFile[] productPictures)
            throws IllegalStateException, IOException {

        // lấy thông tin cũ của product theo id
		Blog productInDb = super.getById(p.getId());

        // có đẩy avartar ??? => xóa avatar cũ đi và thêm avatar mới
        if (!isEmptyUploadFile(productAvatar)) {
            // xóa avatar trong folder lên
            new File(UPLOAD_FOLDER_ROOT + productInDb.getAvatar()).delete();

            // add avartar moi
            productAvatar
                    .transferTo(new File(UPLOAD_FOLDER_ROOT + "product/avatar/" + productAvatar.getOriginalFilename()));
            p.setAvatar("product/avatar/" + productAvatar.getOriginalFilename());
        } else {
            // su dung lai avatar cu
            p.setAvatar(productInDb.getAvatar());
        }

        // có đẩy pictures ???
        if (!isEmptyUploadFile(productPictures)) {

            // xóa pictures cũ
            if (productInDb.getBlogImages() != null && productInDb.getBlogImages().size() > 0) {
                for (BlogImage opi : productInDb.getBlogImages()) {
                    // xóa avatar trong folder lên
                    new File(UPLOAD_FOLDER_ROOT + opi.getPath()).delete();

                    // xóa dữ liệu trong database
                    blogImageService.delete(opi);
                }
            }

            // thêm pictures mới
            for (MultipartFile pic : productPictures) {
                pic.transferTo(new File(UPLOAD_FOLDER_ROOT + "product/pictures/" + pic.getOriginalFilename()));

                BlogImage pi = new BlogImage();
                pi.setPath("product/pictures/" + pic.getOriginalFilename());
                pi.setTitle(pic.getOriginalFilename());

                p.addBlogImage(pi);

            }
        }

        p.setSeo(new Slugify().slugify(p.getTitle()));

        return super.saveOrUpdate(p);

    }

    @Transactional
    public void remove(Blog p) {
        delete(p);
    }

}
