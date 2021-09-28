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
public class CommentBodyRequest implements Serializable {
    private static final long serialVersionUID = -4582227043675481556L;

    private String Body;

    @Autowired
    private static ModelMapper mapper = new ModelMapper();

    public static Comment mapToDto (CommentRequest cRequest) {
        Comment comment = mapper.map(cRequest, Comment.class);
        return comment;
    }
}
