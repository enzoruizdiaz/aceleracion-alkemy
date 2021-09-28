package ProjectAlkemy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ProjectAlkemy.controller.request.CommentRequest;
import ProjectAlkemy.exception.CommentNotAuthorizatedException;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Comment;
import ProjectAlkemy.model.User;
import ProjectAlkemy.repository.BaseRepository;
import ProjectAlkemy.repository.CommentRepository;
import ProjectAlkemy.service.imp.BaseServiceImpl;

@Service
public class CommentService extends BaseServiceImpl<Comment, Long>{

	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserService userService;
	@Autowired
	NewsService newsService;
	
	public CommentService(BaseRepository<Comment, Long> baseRepository) {
		super(baseRepository);
	}

	public List<Comment> getAllCommentsOrderedByCreation() throws ListNotFoundException{
		List<Comment> list = commentRepository.findAllBySoftDeleteFalseOrderByCreateAt();
		return Optional.ofNullable(list).orElseThrow(() -> new ListNotFoundException("no comment to list"));
	}
	
	public Comment setComment (CommentRequest cr) throws RecordNotExistException {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findByEmail(userDetails.getUsername()).orElseThrow(()->new RecordNotExistException("User has not been found..."));
		Comment c = new Comment();
		c.setNew_id(newsService.getById(cr.getFk_newId()));
		c.setUser_id(user);
		c.setBody(cr.getBody());
		c.setSoftDelete(false);
		return c;
	}

	public Comment updateComment(Long id, String body) throws RecordNotExistException,CommentNotAuthorizatedException {

		Comment comment=this.getById(id);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user= userService.findByEmail(userDetails.getUsername()).orElseThrow(()->new RecordNotExistException("User has not been found..."));

		if(!(user.getId().equals(comment.getUser_id().getId())) && !(user.getRoleId().getName().equals("ADMIN"))){
			throw new CommentNotAuthorizatedException("Does not have permissions to update...");
		}
		comment.setBody(body);
		Comment update=this.update(comment);

		return update;
	}

	public Comment deleteComment(Long id) throws RecordNotExistException,CommentNotAuthorizatedException {

		Comment comment=this.getById(id);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user= userService.findByEmail(userDetails.getUsername()).orElseThrow(()->new RecordNotExistException("User has not been found..."));

		if(!(user.getId().equals(comment.getUser_id().getId())) && !(user.getRoleId().getName().equals("ADMIN"))){
			throw new CommentNotAuthorizatedException("Does not have permissions to delete...");
		}
		comment.setSoftDelete(true);
		Comment delete= this.update(comment);

		return delete;
	}
}
