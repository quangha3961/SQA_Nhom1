package beebooks.repository;

import beebooks.dto.ProductSearchDataModel;
import beebooks.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{

    @PersistenceContext
    EntityManager entityManager = null;

    @PersistenceContext
    default Class<ProductSearchDataModel> clazz() {
        // TODO Auto-generated method stub
        return ProductSearchDataModel.class;
    }

    @Query(value ="select sum(tp.quantity)  from tbl_products tp ",nativeQuery = true)
    Integer getSumQuantity();

    @Query(value ="select * from tbl_products where price between :fromPrice and :toPrice",nativeQuery = true)
    List<Product> getListProduct(@Param("fromPrice") BigDecimal fromPrice, @Param("toPrice") BigDecimal toPrice);

    List<Product> findByCategories(Integer categoriesId);


}
