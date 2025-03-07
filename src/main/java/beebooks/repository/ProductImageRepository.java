package beebooks.repository;

import beebooks.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer>{
    @Modifying
    @Query(value = "delete from tbl_products_images tpi where tpi.product_id = :productId", nativeQuery = true)
    void deleteByProduct(Integer productId);

}
