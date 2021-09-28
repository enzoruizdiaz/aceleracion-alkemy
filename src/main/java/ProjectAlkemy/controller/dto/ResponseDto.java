package ProjectAlkemy.controller.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDto implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3840616156302232680L;
	Integer code;
    String description;
}
