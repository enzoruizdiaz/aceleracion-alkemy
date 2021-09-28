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

import ProjectAlkemy.controller.dto.NewsDto;
import ProjectAlkemy.controller.request.NewRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Category;
import ProjectAlkemy.model.Comment;
import ProjectAlkemy.model.News;
import ProjectAlkemy.repository.BaseRepository;
import ProjectAlkemy.repository.CommentRepository;
import ProjectAlkemy.repository.NewsRepository;
import ProjectAlkemy.service.imp.BaseServiceImpl;

@Service
public class NewsService extends BaseServiceImpl<News, Long> {

	@Autowired
	private NewsRepository newsRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private CategoryService categoryService;

	public NewsService(BaseRepository<News, Long> baseRepository) {
		super(baseRepository);
	}
	
	public News softDelete(News news) {
		news.setSoftDelete(true);
		return this.update(news);
		
	}

    public List<Comment> getComments(Long id) throws ListNotFoundException, RecordNotExistException {
		News news= this.getById(id);
		List<Comment> list = commentRepository.findAllComment(news.getId());
		return Optional.ofNullable(list).orElseThrow(()->new ListNotFoundException("List not found.."));
    }

	public  News setNew(News news, NewRequest newReq) throws RecordNotExistException {
		Category category = categoryService.getById(newReq.getIdCategory());

		if (newReq.getIdCategory() != null) {
			news.setCategoryId(category);
		}
		if (newReq.getName() != null) {
			news.setName(newReq.getName());
		}
		if (newReq.getImage() != null) {
			news.setImage(newReq.getImage());
		}
		if (newReq.getContent() != null) {
			news.setContent(newReq.getContent());
		}

		return news;

	}

	public Map<String,Object> getAllNewsPaged(Integer page,Integer size) throws ListNotFoundException {

		Pageable paging= PageRequest.of(page,size);
		Page<News> pageT=newsRepository.findAllBySoftDeleteFalseOrderByCreateAt(paging);

		Optional.ofNullable(pageT).orElseThrow(()-> new ListNotFoundException("There was an error on the page..."));

		List<News> newsPage= pageT.getContent();
		Optional.ofNullable(newsPage).orElseThrow(()-> new ListNotFoundException("There was an error on the page..."));

		List<NewsDto> newsDtoList=NewsDto.mapEntityPageIntoDtoPage(newsPage);

		Map<String,Object> response = new HashMap<>();
		response.put("News",newsDtoList);

		String sig;
		String ante;

		if (page<pageT.getTotalPages()-1){
			sig= String.valueOf(page+1);
		}else{
			sig=null;
		};

		if(page>0&&page<pageT.getTotalPages()){
			ante= String.valueOf(page-1);
		}else{
			ante=null;
		}

		if(ante==null) response.put("Previous page ","No previous page found ...");
		else response.put("Previous page ",ServletUriComponentsBuilder.fromCurrentRequestUri().replaceQueryParam("page",ante).replaceQueryParam("size",size).build().toUri());

		if(sig==null)response.put("Next page ","No next page found ...");
		else response.put("Next page ",ServletUriComponentsBuilder.fromCurrentRequestUri().replaceQueryParam("page",sig).replaceQueryParam("size",size).build().toUri());

		return response;
	}
}
