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

import ProjectAlkemy.controller.dto.TestimonyDto;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.model.Testimonials;
import ProjectAlkemy.repository.BaseRepository;
import ProjectAlkemy.repository.TestimonialsRespository;
import ProjectAlkemy.service.imp.BaseServiceImpl;

@Service
public class TestimonialsService extends BaseServiceImpl<Testimonials, Long> {

	@Autowired
	private TestimonialsRespository testimonialsRespository;

	public TestimonialsService(BaseRepository<Testimonials, Long> baseRepository) {
		super(baseRepository);
	}

	public Testimonials softDelete(Testimonials testimonials) {
		testimonials.setSoftDelete(true);
		return this.update(testimonials);
	}

	public Map<String,Object> getAllTestimony(Integer page,Integer size) throws ListNotFoundException {

		Pageable paging= PageRequest.of(page,size);
		Page<Testimonials> pageT = testimonialsRespository.findAllBySoftDeleteFalseOrderByCreateAt(paging);

		Optional.ofNullable(pageT).orElseThrow(()-> new ListNotFoundException("There was an error on the page..."));

		List<Testimonials> testimonialsPage= pageT.getContent();
		Optional.ofNullable(testimonialsPage).orElseThrow(()-> new ListNotFoundException("There was an error on the page..."));

		List<TestimonyDto> testimonyDtoList=TestimonyDto.mapToDtoList(testimonialsPage);

		Map<String,Object> response = new HashMap<>();
		response.put("Testimonials",testimonyDtoList);

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
	public Testimonials findByName(String name){
		return testimonialsRespository.findByName(name).orElse(null);
	}


}
