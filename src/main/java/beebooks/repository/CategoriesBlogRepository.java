package beebooks.repository;

import beebooks.entities.CategoriesBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesBlogRepository extends JpaRepository<CategoriesBlog, Integer>{

}
