package ProjectAlkemy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.dto.UserDto;
import ProjectAlkemy.controller.request.UserRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.User;
import ProjectAlkemy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Controller", description = "All Api Rest methods to manage a Users")
public class UserController {

	
	private UserService userService;
	
	@Autowired
	public UserController (UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "Delete a user by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	Return a OK message", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "	Slide Not Found", content = {
			@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "503", description = "	Service Unavailable") })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@Parameter(description = "	User id to delete")@PathVariable("id") Long id) throws RecordNotExistException{

		User user = userService.getById(id);
		userService.deleteUser(user);
		return new ResponseEntity<>(new ResponseDto(200,"Has been successfully deleted."), HttpStatus.OK);

	}

	@Operation(summary = "Get all users in data base")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	List all users in data base", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
			@ApiResponse(responseCode = "404", description = "	Users not found", content = {
					@Content(mediaType = "application/json") })})

	@GetMapping
	public ResponseEntity<?> listUsers() throws ListNotFoundException {

		List<User> users = userService.getAllNotDeleted();
		List<UserDto> userDtos = UserDto.mapToDtoList(users);
		return new ResponseEntity<>(userDtos, HttpStatus.OK);

	}

	@Operation(summary = "Update a user by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	User details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class)) }),
			@ApiResponse(responseCode = "404", description = "	User not found")})			
			
	@PatchMapping("/{id}")
	public ResponseEntity<?> patchUpdateUser(@Parameter(description = "	User id to update")@PathVariable("id") Long id,@Parameter(description = "	All attributes to update a User") @RequestBody UserRequest userRequest) throws RecordNotExistException {
		ResponseEntity<?> responseEntity;

		User user = userService.patchUpdate(id, userRequest);
		UserDto userDto = UserDto.mapToDto(user);
		responseEntity = new ResponseEntity<>(userDto, HttpStatus.OK);

		return responseEntity;
	}
}
