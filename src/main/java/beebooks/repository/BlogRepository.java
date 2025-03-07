package beebooks.repository;

import beebooks.entities.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer>{
    void deleteByCategoriesBlog(Integer id);

}
