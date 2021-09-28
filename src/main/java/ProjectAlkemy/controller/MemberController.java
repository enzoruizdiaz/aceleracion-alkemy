package ProjectAlkemy.controller;

import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ProjectAlkemy.controller.dto.MemberDto;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.MemberRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Member;
import ProjectAlkemy.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/members")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Member Controller", description = "All Api Rest methods to manage a Members")
public class MemberController {


	private MemberService memberService;
	
	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}
	

	@Operation(summary = "Create a new Member")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a Member created", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "    Name are required"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PostMapping()
	public ResponseEntity<?> createMember(@RequestBody MemberRequest memberRequest) {

		if (memberRequest.getName() == null || memberRequest.getImage() == null) {
			return new ResponseEntity<>(new ResponseDto(400,"Name and image are required"),HttpStatus.BAD_REQUEST);
		} else {
			Member member = MemberRequest.mapToEntity(memberRequest);
			member.setSoftDelete(false);
			member = memberService.create(member);
			return new ResponseEntity<>(new ResponseDto(200,"id :" + member.getId()), HttpStatus.OK);

		}

	}

	@Operation(summary = "Get Members")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Member list details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MemberDto.class)) }),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllMember(
			@Parameter(description= "	Selected page")@RequestParam(defaultValue = "0") int page,
			@Parameter(description = "	Selected size page")@RequestParam(defaultValue = "10") int size) throws ListNotFoundException {

		Map<String,Object> response = memberService.getAllMemberPaged(page, size);
		return new ResponseEntity<>(response, HttpStatus.OK);
	} 
	

	@Operation(summary = "Delete a Member by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a Member deleted", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "    Id does not exist."),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMember(
			@Parameter(description = "	ID of Member to be deleted")
			@PathVariable("id") Long id) throws RecordNotExistException{

			Member memb = memberService.getById(id);
			memberService.deleteMember(memb);
			return new ResponseEntity<>(new ResponseDto(200,"has been successfully deleted."), HttpStatus.OK);

		}
	

	@Operation(summary = "Update a Member by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a Member updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MemberDto.class)) }),
			@ApiResponse(responseCode = "404", description = "    Id does not exist."),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PutMapping("/{id}")
	public ResponseEntity<?> MemberUpdate(
			@Parameter(description = "	ID of Member to be updated") @PathVariable("id") Long id,
			@RequestBody MemberRequest membRequest) throws RecordNotExistException {
	
			Member update = memberService.setMember(membRequest, id);
			update = memberService.update(update);
			MemberDto membDto = MemberDto.entityToDto(update);
			return new ResponseEntity<>(membDto, HttpStatus.OK);

		} 
	
	
	@Operation(summary = "Get a Members by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	Members details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MemberDto.class)) }),
			@ApiResponse(responseCode = "404", description = "	Members not found"),
			@ApiResponse(responseCode = "500", description = "	Internal Server Error") })
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getMemberById(
			@Parameter(description = "	Id of Members to be searched") @PathVariable Long id) throws RecordNotExistException {
		Member members = memberService.getById(id);
		MemberDto membDto = MemberDto.entityToDto(members);
		return new ResponseEntity<>(membDto,HttpStatus.OK);

	}
}
