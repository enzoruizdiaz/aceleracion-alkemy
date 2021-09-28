package ProjectAlkemy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organizations")
public class Organization extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "organization_id", nullable = false)
	private long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String image;

	@Column(nullable = true)
	private String address;

	@Column(nullable = true)
	private Integer phone;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String welcomeText;

	@Column(nullable = true)
	private String aboutUs;
	
	@Column(nullable = true)
	private String facebook;
	
	@Column(nullable = true)
	private String linkedin;
	
	@Column(nullable = true)
	private String instagram;

	@Column(name = "softDelete", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
	private Boolean softDelete;

}
