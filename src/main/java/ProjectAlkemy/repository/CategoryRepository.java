package ProjectAlkemy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ProjectAlkemy.model.Category;

@Repository
public interface CategoryRepository extends BaseRepository<Category,Long>{

	Page<Category>findAllBySoftDeleteFalseOrderByCreateAt(Pageable pageable);
	Page<Category>findAllByOrderByCreateAt(Pageable pageable);
}
