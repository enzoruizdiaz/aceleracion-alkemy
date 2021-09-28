package ProjectAlkemy.controller.request;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryRequest {
	private String name;
	private String description;
	private String image;

	
	
	
	@Autowired
	private static ModelMapper mm = new ModelMapper();
	
	public static CategoryRequest mapToRequest(Category cat) {
	CategoryRequest categoryReq = mm.map(cat, CategoryRequest.class);

		return categoryReq;
	}

	public static Category mapToEntity(CategoryRequest categoryReq) {
		Category cat = mm.map(categoryReq, Category.class);
		return cat;
	}
}
