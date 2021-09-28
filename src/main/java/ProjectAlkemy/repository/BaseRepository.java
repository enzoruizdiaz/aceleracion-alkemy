package ProjectAlkemy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ProjectAlkemy.model.Base;

@NoRepositoryBean
public interface BaseRepository <E extends Base,ID> extends JpaRepository<E,ID>{
}
