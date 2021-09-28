package ProjectAlkemy.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import ProjectAlkemy.model.User;
import ProjectAlkemy.service.RoleService;
import ProjectAlkemy.service.UserService;

@Configuration
public class SeedDataUsers {
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	  @Bean
		CommandLineRunner commandLineRunner() {
			return args ->{
				
	User user = userService.findByEmail("juan@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Juan",
				"Cornejo",
				"juan@gmail.com",
				passwordEncoder.encode("12131415"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
    user = userService.findByEmail("pedro@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Pedro",
				"Alva",
				"pedro@gmail.com",
				passwordEncoder.encode("21232425"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
	user = userService.findByEmail("lucas@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Lucas",
				"Neira",
				"lucas@gmail.com",
				passwordEncoder.encode("31323435"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
	user = userService.findByEmail("pino@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Julio",
				"Pino",
				"pino@gmail.com",
				passwordEncoder.encode("41424345"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
	user = userService.findByEmail("rojo@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Pamela",
				"Rojo",
				"rojo@gmail.com",
				passwordEncoder.encode("51525354"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
	user = userService.findByEmail("gaspar@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Gaspar",
				"Conejo",
				"gaspar@gmail.com",
				passwordEncoder.encode("54535251"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
	user = userService.findByEmail("hidalgo_italo@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Italo",
				"Hidalgo",
				"hidalgo_italo@gmail.com",
				passwordEncoder.encode("qqwess12"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
	user = userService.findByEmail("perlasa@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Irina",
				"Perlasa",
				"perlasa@gmail.com",
				passwordEncoder.encode("plkm12kj"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
	user = userService.findByEmail("sebastian@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Sebastian",
				"Garcia",
				"sebastian@gmail.com",
				passwordEncoder.encode("jhytgf90"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
	user = userService.findByEmail("leni@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Leni",
				"Dalta",
				"leni@gmail.com",
				passwordEncoder.encode("akakak12"),
				false,
				roleService.findByName("USER"));
		userService.create(user);
	}
	
	//ADMINS
	user = userService.findByEmail("aylen@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Aylen",
				"Cruzado",
				"aylen@gmail.com",
				passwordEncoder.encode("aswenh47"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
    user = userService.findByEmail("lee@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Pepe",
				"Lee",
				"lee@gmail.com",
				passwordEncoder.encode("aswstfkl"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
	user = userService.findByEmail("cerna@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Juana",
				"Cerna",
				"cerna@gmail.com",
				passwordEncoder.encode("juhygtdf"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
	user = userService.findByEmail("need@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Piter",
				"Need",
				"need@gmail.com",
				passwordEncoder.encode("mknjhyfr"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
	user = userService.findByEmail("luna@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Luna",
				"Rondon",
				"luna@gmail.com",
				passwordEncoder.encode("lokidedf"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
	user = userService.findByEmail("cheen@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Jun",
				"Cheen",
				"cheen@gmail.com",
				passwordEncoder.encode("hnbgvfdc"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
	user = userService.findByEmail("farinia@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Octavio",
				"Farinia",
				"farinia@gmail.com",
				passwordEncoder.encode("iouytrwe"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
	user = userService.findByEmail("prado@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Juli",
				"Prado",
				"prado@gmail.com",
				passwordEncoder.encode("njl15479"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
	user = userService.findByEmail("biardy@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Inna",
				"Biardy",
				"biardy@gmail.com",
				passwordEncoder.encode("lklñkjiu"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
	user = userService.findByEmail("londra@gmail.com").orElse(null);
	if(user == null) {
		user = new User(
				"Paulo",
				"Londra",
				"londra@gmail.com",
				passwordEncoder.encode("klyterhn"),
				false,
				roleService.findByName("ADMIN"));
		userService.create(user);
	}
	
			};
		}
}
