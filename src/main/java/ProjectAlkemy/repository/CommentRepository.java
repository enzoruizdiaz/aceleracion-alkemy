package ProjectAlkemy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ProjectAlkemy.model.Comment;


@Repository
public interface CommentRepository extends BaseRepository<Comment,Long> {

	List<Comment> findAllBySoftDeleteFalseOrderByCreateAt();

	List<Comment> findAllByOrderByCreateAt();

	@Query(value="SELECT co.* FROM comments co inner join news ne on ne.news_id=co.fk_new where ne.news_id=?1 and co.soft_delete = false", nativeQuery = true)
	List<Comment> findAllComment(Long id);
}
