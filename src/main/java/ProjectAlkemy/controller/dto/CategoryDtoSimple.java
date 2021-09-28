package ProjectAlkemy.controller.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDtoSimple implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6504810742304809576L;

	private String name;

    @Autowired
	private static ModelMapper mapper = new ModelMapper();
    
	//method to map a Organization Object to a dto object using model mapper
	public static CategoryDtoSimple entityToDto(Category category) {
		CategoryDtoSimple aux = mapper.map(category,CategoryDtoSimple.class);
		return aux;
	}
    
    public static List<CategoryDtoSimple> EntitiesToDtos(List<Category> c) {
    	List<CategoryDtoSimple> categories = new ArrayList<CategoryDtoSimple>();
    	c.forEach(entity ->{ categories.add(entityToDto(entity));});
    	return categories;
    }
}
