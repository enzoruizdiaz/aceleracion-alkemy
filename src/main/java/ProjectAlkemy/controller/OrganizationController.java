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

import ProjectAlkemy.controller.dto.OrganizationDTOSimple;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.OrganizationRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Organization;
import ProjectAlkemy.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/organization/public")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Organization Controller", description = "All Api Rest methods to manage a Organization")
public class OrganizationController {

	
	private OrganizationService organizationServ;
	
	@Autowired
	public OrganizationController(OrganizationService organizationServ) {
		this.organizationServ = organizationServ;
	}

	@Operation(summary = "List all Organizations in data base")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "    List organizations", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationDTOSimple.class)) }),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	// details of Organizations Objects
	@GetMapping
	public ResponseEntity<?> organizationDetails() throws ListNotFoundException {
		List<Organization> list = organizationServ.getAllNotDelete();
		List<OrganizationDTOSimple> listDto = OrganizationDTOSimple.mapListToDtoList(list);
		return new ResponseEntity<>(listDto,HttpStatus.OK);
	}

	@Operation(summary = "Update a Organization by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a Organization updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationDTOSimple.class)) }),
			@ApiResponse(responseCode = "400", description = "    Malformed request body"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PutMapping("/{id}")
	public ResponseEntity<?> organizationEdit(
			@Parameter(description = "	Id of Organization to be updated") @PathVariable("id") Long id,
			@RequestBody OrganizationRequest orgRequest)
			throws RecordNotExistException{
		
		Organization update = organizationServ.setOrg(orgRequest, id);
		organizationServ.update(update);
		OrganizationDTOSimple auxDto = OrganizationDTOSimple.mapToDto(update);
		return new ResponseEntity<>(auxDto, HttpStatus.OK);

	}

	@Operation(summary = "Create a new Organization")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a Organization created", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "    Name and content are required"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PostMapping()
	public ResponseEntity<?> createOrg(@RequestBody OrganizationRequest orgRequest) {

		if (orgRequest.getName() == null || orgRequest.getEmail() == null) {
			return new ResponseEntity<>(new ResponseDto(400,"name and email are required"),HttpStatus.BAD_REQUEST);
		} else {
			Organization org = OrganizationRequest.mapToEntity(orgRequest);
			org.setSoftDelete(false);
			org = organizationServ.create(org);
			return new ResponseEntity<>(new ResponseDto(200,"Id: " + org.getId()), HttpStatus.OK);

		} 
	}

	@Operation(summary = "Delete a Organization by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a Organization deleted", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "    Id does not exist."), 
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteOrganization(
			@Parameter(description = "	Id of Organization to be deleted") @PathVariable("id") Long id)
			throws RecordNotExistException{

		Organization org = organizationServ.getById(id);
		organizationServ.deleteOrganization(org);
		return new ResponseEntity<>(new ResponseDto(200,"Has been successfully deleted."), HttpStatus.OK);

	}
	
	@Operation(summary = "Get a Organization by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	Organization details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = OrganizationDTOSimple.class)) }),
			@ApiResponse(responseCode = "404", description = "	Organization not found"),
			@ApiResponse(responseCode = "500", description = "	Internal Server Error") })
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getOrganizationById(
			@Parameter(description = "	Id of Organization to be searched") @PathVariable Long id) throws RecordNotExistException {
		Organization org = organizationServ.getById(id);
		OrganizationDTOSimple orgDto = OrganizationDTOSimple.mapToDto(org);
		return new ResponseEntity<>(orgDto,HttpStatus.OK);

	}

}
