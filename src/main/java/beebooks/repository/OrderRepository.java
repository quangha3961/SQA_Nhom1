package beebooks.repository;

import beebooks.entities.Saleorder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Saleorder, Integer> {
    @Query("select s from Saleorder as s where s.customer_email = :customerEmail")
    List<Saleorder> findByEmailOrder(@Param("customerEmail") String customerEmail);

    Saleorder findByCode(String code);

    @Query(value ="select sum(ts.status) from tbl_saleorder ts where ts.payment_status != 4",nativeQuery = true)
    Integer getSumOrder();

    @Query(value ="select sum(ts.total_price)  from tbl_saleorder ts where ts.payment_status != 4",nativeQuery = true)
    Integer getSumPrice();

    @Query(value ="select sum(ts.total) from tbl_saleorder ts where ts.payment_status != 4",nativeQuery = true)
    Integer getSumQuantity();
}
