package ProjectAlkemy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ProjectAlkemy.model.News;

@Repository
public interface NewsRepository extends BaseRepository<News,Long>{

	Page<News> findAllBySoftDeleteFalseOrderByCreateAt(Pageable paging);

}
