package beebooks.repository;

import beebooks.entities.SaleorderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<SaleorderProducts, Integer> {
    @Query(value = "select * from tbl_saleorder_products tsp where tsp.saleorder_id = :saleOrder", nativeQuery = true)
    List<SaleorderProducts> findBySaleOrder(@Param("saleOrder") Integer saleOrder);

    @Query(value ="select sum(tsp.quality) from tbl_saleorder_products tsp",nativeQuery = true)
    Integer getSumQuantity();

    @Query(value ="select sum(tp.price * tsp.quality) from tbl_saleorder_products tsp  " +
            "inner join tbl_products tp on tsp.product_id = tp.id " +
            "inner join tbl_saleorder ts on tsp.saleorder_id = ts.id " +
            "where tp.id = tsp.product_id and ts.payment_status = 3",nativeQuery = true)
    Long getSumPrice();

    void deleteBySaleOrder(Integer id);

}
