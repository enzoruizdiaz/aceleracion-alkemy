package ProjectAlkemy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProjectAlkemy.controller.request.OrganizationRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Organization;
import ProjectAlkemy.repository.BaseRepository;
import ProjectAlkemy.repository.OrganizationRepository;
import ProjectAlkemy.service.imp.BaseServiceImpl;

@Service
public class OrganizationService extends BaseServiceImpl<Organization,Long> {

	@Autowired
	OrganizationRepository organizationRepo;

	
	public OrganizationService(BaseRepository<Organization, Long> baseRepository) {
		super(baseRepository);
	}
	public Organization deleteOrganization(Organization org) {
		org.setSoftDelete(true);
		return this.update(org);
	}
	
	public List<Organization> getAllNotDelete() throws ListNotFoundException {

		List<Organization> list = organizationRepo.findAllBySoftDeleteFalseOrderByCreateAt();
		return Optional.ofNullable(list).orElseThrow(() -> new ListNotFoundException("no comment to list"));
	}

	public  Organization setOrg(OrganizationRequest orgReq, Long id) throws RecordNotExistException {
		Organization organization = this.getById(id);
		
		if (organization != null) {
			if (orgReq.getName() != null) {
				organization.setName(orgReq.getName());
			}
			if (orgReq.getAddress() != null) {
				organization.setAddress(orgReq.getAddress());
			}
			if (orgReq.getAboutUs() != null) {
				organization.setAboutUs(orgReq.getAboutUs());
			}
			if (orgReq.getEmail() != null) {
				organization.setEmail(orgReq.getEmail());
			}
			if (orgReq.getFacebook()!= null) {
				organization.setFacebook(orgReq.getFacebook());
			}
			if (orgReq.getInstagram() != null) {
				organization.setInstagram(orgReq.getInstagram());
			}
			if (orgReq.getPhone() != null) {
				organization.setPhone(orgReq.getPhone());
			}
			if (orgReq.getWelcomeText() != null) {
				organization.setWelcomeText(orgReq.getWelcomeText());
			}
			if (orgReq.getLinkedin() != null) {
				organization.setLinkedin(orgReq.getLinkedin());
			}
			if (orgReq.getImage() != null) {
				organization.setImage(orgReq.getImage());
			}
		}
		
		return organization;

	}


}
