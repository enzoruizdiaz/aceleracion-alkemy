package ProjectAlkemy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ProjectAlkemy.controller.dto.OrganizationDTOSimple;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.OrganizationRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Organization;
import ProjectAlkemy.service.OrganizationService;

class OrganizationControllerTest {

	private OrganizationService organizationService;
	private OrganizationController organizationController;
	private Organization org;
	private OrganizationDTOSimple orgDto;
	private List<Organization> orgList = new ArrayList<>();
	private List<OrganizationDTOSimple> listDto = new ArrayList<>();
	private OrganizationRequest orgReq, orgReqNull;

	@BeforeEach
	void setUp() {
		organizationService = Mockito.mock(OrganizationService.class);
		organizationController = new OrganizationController(organizationService);
		org = new Organization(1L, "Somos Mas", "amazon.url", "calle falsa 123", 12345678,
				"ongsomosmas@gmail.com", "Welcome to Somos Mas", "About Somos Mas", "Facebook.url", "LinkEdIn.url",
				"Instagram.url", false);
		orgList.add(org);
		orgDto= OrganizationDTOSimple.mapToDto(org);
		listDto = OrganizationDTOSimple.mapListToDtoList(orgList);
		orgReqNull = new OrganizationRequest();
		orgReq = new OrganizationRequest("Somos Mas", "amazon.url", "calle falsa 123", Integer.valueOf("12345678"),"ongsomosmas@gmail.com", "Welcome to Somos Mas", "About Somos Mas", "Facebook.url", "LinkEdIn.url","Instagram.url");
	}

	@Test
	void organizationDetailsOk() throws ListNotFoundException {
		when(organizationService.getAllNotDelete()).thenReturn(orgList);
		ResponseEntity<?> ok = organizationController.organizationDetails();
		assertEquals(ok,new ResponseEntity<>(listDto,HttpStatus.OK));
		verify(organizationService,times(1)).getAllNotDelete();
	}
	
	@Test
	void organizationDetailsException() throws ListNotFoundException {
		when(organizationService.getAllNotDelete()).thenThrow(new ListNotFoundException("List Exception"));
		assertThrows(ListNotFoundException.class, () -> organizationService.getAllNotDelete());
		verify(organizationService,times(1)).getAllNotDelete();
	}

	@Test
	void organizationEditOk() throws RecordNotExistException {
		when(organizationService.setOrg(orgReq, 1L)).thenReturn(org);
		when(organizationService.update(org)).thenReturn(org);
		ResponseEntity<?> ok = organizationController.organizationEdit(1L, orgReq);
		assertEquals(ok,new ResponseEntity<>(orgDto, HttpStatus.OK));
		verify(organizationService,times(1)).setOrg(orgReq, 1L);
		verify(organizationService,times(1)).update(org);
	}

	@Test
	void organizationEditException() throws RecordNotExistException {
		when(organizationService.setOrg(orgReq,1L)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class, () -> organizationService.setOrg(orgReq,1L));
		verify(organizationService,times(1)).setOrg(orgReq,1L);
	}
	
	@Test
	void createOrgOk() {
		MockedStatic<OrganizationRequest> mb = Mockito.mockStatic(OrganizationRequest.class);
		mb.when(() -> { OrganizationRequest.mapToEntity(orgReq); }).thenReturn(org);
		when(organizationService.create(org)).thenReturn(org);
		ResponseEntity<?> ok = organizationController.createOrg(orgReq);
		assertEquals(ok,new ResponseEntity<>(new ResponseDto(200,"Id: " + org.getId()), HttpStatus.OK));
		verify(organizationService,times(1)).create(org);
	}
	
	@Test 
	void createOrgBadRequest() {
		ResponseEntity<?> ok = organizationController.createOrg(orgReqNull);
		assertEquals(ok,new ResponseEntity<>(new ResponseDto(400,"name and email are required"),HttpStatus.BAD_REQUEST));
	}


	@Test
	void deleteOrganization() throws RecordNotExistException {
		when(organizationService.getById(1L)).thenReturn(org);
		when(organizationService.deleteOrganization(org)).thenReturn(org);
		ResponseEntity<?> ok = organizationController.deleteOrganization(1L);
		assertEquals(ok,new ResponseEntity<>(new ResponseDto(200,"Has been successfully deleted."),HttpStatus.OK));
		verify(organizationService,times(1)).getById(1L);
		verify(organizationService,times(1)).deleteOrganization(org);
	}
	
	@Test
	void deleteOrganizationException() throws RecordNotExistException {
		when(organizationService.getById(1L)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class, () -> organizationService.getById(1L));
		verify(organizationService,times(1)).getById(1L);
	}

	@Test
	void getOrganizationById() throws RecordNotExistException {
		when(organizationService.getById(1L)).thenReturn(org);
		ResponseEntity<?> ok = organizationController.getOrganizationById(1L);
		assertEquals(ok,(new ResponseEntity<>(orgDto,HttpStatus.OK)));
		verify(organizationService,times(1)).getById(1L);
	}
	
	@Test
	void getOrganizationByIdException() throws RecordNotExistException {
		when(organizationService.getById(1L)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class, () -> organizationService.getById(1L));
		verify(organizationService,times(1)).getById(1L);
	}
}