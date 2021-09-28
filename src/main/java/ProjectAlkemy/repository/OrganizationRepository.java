package ProjectAlkemy.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import ProjectAlkemy.model.Organization;

@Repository
public interface OrganizationRepository extends BaseRepository<Organization, Long> {

	List<Organization> findAllBySoftDeleteFalseOrderByCreateAt();

}
