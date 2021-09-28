package ProjectAlkemy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ProjectAlkemy.service.MyUserDetailsService;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}
	
    private static final String[] ADMIN_PATHLIST = {
    		"/users/**",
    		"/categories",
			"/testimonials/**",
			"/news/**",
    		"/slides/**",
			"/activities/**",
    		"/contacts/**",
			"/members/**",
			"/organization/public/**"
    };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		  .antMatchers("/auth/register","/auth/login","/auth/me").permitAll()  //hasAnyRole("USER", "ADMIN")
		  .antMatchers( "/api/**","/swagger-ui-custom.html","/swagger-ui/**").permitAll()
		  .antMatchers(HttpMethod.GET,"/categories").hasAnyRole("USER","ADMIN")
		  .antMatchers(HttpMethod.GET,"/organization/public").hasAnyRole("USER","ADMIN")//hasAuthority("ROLE_USER")
		  .antMatchers(HttpMethod.POST,"/contacts").hasAnyRole("USER", "ADMIN")
		  .antMatchers(HttpMethod.POST, "/comments").hasAnyRole("USER","ADMIN")
		  .antMatchers(HttpMethod.GET, "/comments").hasAnyRole("ADMIN")
		  .antMatchers(ADMIN_PATHLIST).hasRole("ADMIN")//hasAuthority("ROLE_ADMIN")
		  .anyRequest().authenticated().and().sessionManagement()
		  .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(myUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
}
