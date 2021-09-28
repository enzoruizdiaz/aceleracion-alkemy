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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "activities")
public class Activity extends Base{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="activity_id",nullable = false, unique = true)
	private Long id;
	
	@Column(nullable =false)
	private String name;
	@Column(nullable =false, columnDefinition = "text")
	private String  content;
	
	private  String image;
	
	@Column(name = "soft_delete",columnDefinition="tinyint(1) default 0")
	private Boolean softDelete;
	
}
