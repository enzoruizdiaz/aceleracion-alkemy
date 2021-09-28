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
@Table(name = "testimonials")

public class Testimonials extends Base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "testimonials_id", nullable = false, unique = true)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;
	@Column
	private String image;
	@Column
	private String content;

	@Column(nullable = false)
	private boolean softDelete;

}
