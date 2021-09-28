package ProjectAlkemy.controller.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ProjectAlkemy.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 544081899412473308L;
	private String name;
	private String description;
	private String image;
	
	@JsonIgnoreProperties(value = "categoryId")
	private List<NewsDto> news= new ArrayList<>();
	
    @Autowired
	private static ModelMapper mapper = new ModelMapper();
    
	public static CategoryDto entityToDto(Category category) {
		CategoryDto aux = mapper.map(category,CategoryDto.class);
		return aux;
	}
    
    public static List<CategoryDto> EntitiesToDtos(List<Category> c) {
    	List<CategoryDto> categories = new ArrayList<CategoryDto>();
    	c.forEach(entity ->{ categories.add(entityToDto(entity));});
    	return categories;
    }


}
