package ProjectAlkemy.controller.request;

import java.io.Serializable;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest implements Serializable {
	
	private static final long serialVersionUID = 2691992794276295875L;
	
	private Long fk_newId;
	private String Body;
	
	@Autowired
	private static ModelMapper mapper = new ModelMapper();
	
	public static Comment mapToDto (CommentRequest cRequest) {
		Comment comment = mapper.map(cRequest, Comment.class);
		return comment;
	}
}
