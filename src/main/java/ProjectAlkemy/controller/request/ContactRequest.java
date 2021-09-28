package ProjectAlkemy.controller.request;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ContactRequest {
	private String name;

	private String phone;

	private String email;

	private String message;
	
	
	@Autowired
	private static ModelMapper mm = new ModelMapper();
	
	public static ContactRequest mapToRequest(Contact cont) {
		ContactRequest contactReq = mm.map(cont, ContactRequest.class);

		return contactReq;
	}

	public static Contact mapToEntity(ContactRequest contactReq) {
		Contact contact = mm.map(contactReq, Contact.class);
		return contact;
	}
}
