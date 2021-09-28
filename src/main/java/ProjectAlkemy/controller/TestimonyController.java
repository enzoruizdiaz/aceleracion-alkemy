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

import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.dto.TestimonyDto;
import ProjectAlkemy.controller.request.TestimonyRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Testimonials;
import ProjectAlkemy.service.TestimonialsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/testimonials")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Testimonial Controller", description = "All Api Rest methods to manage testimonial")
public class TestimonyController {

    private TestimonialsService testimonialsService;

    @Autowired
    public TestimonyController(TestimonialsService testimonialsService) {
        this.testimonialsService = testimonialsService;
    }

    @Operation(summary = "Create a new Testimony")
	@ApiResponses(value = {
            	@ApiResponse(responseCode = "200", description = "    Show created URI ", content = {
            	@Content(mediaType = "application/json") }),
            	@ApiResponse(responseCode = "400", description = "    Name and content are required", content = {
                @Content(mediaType = "application/json") }),
            	@ApiResponse(responseCode = "500", description = "    Internal Server Error")})
    @PostMapping
    public ResponseEntity<?> createTestimony(@RequestBody TestimonyRequest testimonyRequest) {

        ResponseEntity<?> responseEntity;

        if(testimonyRequest.getName()!=null&&testimonyRequest.getContent()!=null){
            Testimonials created = testimonialsService.create(TestimonyRequest.mapToEntity(testimonyRequest));
            responseEntity = new ResponseEntity<>(new ResponseDto(200,"Id: " + created.getId()),HttpStatus.OK);
        }else {
            responseEntity = new ResponseEntity<>(new ResponseDto(400,"Name and content are required.."),HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @Operation(summary = "Update a Testimony by the id supplied")
	@ApiResponses(value = {
            	@ApiResponse(responseCode = "200", description = "    Show id of a Testimony updated", content = {
            	@Content(mediaType = "application/json", schema = @Schema(implementation = TestimonyDto.class)) }),
            	@ApiResponse(responseCode = "404", description = "    Id does not exist", content = {
            	@Content(mediaType = "application/json") }),
            	@ApiResponse(responseCode = "500", description = "    Internal Server Error")})
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTestimony(@Parameter(description = "	Id of Testimony")@PathVariable("id") Long id,@Parameter(description = "	All attributes to update a Testimony")@RequestBody TestimonyRequest testimonyRequest) throws RecordNotExistException {

        ResponseEntity<?> responseEntity;
        Testimonials testimonials;

        testimonials = testimonialsService.getById(id);
        if (testimonials != null) {
                   			if (testimonyRequest.getName() != null) {
                                testimonials.setName(testimonyRequest.getName());
                    			}
                			if (testimonyRequest.getContent() != null) {
                                testimonials.setContent(testimonyRequest.getContent());
                    			}
                			if (testimonyRequest.getImage() != null) {
                                testimonials.setImage(testimonyRequest.getImage());
                    			} }

        testimonials = testimonialsService.update(testimonials);
        responseEntity = new ResponseEntity<>(TestimonyDto.mapToDto(testimonials),HttpStatus.OK);

        return responseEntity;
    }

    @Operation(summary = "Delete a Testimony by the id supplied")
	@ApiResponses(value = {
            	@ApiResponse(responseCode = "200", description = "    Show id of a Testimony deleted", content = {
            	@Content(mediaType = "application/json") }),
            	@ApiResponse(responseCode = "404", description = "    Id does not exist.", content = {
                @Content(mediaType = "application/json") }),
            	@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeTestimony(@Parameter(description = "	Id of Testimony to be deleted") @PathVariable("id") Long id) throws RecordNotExistException {

        Testimonials testimonials;
        ResponseEntity<?> responseEntity;

        testimonials = testimonialsService.getById(id);
        testimonials = testimonialsService.softDelete(testimonials);
        responseEntity = new ResponseEntity<>(new ResponseDto(200,"Has been deleted successfully.."),HttpStatus.OK);

        return responseEntity;
    }

    @Operation(summary = "Get testimonial pages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "    Show testimonials page..", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "    List not found..", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "    Internal Server Error..") })
    @GetMapping("/paged")
    public ResponseEntity<?> getTestimonials(@Parameter(description = "	Selected page")@RequestParam(defaultValue = "0")Integer page,@Parameter(description = "	Selected size page")@RequestParam(defaultValue = "10")Integer size) throws ListNotFoundException {

        ResponseEntity<?> responseEntity;
        Map<String,Object> response= null;

        response = testimonialsService.getAllTestimony(page,size);
        responseEntity=new ResponseEntity<>(response,HttpStatus.OK);

        return responseEntity;
    }

    @Operation(summary = "Get a testimony by the id supplied")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "	Return a OK message...", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TestimonyDto.class)) }),
            @ApiResponse(responseCode = "404", description = "	Testimony not found...", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "	Internal Server Error") })
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdTestimony(@Parameter(description = "	Id of testimony to search") @PathVariable("id")Long id) throws RecordNotExistException{
        Testimonials found = testimonialsService.getById(id);
        TestimonyDto auxDto = TestimonyDto.mapToDto(found);
        return new ResponseEntity<>(auxDto,HttpStatus.OK);
    }

}
