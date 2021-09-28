package ProjectAlkemy.controller.dto;

import java.io.Serializable;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2538891841279760958L;
	private String name;
    private String description;
    

    @Autowired
    private static ModelMapper mapper = new ModelMapper();

    public static RoleDto mapToDto(Role r) {
        RoleDto roleDto = mapper.map(r, RoleDto.class);

        return roleDto;
    }

    //metodo para mapear entity
    public static Role mapToEntity(RoleDto roleDto) {
        Role role = mapper.map(roleDto, Role.class);
        return role;
    }

}
