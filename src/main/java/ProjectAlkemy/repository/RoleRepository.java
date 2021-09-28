package ProjectAlkemy.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ProjectAlkemy.model.Role;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

	Optional<Role> findByName(String name);


}
