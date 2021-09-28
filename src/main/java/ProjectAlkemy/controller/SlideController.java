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

import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.dto.SlideDto;
import ProjectAlkemy.controller.dto.SlideDtoSimple;
import ProjectAlkemy.controller.request.SlideRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Slide;
import ProjectAlkemy.service.SlideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/slides")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Slide Controller", description = "All Api Rest methods to manage a Slides")
public class SlideController {

	@Autowired
	private SlideService slideService;

	@Operation(summary = "Get all slides in data base")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	List all slides in data base", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = SlideDtoSimple.class)) }),
			@ApiResponse(responseCode = "500", description = "	Internal Server Error") })

	@GetMapping
	public ResponseEntity<?> getallSlides() throws ListNotFoundException {

		List<Slide> slides = slideService.getAllSlidesOrderer();
		if (slides.isEmpty()) {
			return new ResponseEntity<>(slides, HttpStatus.OK);
		} else {
			List<SlideDtoSimple> slidesDtos = SlideDtoSimple.mapToDtoList(slides);
			return new ResponseEntity<>(slidesDtos, HttpStatus.OK);
		}

	}

	@Operation(summary = "Get a slide by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	Slides details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = SlideDto.class)) }),
			@ApiResponse(responseCode = "404", description = "	Slide not found", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", description = "	Internal Server Error") })
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getSlideById(
			@Parameter(description = "	Id of Slide to be searched") @PathVariable Long id)
			throws RecordNotExistException {

		Slide slide = slideService.getById(id);
		SlideDto slideDto = SlideDto.mapToDto(slide);
		return new ResponseEntity<>(slideDto, HttpStatus.OK);

	}

	@Operation(summary = "Update a slide by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	Slide details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = SlideDto.class)) }),
			@ApiResponse(responseCode = "404", description = "	Slide or Organization not found", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", description = "	Internal Server Error") })
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateSlide(@Parameter(description = "	Id of slide to be updated") @PathVariable Long id,
			@RequestBody SlideRequest sliderequest) throws RecordNotExistException {

		Slide slideToUpdate = slideService.getById(id);
		slideToUpdate = slideService.setSlideToUpdateOrCreate(slideToUpdate, sliderequest);
		slideToUpdate = slideService.update(slideToUpdate);
		SlideDto slideDto = SlideDto.mapToDto(slideToUpdate);
		return new ResponseEntity<>(slideDto, HttpStatus.OK);

	}

	@Operation(summary = "Delete a slide by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	Return a OK message", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "	Slide Not Found", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", description = "	Internal Server Error") })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteSlideById(
			@Parameter(description = "	Id of Slide to be deleted") @PathVariable Long id)
			throws RecordNotExistException {

		slideService.deleteById(id);
		return new ResponseEntity<>(new ResponseDto(200, "Has been deleted successfully.."), HttpStatus.OK);

	}

	@Operation(summary = "Create a new slide")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "	Return a OK message", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "	Return a error message", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "	Organization not found", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "500", description = "	Internal Server Error") })
	@PostMapping
	public ResponseEntity<?> createSlide(@RequestBody SlideRequest sliderequest) throws RecordNotExistException {

		if (sliderequest.getImageUrl() == null || sliderequest.getFkidOrganization() == null
				|| sliderequest.getText() == null) {
			return new ResponseEntity<>(new ResponseDto(400, "Attribute ImageUrl, FkidOrganization or Text are required"),
					HttpStatus.BAD_REQUEST);
		}
		Slide slide = new Slide();
		slide.setId(-1L);
		slide = slideService.setSlideToUpdateOrCreate(slide, sliderequest);
		slide = slideService.create(slide);
		return new ResponseEntity<>(new ResponseDto(200, "Id : " + slide.getId()), HttpStatus.OK);

	}

}
