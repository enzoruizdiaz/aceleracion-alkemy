package ProjectAlkemy.controller.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.Slide;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlideDtoSimple implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4755927223052982808L;
	private String imageUrl;
	private Integer order;
	
	@Autowired
	private static ModelMapper mapper = new ModelMapper();
	
	//map Slide entity to slideDTo method
	public static SlideDtoSimple mapToDto(Slide s) {
		SlideDtoSimple slideDtoSimple = mapper.map(s, SlideDtoSimple.class);
		return slideDtoSimple;
	}
	
	// map Slide list to SlideDto list method
	public static List<SlideDtoSimple> mapToDtoList (List<Slide> slides){
		List<SlideDtoSimple> listed = new ArrayList<>();
		for (Slide s : slides) {
			listed.add(mapToDto(s));
		}
		return listed;
	}
}
