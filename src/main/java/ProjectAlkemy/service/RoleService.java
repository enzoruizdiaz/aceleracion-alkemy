package ProjectAlkemy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProjectAlkemy.model.Role;
import ProjectAlkemy.repository.BaseRepository;
import ProjectAlkemy.repository.RoleRepository;
import ProjectAlkemy.service.imp.BaseServiceImpl;

@Service
public class RoleService extends BaseServiceImpl<Role, Long> {

	@Autowired
	private RoleRepository roleRepository;

	public RoleService(BaseRepository<Role, Long> baseRepository) {
		super(baseRepository);
	}

	public Role findByName(String admin) {
		Role role = roleRepository.findByName(admin).orElse(null);
		return role;
	}
}
