package ProjectAlkemy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "comments")
public class Comment extends Base{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name = "comment_Id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "soft_delete", columnDefinition = "BOOLEAN DEFAULT false")
	private Boolean softDelete;
	
	@ManyToOne
	@JoinColumn(name = "FK_user")
	private User user_id;
	
	@ManyToOne
	@JoinColumn(name = "FK_new")
	private News new_id;
	
	private String body;
}
