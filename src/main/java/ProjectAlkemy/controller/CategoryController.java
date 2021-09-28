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

import ProjectAlkemy.controller.dto.CategoryDto;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.CategoryRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Category;
import ProjectAlkemy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/categories")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category Controller", description = "All Api Rest methods to manage a Categories")
public class CategoryController {

	private CategoryService categoryService;

	@Autowired
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;

	}

	@Operation(summary = "Get a list of all Categories ")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Return empty Category or a list of Category", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)) }),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@GetMapping
	public ResponseEntity<?> listCategories(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size) throws RecordNotExistException, ListNotFoundException {
		Map<String, Object> response = null;
		response = categoryService.getAllPaginatedCategories(page, size);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Operation(summary = "Get a Category by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Category details", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class)) }),
			@ApiResponse(responseCode = "404", description = "    Category not found"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@GetMapping("/{id}")
	public ResponseEntity<?> detailCategories(
			@Parameter(description = "	Id of Category to view") @PathVariable("id") Long id)
			throws RecordNotExistException {
		Category category = categoryService.getById(id);
		CategoryDto cateDto = CategoryDto.entityToDto(category);
		return new ResponseEntity<>(cateDto, HttpStatus.OK);
	}

	@Operation(summary = "Create a new Category")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show Id of a Category created", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "    Category not found"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PostMapping
	public ResponseEntity<?> createCategories(@RequestBody CategoryRequest catRequest) { 
		if (catRequest.getName() != null && catRequest.getName() != "") {
			Category category = CategoryRequest.mapToEntity(catRequest);
			category.setSoftDelete(false);
			category = categoryService.create(category);
			return new ResponseEntity<>(new ResponseDto(200, "Id :" + category.getId()), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseDto(400, "The field name is required"), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Update a Category by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show Id of a Category updated", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class)) }),
			@ApiResponse(responseCode = "404", description = "    Category not found"),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error") })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCategories(
			@Parameter(description = "	Id of Category to be updated") @PathVariable("id") Long id,
			@RequestBody CategoryRequest catRequest) throws RecordNotExistException {
		Category update = categoryService.setCat(catRequest, id);
		update = categoryService.update(update);
		CategoryDto auxDto = CategoryDto.entityToDto(update);
		return new ResponseEntity<>(auxDto, HttpStatus.OK);
	}

	@Operation(summary = "Delete a Category by the id supplied")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "    Show Id of a Category deleted.", content = {
					@Content(mediaType = "application/json") }),
			@ApiResponse(responseCode = "404", description = "    Id does not exist."),
			@ApiResponse(responseCode = "500", description = "    Internal Server Error.") })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCategory(
			@Parameter(description = "	Id of Category to be deleted") @PathVariable("id") Long id)
			throws RecordNotExistException {
		Category cat = categoryService.getById(id);
		cat = categoryService.softDelete(cat);
		return new ResponseEntity<>(new ResponseDto(200, "Has been successfully deleted."), HttpStatus.OK);
	}

}
