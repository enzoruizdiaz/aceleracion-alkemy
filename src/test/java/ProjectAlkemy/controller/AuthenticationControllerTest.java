package ProjectAlkemy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import ProjectAlkemy.config.SengridConfig;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.AuthenticationRequest;
import ProjectAlkemy.controller.request.UserRequest;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.MyUserDetails;
import ProjectAlkemy.model.Role;
import ProjectAlkemy.model.User;
import ProjectAlkemy.service.MyUserDetailsService;
import ProjectAlkemy.service.UserService;
import ProjectAlkemy.util.JwtUtil;

class AuthenticationControllerTest {

	private UserService userService;
	private MyUserDetailsService myUserDetailsService;
	private AuthenticationController authController;
	private User user;
	private MyUserDetails myUserDetails;
	private UserRequest userRequest, userRequestMap;
	private AuthenticationRequest authRequest;
	AuthenticationManager authenticationManagerBean;
	private JwtUtil jwtTokenUtil;
	private SengridConfig sengridConfig;
	private PasswordEncoder passwordEncoder;
	ArrayList<User> list;

	@BeforeEach
	void setUp() {
		userService = Mockito.mock(UserService.class);
		myUserDetailsService = Mockito.mock(MyUserDetailsService.class);
		myUserDetailsService = Mockito.mock(MyUserDetailsService.class);
		authenticationManagerBean = Mockito.mock(AuthenticationManager.class);
		jwtTokenUtil = Mockito.mock(JwtUtil.class);
		sengridConfig = Mockito.mock(SengridConfig.class);
		passwordEncoder = Mockito.mock(PasswordEncoder.class);
		authController = new AuthenticationController(userService, authenticationManagerBean, myUserDetailsService,
				jwtTokenUtil, sengridConfig, passwordEncoder);
		user = new User("firstName", "lastName", "email", "password", false, null);
		user.setSoftDelete(null);
		userRequest = new UserRequest("firstName", "lastName", "email", "password", null);
		authRequest = new AuthenticationRequest("email", "password");
		userRequestMap = UserRequest.mapToRequest(user);
		list = new ArrayList<>();
		myUserDetails = new MyUserDetails(
				new User("firstName", "lastName", "email", "password", false, new Role(1L, "USER", "user", list)));
		
		
	}

	@Test // register
	void createUser() {

		User aux = UserRequest.mapToEntity(userRequest);
		when(userService.createUser(user)).thenReturn(user);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null);
		when(authenticationManagerBean.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		when(myUserDetailsService.loadUserByUsername(user.getEmail())).thenReturn(myUserDetails);
		ResponseEntity ok = authController.createUser(userRequest);

		assertEquals(ok, new ResponseEntity<>(new ResponseDto(200, "Id: " + user.getId() + "   Token: " + "null"),
				HttpStatus.OK));
		verify(userService, times(1)).createUser(user);
	}
	
	@Test
	void createUserBadRequest() {
		ResponseEntity ok = authController.createUser(new UserRequest(null, null, null, null, null));
		assertEquals(ok,new ResponseEntity<>(new ResponseDto(400,"firstname, lastname, password and email are required."),
				HttpStatus.BAD_REQUEST)); 
	}
	@Test
	void createUserException() throws IOException {
	
		when(userService.createUser(user)).thenReturn(user);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null);
		when(authenticationManagerBean.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		when(myUserDetailsService.loadUserByUsername(user.getEmail())).thenReturn(myUserDetails);
		doThrow(new IOException()).when(sengridConfig).sendMail(user.getEmail(), "REGISTER");;
		ResponseEntity ok = authController.createUser(userRequest);

		assertEquals(ok, new ResponseEntity<>(new ResponseDto(200, "Failed to send email to: "  + user.getEmail() +"  Token: " +"null"),
				HttpStatus.OK));
		verify(userService, times(1)).createUser(user);
	 
	}

	@Test // login
	void createAuthentication() {

		when(myUserDetailsService.loadUserByUsername(authRequest.getUsername())).thenReturn(myUserDetails);
		when(passwordEncoder.matches(userRequest.getPassword(), myUserDetails.getPassword())).thenReturn(true);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null);
		when(authenticationManagerBean.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		when(jwtTokenUtil.generateToken(myUserDetails)).thenReturn("123");

		ResponseEntity ok = authController.createAuthentication(authRequest);
		assertEquals(ok, new ResponseEntity<>(new ResponseDto(200, "123"), HttpStatus.OK));

	}

	@Test // login
	void createAuthenticationExceptionCredential() throws BadCredentialsException {

		when(myUserDetailsService.loadUserByUsername(authRequest.getUsername())).thenReturn(myUserDetails);
		when(passwordEncoder.matches(userRequest.getPassword(), myUserDetails.getPassword())).thenReturn(true);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null);
		when(authenticationManagerBean.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new BadCredentialsException(""));

		assertThrows(BadCredentialsException.class, () -> authController.createAuthentication(authRequest));

	}

	@Test
	void createAuthenticationBadRequest() {

		when(myUserDetailsService.loadUserByUsername(authRequest.getUsername())).thenReturn(myUserDetails);
		when(passwordEncoder.matches(userRequest.getPassword(), myUserDetails.getPassword())).thenReturn(false);

		ResponseEntity ok = authController.createAuthentication(authRequest);
		assertEquals(ok, new ResponseEntity<>(new ResponseDto(400, "Incorrect password"), HttpStatus.BAD_REQUEST));

	}

	@Test
	void createAuthenticationBadCredential() {

		when(myUserDetailsService.loadUserByUsername(authRequest.getUsername())).thenReturn(null);
		when(passwordEncoder.matches(userRequest.getPassword(), myUserDetails.getPassword())).thenReturn(false);

		ResponseEntity ok = authController.createAuthentication(authRequest);
		assertEquals(ok, new ResponseEntity<>(new ResponseDto(400, "Bad credentials"), HttpStatus.BAD_REQUEST));

	}

	@Test
	void createAuthenticationPasswordAndNameRequire() {

		ResponseEntity ok = authController.createAuthentication(new AuthenticationRequest(null, null));
		assertEquals(ok, new ResponseEntity<>(new ResponseDto(400, "Password and Username are required"),
				HttpStatus.BAD_REQUEST));

	}

	@Test
	void authenticate() throws RecordNotExistException {
		when(userService.getLoggedUser()).thenReturn(user);
		ResponseEntity ok = authController.authenticate();
		assertEquals(ok, new ResponseEntity<>(userRequestMap, HttpStatus.OK));
		verify(userService, times(1)).getLoggedUser();

	}

	@Test
	void authenticateException() throws RecordNotExistException {
		when(userService.getLoggedUser()).thenThrow(new RecordNotExistException("not Found"));
		assertThrows(RecordNotExistException.class, () -> authController.authenticate());
		verify(userService, times(1)).getLoggedUser();
	}
}