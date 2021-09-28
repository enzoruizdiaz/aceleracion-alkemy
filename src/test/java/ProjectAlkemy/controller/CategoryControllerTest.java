package ProjectAlkemy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ProjectAlkemy.controller.dto.CategoryDto;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.CategoryRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Category;
import ProjectAlkemy.service.CategoryService;

class CategoryControllerTest {

	private CategoryService categoryService;
	private CategoryController categoryController;
	private Category category;
	private CategoryDto categoryDto;
	private CategoryRequest categoryRequest;
	private CategoryRequest categoryRequest2;
	private Map<String,Object> map;

	@BeforeEach
	void setUp() {
		categoryService = Mockito.mock(CategoryService.class);
		categoryController = new CategoryController(categoryService);
		category = new Category(1L, "name", "description", "image", false, null);
		categoryDto = CategoryDto.entityToDto(category);
		categoryRequest = new CategoryRequest("name", "description", "image");
		categoryRequest2= new CategoryRequest();
	}

	
	@Test
	void listCategories() throws ListNotFoundException, RecordNotExistException {
		when(categoryService.getAllPaginatedCategories(0, 10)).thenReturn(map);
    	ResponseEntity<?> ok = categoryController.listCategories(0, 10);
    	assertEquals(ok,new ResponseEntity<>(map,HttpStatus.OK));
    	verify(categoryService,times(1)).getAllPaginatedCategories(0, 10);
	}
	
	@Test 
	void listCategoriesListException () throws ListNotFoundException, RecordNotExistException {
		 when(categoryService.getAllPaginatedCategories(0, 10)).thenThrow(new ListNotFoundException("not Found"));

         assertThrows(ListNotFoundException.class, () -> categoryController.listCategories(0, 10));
         verify(categoryService,times(1)).getAllPaginatedCategories(0, 10);
	}
	@Test 
	void listCategoriesRecordException () throws ListNotFoundException, RecordNotExistException {
		 when(categoryService.getAllPaginatedCategories(0, 10)).thenThrow(new RecordNotExistException("not Found"));

         assertThrows(RecordNotExistException.class, () -> categoryController.listCategories(0, 10));
         verify(categoryService,times(1)).getAllPaginatedCategories(0, 10);
	}

	@Test
	void detailCategories() throws RecordNotExistException {
		when(categoryService.getById(1L)).thenReturn(category);
		ResponseEntity<?> ok = categoryController.detailCategories(1L);
		assertEquals(ok,(new ResponseEntity<>(categoryDto,HttpStatus.OK)));
		verify(categoryService,times(1)).getById(1L);
	}

	@Test 
	void createCategories() {
		MockedStatic<CategoryRequest> mb = Mockito.mockStatic(CategoryRequest.class);
		mb.when(() -> { CategoryRequest.mapToEntity(categoryRequest); }).thenReturn(category);
		when(categoryService.create(category)).thenReturn(category);
		ResponseEntity ok = categoryController.createCategories(categoryRequest);
		assertEquals(ok, new ResponseEntity<>(new ResponseDto(200, "Id :" + category.getId()), HttpStatus.OK));
		verify(categoryService, times(1)).create(category);
	}
	@Test
	void createCategoriesBadRequest() {
		ResponseEntity ok = categoryController.createCategories(categoryRequest2);
		assertEquals(ok, new ResponseEntity<>(new ResponseDto(400, "The field name is required"), HttpStatus.BAD_REQUEST));
	}

	@Test
	void updateCategories() throws RecordNotExistException {
		when(categoryService.setCat(categoryRequest, 1L)).thenReturn(category);
		when(categoryService.update(category)).thenReturn(category);
		ResponseEntity<?> ok = categoryController.updateCategories(1L, categoryRequest);
		assertEquals(ok,new ResponseEntity<>(categoryDto, HttpStatus.OK));
		verify(categoryService,times(1)).setCat(categoryRequest, 1L);
		verify(categoryService,times(1)).update(category);
	} 
 
	@Test
	void deleteCategoryOk() throws RecordNotExistException {
		when(categoryService.getById(1L)).thenReturn(category);
		ResponseEntity ok = categoryController.deleteCategory(1L);

		assertEquals(ok, new ResponseEntity<>(new ResponseDto(200, "Has been successfully deleted."), HttpStatus.OK));
		verify(categoryService, times(1)).getById(1l);

	}

	@Test
	void deleteCategoryException() throws RecordNotExistException {
		when(categoryService.getById(2L)).thenThrow(new RecordNotExistException("not Found"));
		assertThrows(RecordNotExistException.class, () -> categoryController.deleteCategory(2L));
	}
}