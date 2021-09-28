package ProjectAlkemy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ProjectAlkemy.config.SengridConfig;
import ProjectAlkemy.controller.dto.ContactDto;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.ContactRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Contact;
import ProjectAlkemy.service.ContactService;

class ContactControllerTest {

	private ContactController contactController;
	private ContactDto contactDto;
	private ContactService contactService;
	private Contact contact;
	private ContactRequest contactRequest;
	private SengridConfig sengridConfig;
	private ContactRequest contactRequestNull;
	private List<Contact> contacts = new ArrayList<>();
	private List<Contact> contactsNull = new ArrayList<>();
	
	
    @BeforeEach
    void setUp() {
    	contactService = Mockito.mock(ContactService.class);
        sengridConfig = Mockito.mock(SengridConfig.class);
    	contactController = new ContactController(contactService,sengridConfig);
    	contact = new Contact(1L,"name","6114923","crespinpierre98@gmail.com","message",false);
    	contactDto = ContactDto.entityToDto(contact);
    	contactRequest = new ContactRequest("name","6114923","crespinpierre98@gmail.com","message");
    	contactRequestNull = new ContactRequest();
    	contacts.add(contact); 
    	
    }

    @Test
    void createContact() throws IOException{
    	MockedStatic<ContactRequest> mb = Mockito.mockStatic(ContactRequest.class);
		mb.when(() -> { ContactRequest.mapToEntity(contactRequest); }).thenReturn(contact);
		when(contactService.create(contact)).thenReturn(contact);
		doNothing().when(sengridConfig).sendMail(contact.getEmail(),"CONTACT");
		ResponseEntity<?> ok = contactController.createContact(contactRequest);
	    assertEquals(ok, new ResponseEntity<>(new ResponseDto(200,"Id :" + contact.getId()), HttpStatus.OK));
		verify(contactService,times(1)).create(contact);
		
		 
	}

    @Test
    void createContactException() throws IOException {
		when(contactService.create(contact)).thenReturn(contact);
       doThrow(new IOException()).when(sengridConfig).sendMail(contact.getEmail() ,"CONTACT" );
        ResponseEntity<?> ok = contactController.createContact(contactRequest);
       assertEquals(ok,new ResponseEntity<>(new ResponseDto(200, "failed to send email to:\n" + contact.getEmail()),
				HttpStatus.OK));
       verify(sengridConfig,times(1)).sendMail(contact.getEmail(), "CONTACT");
       
	}
    	
    @Test
	void createContactBadRequest() throws RecordNotExistException {
    	ResponseEntity<?> ok = contactController.createContact(contactRequestNull);
		assertEquals(ok,new ResponseEntity<>(new ResponseDto(404, "The fields name and email are required"),
				HttpStatus.BAD_REQUEST)); 
    }	
    	
    
    @Test
    void updateContacts() throws RecordNotExistException {
		when(contactService.setCont(contactRequest, 1L)).thenReturn(contact);
		when(contactService.update(contact)).thenReturn(contact);
		ResponseEntity<?> ok = contactController.updateContacts(1L,contactRequest);
		assertEquals(ok,new ResponseEntity<>(contactDto, HttpStatus.OK));
		verify(contactService,times(1)).setCont(contactRequest, 1L);
		verify(contactService,times(1)).update(contact); 	
    }
    
	@Test
	void updateNewsExceptionSetContacts() throws RecordNotExistException {
		when(contactService.setCont(contactRequest,1L)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class,()-> contactController.updateContacts(1L, contactRequest));
		verify(contactService,times(1)).setCont(contactRequest, 1L);
	}
 
    @Test  
    void deleteContacts() throws RecordNotExistException {
    	when(contactService.getById(1L)).thenReturn(contact);
    	when(contactService.softDelete(contact)).thenReturn(contact);
    	ResponseEntity<?> ok = contactController.deleteContacts(1L);
		assertEquals(ok,new ResponseEntity<>(new ResponseDto(200,"The Contact has been deleted successfully"), HttpStatus.OK));
		verify(contactService,times(1)).getById(1L);
		verify(contactService,times(1)).softDelete(contact);
    }

	@Test
	void deleteContactsExceptionGetById() throws RecordNotExistException {
		when(contactService.getById(1L)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class,()-> contactController.deleteContacts(1L));
		verify(contactService,times(1)).getById(1L);
	}  
    @Test
    void listContact() throws ListNotFoundException {
    	when(contactService.getAllNotDelete()).thenReturn(contacts);
    	ResponseEntity<?> ok = contactController.listContact();
		assertEquals(ok,new ResponseEntity<>(ContactDto.entitiesToDtos(contacts), HttpStatus.OK));
		verify(contactService,times(1)).getAllNotDelete();
    }
    
    @Test
    void listContactException() throws ListNotFoundException {
    	when(contactService.getAllNotDelete()).thenReturn(contactsNull);
    	ResponseEntity<?> ok = contactController.listContact();
		assertEquals(ok,new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK));
    }
 
    @Test
    void detailContact() throws RecordNotExistException {
    	when(contactService.getById(1L)).thenReturn(contact);
    	ResponseEntity<?> ok = contactController.detailContact(1L);
		assertEquals(ok,new ResponseEntity<>(ContactDto.entityToDto(contact), HttpStatus.OK));
		verify(contactService,times(1)).getById(1L);
    }
    
    @Test
    void detailContactException() throws RecordNotExistException {
    	when(contactService.getById(1L)).thenThrow(new RecordNotExistException("record not exist"));
    	assertThrows(RecordNotExistException.class,()-> contactController.detailContact(1L));
		verify(contactService,times(1)).getById(1L);
	}
    
}