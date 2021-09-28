package ProjectAlkemy.controller;

import java.util.List;
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

import ProjectAlkemy.controller.dto.CommentDtoSimple;
import ProjectAlkemy.controller.dto.NewsDto;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.NewRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Comment;
import ProjectAlkemy.model.News;
import ProjectAlkemy.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/news")
@Tag(name = "News Controller", description = "All Api Rest methods to manage a News")
public class NewsController {

	private NewsService newsService;
	
	@Autowired
	public NewsController(NewsService newsService) {
		this.newsService = newsService;
	}

	@Operation(summary = "Create a new news ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show id of a new created", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "400", description = "    Malformed  BodyRequest "),
			@ApiResponse(responseCode = "404", description = "    New not found "),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })

	@PostMapping
	public ResponseEntity<?> createNews(@RequestBody NewRequest newRequest)
			throws RecordNotExistException {

		if (newRequest.getName() != null && !newRequest.getName().isEmpty() && newRequest.getContent() != null
				&& !newRequest.getContent().isEmpty() && newRequest.getImage() != null
				&& !newRequest.getImage().isEmpty() && newRequest.getIdCategory() != null
				&& newRequest.getIdCategory() > 0) {

			News news = newsService.setNew(new News(), newRequest);
			news.setSoftDelete(false);
			news = newsService.create(news);

			return new ResponseEntity<>(new ResponseDto(200, "Id: " + news.getId()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseDto(400, "All atributes are required"), HttpStatus.BAD_REQUEST);
		}

	}

	@Operation(summary = "Get a news by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    News details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = NewsDto.class)) }),
			@ApiResponse(responseCode = "404", description = "    New not found "),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })

	@GetMapping("/{id}")
	public ResponseEntity<?> newsById(@Parameter(description = "	Id of News to view")@PathVariable("id") Long id) throws RecordNotExistException {

		News news = newsService.getById(id);
		NewsDto newsDto = NewsDto.mapToDto(news);
		return new ResponseEntity<>(newsDto, HttpStatus.OK);

	}

	@Operation(summary = "Update a news by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "   The news was updated ", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = NewsDto.class)) }),
			@ApiResponse(responseCode = "404", description = "   New not found "),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })

	@PutMapping("/{id}")
	public ResponseEntity<?> updateNews(@Parameter(description = "	Id of News to view")@PathVariable("id") Long id, @RequestBody NewRequest newRequest) throws RecordNotExistException {

		News news = newsService.getById(id);
		news = newsService.setNew(news, newRequest);
		news = newsService.update(news);
		NewsDto newsDto = NewsDto.mapToDto(news);
		return new ResponseEntity<>(newsDto, HttpStatus.OK);

	}

	@Operation(summary = "Delete a news by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "   The news was delete", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "   New not found "),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteNew(@PathVariable("id") Long id)
			throws RecordNotExistException{

		News news = newsService.getById(id);
		news = newsService.softDelete(news);

		return new ResponseEntity<>(new ResponseDto(200,"Has been successfully deleted."), HttpStatus.OK);

	}

	@Operation(summary = "Return all comments on a news item")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "   Comment list", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = CommentDtoSimple.class)) }),
			@ApiResponse(responseCode = "404", description = "   New not found "),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@GetMapping("/{id}/comments")
	public ResponseEntity<?> getNewsById(@PathVariable("id") Long id)
			throws ListNotFoundException, RecordNotExistException {

		List<Comment> comments = newsService.getComments(id);
		return new ResponseEntity<>(CommentDtoSimple.mapToListDto(comments), HttpStatus.OK);

	}

	@Operation(summary = "Get all news in data base")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "   The news was updated ", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)) }),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@GetMapping
	public ResponseEntity<?> paginas(
			@Parameter(description= "	Selected page")@RequestParam(defaultValue = "0") int page,
			@Parameter(description = "	Selected size page")@RequestParam(defaultValue = "10") int size) throws ListNotFoundException {
		Map<String,Object> response = newsService.getAllNewsPaged(page, size);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
