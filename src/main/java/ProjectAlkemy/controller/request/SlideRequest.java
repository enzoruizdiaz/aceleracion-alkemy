package ProjectAlkemy.controller.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlideRequest implements Serializable {

	private static final long serialVersionUID = -6280564330081832259L;
	private String text;
	private Integer order;
	private String imageUrl;
	private Long fkidOrganization;
}
