package ProjectAlkemy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ProjectAlkemy.controller.dto.CategoryDtoSimple;
import ProjectAlkemy.controller.request.CategoryRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Category;
import ProjectAlkemy.repository.BaseRepository;
import ProjectAlkemy.repository.CategoryRepository;
import ProjectAlkemy.service.imp.BaseServiceImpl;

@Service
public class CategoryService extends BaseServiceImpl<Category, Long> {

	@Autowired
	private CategoryRepository categoryRepository;

	public CategoryService(BaseRepository<Category, Long> baseRepository) {
		super(baseRepository);
	}

	public boolean exist(Long id) {
		return categoryRepository.existsById(id);
	}

	public Category softDelete(Category category) {
		category.setSoftDelete(true);
		Category deleted = this.update(category);
		return deleted;
	}

	public Map<String, Object> getAllPaginatedCategories(Integer page, Integer size)
			throws ListNotFoundException, RecordNotExistException {

		Pageable paging = PageRequest.of(page, size);
		Map<String, Object> response = new HashMap<>();
		String next;
		String previous;
		
		Page<Category> pageCat = categoryRepository.findAllBySoftDeleteFalseOrderByCreateAt(paging);// user
		Optional.ofNullable(pageCat).orElseThrow(() -> new ListNotFoundException("There was an error on the page..."));
		List<CategoryDtoSimple> categoryDtoList = CategoryDtoSimple.EntitiesToDtos(pageCat.getContent());
		Optional.ofNullable(categoryDtoList).orElseThrow(()-> new ListNotFoundException("There was an error on the page..."));
		
		response.put("Categories", categoryDtoList);

		if (page < pageCat.getTotalPages() - 1) {
			next = String.valueOf(page + 1);
		} else {
			next = null;
		}

		if (page > 0 && page < pageCat.getTotalPages()) {
			previous = String.valueOf(page - 1);
		} else {
			previous = null;
		}

		if (previous == null) {
			response.put("Previous page ", "No previous page found ...");
		} else {
			response.put("Previous page ", ServletUriComponentsBuilder.fromCurrentRequestUri()
					.replaceQueryParam("page", previous).replaceQueryParam("size", size).build().toUri());
		}

		if (next == null) {
			response.put("Next page ", "No next page found ...");
		} else {
			response.put("Next page ", ServletUriComponentsBuilder.fromCurrentRequestUri()
					.replaceQueryParam("page", next).replaceQueryParam("size", size).build().toUri());
		}

		return response;
	}

	public Category setCat(CategoryRequest catReq, Long id) throws RecordNotExistException {
		Category category = this.getById(id);
		if (category != null) {
			if (catReq.getName() != null) {
				category.setName(catReq.getName());
			}
			if (catReq.getDescription() != null) {
				category.setDescription(catReq.getDescription());
			}
			if (catReq.getImage() != null) {
				category.setImage(catReq.getImage());
			}
		}
		return category;
	}
}
