package ProjectAlkemy.controller.request;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequest {
	
	private String name;
	private String image;
	private String address;
	private Integer phone;
	private String email;
	private String welcomeText;
	private String aboutUs;
	private String facebook;
	private String linkedin;
	private String instagram;
	
	
	@Autowired
	private static ModelMapper mapper = new ModelMapper();
	
	public static Organization mapToEntity(OrganizationRequest orgReq) {
		Organization org = mapper.map(orgReq, Organization.class);
		return org;
	}

}
