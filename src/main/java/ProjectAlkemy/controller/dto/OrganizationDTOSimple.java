package ProjectAlkemy.controller.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ProjectAlkemy.model.Organization;
import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class OrganizationDTOSimple implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4438554583853749688L;
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
	private static ModelMapper modelmapper = new ModelMapper();
	
	//method to map a Organization Object to a dto object using model mapper
	public static OrganizationDTOSimple mapToDto(Organization organization) {
		OrganizationDTOSimple aux = modelmapper.map(organization, OrganizationDTOSimple.class);
		return aux;
	}
	
	//method to map a List of Organization Object to a list of dto objects using model mapper
	public static List <OrganizationDTOSimple> mapListToDtoList(List<Organization> organizations)  {
		List<OrganizationDTOSimple> listed = new ArrayList<>();
		for (Organization o : organizations) {
			OrganizationDTOSimple auxDto =mapToDto(o);
			listed.add(auxDto);
		}
		return listed;
	}
}
