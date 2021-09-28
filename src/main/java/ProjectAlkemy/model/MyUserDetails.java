package ProjectAlkemy.model;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class MyUserDetails extends Base implements UserDetails {

	private static final long serialVersionUID = 5402242263237697291L;
	private String userName;
	private String password;
	private boolean active;
	private Collection<? extends GrantedAuthority> authorities;

	
	public MyUserDetails() {

	}

	public MyUserDetails(User user) {
		this.userName = user.getEmail();
		this.password = user.getPassword();
		this.active = user.getSoftDelete();
		this.authorities = this.getAuthority(user.getRoleId());
	}
		
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return this.authorities;
	}

	
	private Collection<? extends GrantedAuthority> getAuthority(Role role)  {
		List<GrantedAuthority> authority = new ArrayList<GrantedAuthority>();
			authority.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));

		return authority;
	}

	

}
