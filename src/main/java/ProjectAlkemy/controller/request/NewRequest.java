package ProjectAlkemy.controller.request;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.News;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewRequest {

	private String name;

	private String content;

	private String image;

	private Long idCategory;
	
	@Autowired
	private static ModelMapper mapper = new ModelMapper();

	public static News mapToEntity(NewRequest newReq) {
		News news = mapper.map(newReq, News.class);
		return news ;
	}
	
	
}
