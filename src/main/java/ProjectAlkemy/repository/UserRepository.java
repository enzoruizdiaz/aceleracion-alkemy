package ProjectAlkemy.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import ProjectAlkemy.model.User;

@Repository
public interface UserRepository extends BaseRepository<User,Long> {

	Optional<User> findUserByEmail(String email);

	List<User> findAllBySoftDeleteFalseOrderByCreateAt();
}
