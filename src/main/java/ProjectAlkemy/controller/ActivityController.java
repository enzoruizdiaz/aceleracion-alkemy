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

import ProjectAlkemy.controller.dto.ActivityDto;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.ActivityRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Activity;
import ProjectAlkemy.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/activities")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Activity Controller", description = "All Api Rest methods to manage a Activities")
public class ActivityController {

	
	private ActivityService actService;

	@Autowired
	public ActivityController(ActivityService activityService) {
		this.actService = activityService;
	}

	@Operation(summary = "Create a new Activity")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a Activity created", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "    Name and content are required"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PostMapping()
	public ResponseEntity<?> createActivity(
			@Parameter(description = "		Name and content are required") @RequestBody ActivityRequest activityRequest) {

		if (activityRequest.getName() == null || activityRequest.getContent() == null) {
			return new ResponseEntity<>(new ResponseDto(400, "The field name and content are required."),
					HttpStatus.BAD_REQUEST);
		} else {
			Activity act = ActivityRequest.mapToEntity(activityRequest);
			act.setSoftDelete(false);
			act = actService.create(act);
			return new ResponseEntity<>(new ResponseDto(200, "Id: " + act.getId()), HttpStatus.OK);

		}
	}

	@Operation(summary = "Update a Activity by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a Activity updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = ActivityDto.class)) }),
			@ApiResponse(responseCode = "400", description = "    Malformed request body"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateActivity(
			@Parameter(description = "	Id of Activity to be updated") @PathVariable("id") Long id,
			@RequestBody ActivityRequest actRequest) throws RecordNotExistException {

		Activity update = actService.setAct(actRequest, id);
		update = actService.update(update);
		ActivityDto actReq = ActivityDto.mapToDto(update);

		return new ResponseEntity<>(actReq, HttpStatus.OK);

	}

	@Operation(summary = "Delete a Activity by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a Activity deleted.", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "    Id does not exist."),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error.") })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteActivity(
			@Parameter(description = "	Id of Activity to be deleted") @PathVariable("id") Long id)
			throws RecordNotExistException {

		Activity act = actService.getById(id);
		actService.deleteActivity(act);
		return new ResponseEntity<>(new ResponseDto(200, "Has been successfully deleted."), HttpStatus.OK);
	} 

	@Operation(summary = "Get Activities")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "    Activity list details", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = ActivityDto.class)) }),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@GetMapping
	public ResponseEntity<?> listActivities() throws ListNotFoundException {
		List<Activity> acts = actService.getAllNotDelete();
		List<ActivityDto> actDto = ActivityDto.mapToListDto(acts);
		return new ResponseEntity<>(actDto, HttpStatus.OK);
	}

}
