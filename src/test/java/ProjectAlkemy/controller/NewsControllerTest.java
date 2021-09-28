package ProjectAlkemy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ProjectAlkemy.controller.dto.CommentDtoSimple;
import ProjectAlkemy.controller.dto.NewsDto;
import ProjectAlkemy.controller.dto.ResponseDto;
import ProjectAlkemy.controller.request.NewRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Category;
import ProjectAlkemy.model.Comment;
import ProjectAlkemy.model.News;
import ProjectAlkemy.service.NewsService;

class NewsControllerTest {

	private NewsService newsService;
	private NewsController newsController;
	private Category cat;
	private News news;
	private List<News> newsList = new ArrayList<>();
	private List<Comment> commentList = new ArrayList<>();
	private NewRequest newReq, newReqNull;
	private NewsDto newsDto;
	private Map<String,Object> response;
	 
	@BeforeEach
	void setUp() {
		newsService = Mockito.mock(NewsService.class);
		newsController = new NewsController(newsService);
		cat = new Category(1L, "Noticias Falsas", "descripcion", "amazon.url", false, new ArrayList<>());
		news = new News(1L,"Titulo Noticia","Cuerpo de la noticia","amazon.url",false,cat,new ArrayList<>());
		newsList.add(news);
		newReq = new NewRequest("Titulo Noticia","Cuerpo de la noticia","amazon.url",1L);
		newReqNull = new NewRequest();
		newsDto = NewsDto.mapToDto(news);
	}
 
	@Test
	void createNewsOk() throws RecordNotExistException {
		when(newsService.setNew(new News(),newReq)).thenReturn(news);
		when(newsService.create(news)).thenReturn(news);
		ResponseEntity<?> ok = newsController.createNews(newReq);
		assertEquals(ok,new ResponseEntity<>(new ResponseDto(200, "Id: " + news.getId()), HttpStatus.OK));
		verify(newsService,times(1)).setNew(new News(),newReq);
		verify(newsService,times(1)).create(news);
	}
	
	@Test
	void createNewsBadRequest() throws RecordNotExistException {
		ResponseEntity<?> ok = newsController.createNews(newReqNull);
		assertEquals(ok,new ResponseEntity<>(new ResponseDto(400, "All atributes are required"), HttpStatus.BAD_REQUEST));
	}
	
	@Test
	void createNewsException() throws RecordNotExistException {
		when(newsService.setNew(new News(), newReq)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class,()-> newsController.createNews(newReq));
		verify(newsService,times(1)).setNew(new News(), newReq);
	}
 
	@Test
	void newsByIdOk() throws RecordNotExistException {
		when(newsService.getById(1L)).thenReturn(news);
		ResponseEntity<?> ok = newsController.newsById(1L);
		assertEquals(ok,new ResponseEntity<>(newsDto, HttpStatus.OK));
		verify(newsService,times(1)).getById(1L);
	} 
	@Test
	void newsByIdException() throws RecordNotExistException {
		when(newsService.getById(1L)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class,()-> newsController.newsById(1L));
		verify(newsService,times(1)).getById(1L);
	}


	@Test
	void updateNewsOk() throws RecordNotExistException {
		when(newsService.getById(1L)).thenReturn(news);
		when(newsService.setNew(news, newReq)).thenReturn(news);
		when(newsService.update(news)).thenReturn(news);
		ResponseEntity<?> ok = newsController.updateNews(1L,newReq);
		assertEquals(ok,new ResponseEntity<>(newsDto, HttpStatus.OK));
		verify(newsService,times(1)).getById(1L);
		verify(newsService,times(1)).setNew(news, newReq);
		verify(newsService,times(1)).update(news);
	}
	
	@Test
	void updateNewsExceptionGetById() throws RecordNotExistException {
		when(newsService.getById(1L)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class,()-> newsController.updateNews(1L,newReq));
		verify(newsService,times(1)).getById(1L);
	} 
	
	@Test
	void updateNewsExceptionSetNew() throws RecordNotExistException {
		when(newsService.getById(1L)).thenReturn(news);
		when(newsService.setNew(news, newReq)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class,()-> newsController.updateNews(1L, newReq));
		verify(newsService,times(1)).setNew(news, newReq);
	}
	
	@Test
	void deleteNewOk() throws RecordNotExistException {
		when(newsService.getById(1L)).thenReturn(news);
		when(newsService.softDelete(news)).thenReturn(news);
		ResponseEntity<?> ok = newsController.deleteNew(1L);
		assertEquals(ok,new ResponseEntity<>(new ResponseDto(200,"Has been successfully deleted."), HttpStatus.OK));
		verify(newsService,times(1)).getById(1L);
		verify(newsService,times(1)).softDelete(news);
	}
	
	@Test
	void deleteNewException() throws RecordNotExistException {
		when(newsService.getById(1L)).thenThrow(new RecordNotExistException("record not exist"));
		assertThrows(RecordNotExistException.class,()-> newsController.deleteNew(1L));
		verify(newsService,times(1)).getById(1L);
	}

	@Test
	void getNewsByIdOk() throws ListNotFoundException, RecordNotExistException {
		when(newsService.getComments(1L)).thenReturn(commentList);
		ResponseEntity<?> ok = newsController.getNewsById(1L);
		assertEquals(ok,new ResponseEntity<>(CommentDtoSimple.mapToListDto(commentList), HttpStatus.OK));
		verify(newsService,times(1)).getComments(1L);
	}
	
	@Test
	void getNewsByIdListException() throws RecordNotExistException, ListNotFoundException {
		when(newsService.getComments(1L)).thenThrow(new ListNotFoundException("Nothing to list"));
		assertThrows(ListNotFoundException.class,()-> newsController.getNewsById(1L));
		verify(newsService,times(1)).getComments(1L);
	}
	
	@Test
	void getNewsByIdRecordNotExistException() throws RecordNotExistException, ListNotFoundException {
		when(newsService.getComments(1L)).thenThrow(new RecordNotExistException("Nothing to list"));
		assertThrows(RecordNotExistException.class,()-> newsController.getNewsById(1L));
		verify(newsService,times(1)).getComments(1L);
	}

	@Test
	void paginasOk() throws ListNotFoundException {
		when(newsService.getAllNewsPaged(1, 0)).thenReturn(response);
		ResponseEntity<?> ok = newsController.paginas(1, 0);
		assertEquals(ok,new ResponseEntity<>(response,HttpStatus.OK));
		verify(newsService,times(1)).getAllNewsPaged(1, 0);
	}
	
	@Test
	void paginasException() throws ListNotFoundException {
		when(newsService.getAllNewsPaged(1, 0)).thenThrow(new ListNotFoundException("Nothing to list"));
		assertThrows(ListNotFoundException.class,()-> newsController.paginas(1, 0));
		verify(newsService,times(1)).getAllNewsPaged(1, 0);
	}
	
}