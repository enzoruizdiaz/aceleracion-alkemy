package ProjectAlkemy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ProjectAlkemy.model.Member;

@Repository
public interface MemberRepository extends BaseRepository<Member,Long> {
	
	Page<Member> findAllBySoftDeleteFalseOrderByCreateAt(Pageable paging);
	
}
