package ProjectAlkemy.controller.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5920054977610472941L;


	private String firstName;

	private String lastName;

	private String email;

	//private String password;
	
	private RoleDto roleId;

	@Autowired
	private static ModelMapper mapper = new ModelMapper();

	// metodo para mapear dto
	public static UserDto mapToDto(User u) {
		UserDto userDto = mapper.map(u, UserDto.class);

		return userDto;
	}

	// M�todo para comvertir una lista de usuarios en una lista de usuarios Dtos
	public static List<UserDto> mapToDtoList(List<User> userList) {
		List<UserDto> users = new ArrayList<UserDto>();
		for (User u : userList) {
			users.add(mapToDto(u));
		}
		return users;
	}

	// metodo para mapear entity
	public static User mapToEntity(UserDto userDto) {
		User user = mapper.map(userDto, User.class);
		return user;
	}
}
