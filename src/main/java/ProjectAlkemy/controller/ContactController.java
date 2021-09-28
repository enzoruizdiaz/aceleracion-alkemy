package ProjectAlkemy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ProjectAlkemy.config.SengridConfig;
import ProjectAlkemy.controller.dto.ContactDto;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.ContactRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Contact;
import ProjectAlkemy.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/contacts")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Contact Controller", description = "All Api Rest methods to manage a Contact form")
public class ContactController {

	
	private ContactService contactService;


	 private SengridConfig sengridConfig;

	@Autowired
	public ContactController(ContactService contactService2, SengridConfig sengridConfig) {
		this.contactService = contactService2;
		this.sengridConfig = sengridConfig;
	}

	@Operation(summary = "Create a new Contact")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show Id of a Contact created", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "    Contact not found"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PostMapping
	public ResponseEntity<?> createContact(@RequestBody ContactRequest contactReq){
		if ((contactReq.getName() != null && !contactReq.getName().isEmpty())
				&& (contactReq.getEmail() != null && !contactReq.getEmail().isEmpty())) {

			Contact contact = ContactRequest.mapToEntity(contactReq);
			contact.setSoftDelete(false); 
			contact = contactService.create(contact);  
			try {
				sengridConfig.sendMail(contact.getEmail(), "CONTACT");
				return new ResponseEntity<>(new ResponseDto(200, "Id :" + contact.getId()), HttpStatus.OK);
			} catch (Exception e) { 
				return new ResponseEntity<>(new ResponseDto(200, "failed to send email to:\n" + contact.getEmail()),
						HttpStatus.OK);  
			}

		} else {
			return new ResponseEntity<>(new ResponseDto(404, "The fields name and email are required"),
					HttpStatus.BAD_REQUEST);
		}
	} 

	@Operation(summary = "Update a Contact by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show Id of a Contact updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ContactDto.class)) }),
			@ApiResponse(responseCode = "404", description = "    Contact not found"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })

	@PutMapping("/{id}")
	public ResponseEntity<?> updateContacts(
			@Parameter(description = "	ID of Contact to be updated") @PathVariable("id") Long id,
			@RequestBody ContactRequest contactReq) throws RecordNotExistException{
		Contact update = contactService.setCont(contactReq, id);
		update = contactService.update(update);
		ContactDto auxDto = ContactDto.entityToDto(update);
		return new ResponseEntity<>(auxDto, HttpStatus.OK); 
	}

	@Operation(summary = "Delete a Contact by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show Id of a Contact deleted", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "    Contact not found"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteContacts(
			@Parameter(description = "	ID of Contact to be deleted") @PathVariable("id") Long id)
			throws RecordNotExistException{
		Contact contact = contactService.getById(id);
		contact = contactService.softDelete(contact); 
		return new ResponseEntity<>(new ResponseDto(200, "The Contact has been deleted successfully"), HttpStatus.OK);
	}

	@Operation(summary = "Get a list of all Contacts ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "     Return empty Contact or a list of Contacts", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ContactDto.class)) }),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@GetMapping
	public ResponseEntity<?> listContact() throws ListNotFoundException {

		List<Contact> contacts = contactService.getAllNotDelete();

		List<ContactDto> contactDto = ContactDto.entitiesToDtos(contacts);
		return new ResponseEntity<>(contactDto, HttpStatus.OK);
 
	}

	@Operation(summary = "Get a Contact by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Contact details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ContactDto.class)) }),
			@ApiResponse(responseCode = "404", description = "    Contact not found"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@GetMapping("/{id}")
	public ResponseEntity<?> detailContact(
			@Parameter(description = "	ID of Contact to view") @PathVariable("id") Long id)
			throws RecordNotExistException {
		Contact contact = contactService.getById(id);
		ContactDto contactDto= ContactDto.entityToDto(contact);
		return new ResponseEntity<>(contactDto, HttpStatus.OK);
	}

}
