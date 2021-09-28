package ProjectAlkemy.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ProjectAlkemy.config.SengridConfig;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.AuthenticationRequest;
import ProjectAlkemy.controller.request.UserRequest;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.User;
import ProjectAlkemy.service.MyUserDetailsService;
import ProjectAlkemy.service.UserService;
import ProjectAlkemy.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller", description = "All Api Rest methods to manage a Authenticated Users")
public class AuthenticationController {

	private UserService userService;
	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	public AuthenticationController(UserService userService, AuthenticationManager authenticationManagerBean,
			MyUserDetailsService myUserDetailsService, JwtUtil jwtTokenUtil, SengridConfig sengridConfig,
			PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.authenticationManagerBean = authenticationManagerBean;
		this.myUserDetailsService = myUserDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
		this.sengridConfig = sengridConfig;
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	SengridConfig sengridConfig;

	@Autowired
	AuthenticationManager authenticationManagerBean;
	@Autowired
	JwtUtil jwtTokenUtil;

	@Operation(summary = "Register a new User")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id and Token of a user registered", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "    Firstname, Lastname, Password and Email are required"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {

		if (userRequest.getFirstName() == null || userRequest.getLastName() == null || userRequest.getEmail() == null
				|| userRequest.getPassword() == null) {
			return new ResponseEntity<>(new ResponseDto(400, "firstname, lastname, password and email are required."),
					HttpStatus.BAD_REQUEST);
		} else {

			User aux = UserRequest.mapToEntity(userRequest);
			User user = userService.createUser(aux);

			authenticationManagerBean.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword()));
			final UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getEmail());
			final String jwt = jwtTokenUtil.generateToken(userDetails);

			try {
				sengridConfig.sendMail(user.getEmail(), "REGISTER");

			} catch (IOException e) {
				return new ResponseEntity<>(
						new ResponseDto(200, "Failed to send email to: " + user.getEmail() + "  Token: " + jwt),
						HttpStatus.OK);
			}
			return new ResponseEntity<>(new ResponseDto(200, "Id: " + user.getId() + "   Token: " + jwt),
					HttpStatus.OK);
		}
	}

	@Operation(summary = "User login")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Return a user token", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "    Malformed request body"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PostMapping("/login")
	public ResponseEntity<?> createAuthentication(@RequestBody AuthenticationRequest authReq)
			throws BadCredentialsException {
		// Validación de campos password y email
		if (authReq.getUsername() != null && authReq.getPassword() != null) {
			UserDetails userDetails = myUserDetailsService.loadUserByUsername(authReq.getUsername());
			// Si hay usuario
			if (userDetails != null) {
				// contraseña coincide
				if (passwordEncoder.matches(authReq.getPassword(), userDetails.getPassword())) {
					try {
						authenticationManagerBean.authenticate(
								new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
					} catch (BadCredentialsException e) {
						e.printStackTrace();
					}

					authenticationManagerBean.authenticate(
							new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword()));
					final String jwt = jwtTokenUtil.generateToken(userDetails);
					return new ResponseEntity<>(new ResponseDto(200, jwt), HttpStatus.OK);
				} else {
					// error contraseña
					return new ResponseEntity<>(new ResponseDto(400, "Incorrect password"), HttpStatus.BAD_REQUEST);
				}
				// no hay usuario
			} else {
				return new ResponseEntity<>(new ResponseDto(400, "Bad credentials"), HttpStatus.BAD_REQUEST);
			}
			// Faltan campos
		} else {
			return new ResponseEntity<>(new ResponseDto(400, "Password and Username are required"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "User details")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Return a information of a user", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = UserRequest.class)) }),
			@ApiResponse(responseCode = "404", description = "    User not found"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@GetMapping("/me")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> authenticate() throws RecordNotExistException {

		User user = userService.getLoggedUser();
		UserRequest userRequest = UserRequest.mapToRequest(user);
		return new ResponseEntity<>(userRequest, HttpStatus.OK);

	}
}
