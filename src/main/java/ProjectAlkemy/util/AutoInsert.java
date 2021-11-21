package ProjectAlkemy.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ProjectAlkemy.model.Activity;
import ProjectAlkemy.model.Role;
import ProjectAlkemy.model.Testimonials;
import ProjectAlkemy.model.User;
import ProjectAlkemy.repository.UserRepository;
import ProjectAlkemy.service.ActivityService;
import ProjectAlkemy.service.RoleService;
import ProjectAlkemy.service.TestimonialsService;
import ProjectAlkemy.service.UserService;

@Component
public class AutoInsert implements CommandLineRunner {

	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ActivityService actService;
	@Autowired
	private TestimonialsService testimonialsService;
	
	@Autowired
	private UserRepository userRepository;

	private Role role;
	private User user;

	@Value("${app.sendgrid.emailfrom}")
	private String email;
	@Value("${admin.password}")
	private String password;

	public Activity addActivity(String name, String content, String image) {
		Activity act = new Activity();
		act.setName(name);
		act.setContent(content);
		act.setImage(image);
		act.setSoftDelete(false);
		return act;
	}

	@Override
	public void run(String... args) throws Exception {

		createTestimonial();

		role = new Role();
		role = roleService.findByName("ADMIN");
		if (role == null) {
			role = new Role();
			role.setName("ADMIN");
			role.setDescription("admin user");
			roleService.create(role);

		}
		role = roleService.findByName("USER");
		if (role == null) {
			role = new Role();
			role.setName("USER");
			role.setDescription("default user");
			roleService.create(role);

		}

		user = new User();
		user = userService.findByEmail(email).orElse(null);
		if (user == null) {
			user = new User();
			user.setEmail(email);
			user.setFirstName("ADMIN");
			user.setLastName("ADMIN");
			user.setPassword(passwordEncoder.encode(password));
			user.setSoftDelete(false);
			role = roleService.findByName("ADMIN");
			user.setRoleId(role);
			userService.create(user);

		}
		List<Activity> activities = new ArrayList<>();
		activities.add(addActivity("Activity", "content activity", "activity.jpg"));
		activities.add(addActivity("Activity2", "content activity", "activity.jpg"));
		activities.add(addActivity("Activity3", "content activity", "activity.jpg"));
		for (Activity a : activities) {
			if (actService.findByName(a.getName()) == null) {
				actService.create(a);
			}
		}
		
		if(userRepository.count() == 0) {
			
			User user =  new User();
			user.setFirstName("Juan");
			user.setLastName("Cornejo");
			user.setEmail("juan@gmail.com");
			user.setPassword(passwordEncoder.encode("12131415"));
			user.setSoftDelete(false);
			user.setRoleId(roleService.findByName("USER"));
			userRepository.save(user);
			user = new User();
			user.setFirstName("Admin2");
			user.setLastName("Admin2");
			user.setEmail("Admin2");
			user.setPassword(passwordEncoder.encode("admin123"));
			user.setSoftDelete(false);
			user.setRoleId(roleService.findByName("ADMIN"));
			userRepository.save(user);
		}
	}

	public void createTestimonial() {

		List<Testimonials> list = new ArrayList<>();
		list.add(new Testimonials(1l, "first", "https://ibb.co/M7X8NJK",
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit,", false));
		list.add(new Testimonials(2l, "second", "https://ibb.co/PL8mJKH",
				"sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", false));
		list.add(new Testimonials(3l, "third", "https://ibb.co/4TE8bER", "Ut enim ad minim veniam,", false));
		list.add(new Testimonials(4l, "fourth", "https://ibb.co/PRFV6rs", "quis nostrud exercitation ullamco laboris ",
				false));
		list.add(new Testimonials(5l, "fifth", "https://ibb.co/kPMCR45T", "nisi ut aliquip ex ea commodo consequat.",
				false));
		list.add(new Testimonials(6l, "sixth", "https://ibb.co/ROIE7nv",
				"Duis aute irure dolor in reprehenderit in voluptate", false));
		list.add(new Testimonials(7l, "seventh", "https://ibb.co/JDFOI86d", "velit esse cillum dolore", false));
		list.add(new Testimonials(8l, "eighth", "https://ibb.co/JSHDH787", "eu fugiat nulla pariatur.", false));
		list.add(new Testimonials(9l, "ninth", "https://ibb.co/pwuw74CG",
				"Excepteur sint occaecat cupidatat non proident,", false));
		list.add(new Testimonials(10l, "tenth", "https://ibb.co/pIR75H",
				"sunt in culpa qui officia deserunt mollit anim id est laborum.", false));
		list.add(new Testimonials(11l, "eleventh", "https://ibb.co/LOHG873f", "Lorem ipsum dolor sit amet, consectetur",
				false));
		list.add(new Testimonials(12l, "twelfth", "https://ibb.co/764gUUJOI",
				"adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", false));
		list.add(new Testimonials(13l, "thirteenth", "https://ibb.co/IOWX4Y",
				"Ut enim ad minim veniam, quis nostrud exercitation", false));
		list.add(new Testimonials(14l, "fourteenth", "https://ibb.co/ihgf43hIU",
				"ullamco laboris nisi ut aliquip ex ea commodo consequat. ", false));
		list.add(new Testimonials(15l, "fifteenth", "https://ibb.co/iyu5wsdu",
				"Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore", false));

		list.stream().filter(t -> testimonialsService.findByName(t.getName()) == null)
				.forEach(testimonialsService::create);

	}

}
