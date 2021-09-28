package ProjectAlkemy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ProjectAlkemy.controller.request.SlideRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Organization;
import ProjectAlkemy.model.Slide;
import ProjectAlkemy.repository.BaseRepository;
import ProjectAlkemy.repository.SlideRepository;
import ProjectAlkemy.service.imp.BaseServiceImpl;

@Service
public class SlideService extends BaseServiceImpl<Slide, Long> {

	@Autowired
	private SlideRepository slidesRepository;
	@Autowired
	private OrganizationService organizationService;

	public SlideService(BaseRepository<Slide, Long> baseRepository) {
		super(baseRepository);
	}

	public Integer getLastOrder() {
		Integer i = slidesRepository.getLastOrder();
		if (i == null) {
			return 0;
		} else {
			return i;
		}
	}

	public Slide getSlideByOrder(Integer i) {
		return slidesRepository.getSlideByOrder(i).orElse(null);
	}

	public List<Slide> getAllSlidesOrderer() throws ListNotFoundException {
		List<Slide> list = slidesRepository.getSlidesOrderer();
		return Optional.ofNullable(list).orElseThrow(() -> new ListNotFoundException("no slide to list"));
	}

	public List<Slide> slideOrderId(Long id) {
		return slidesRepository.slidesOrderId(id);
	}

	public Slide setSlideToUpdateOrCreate(Slide slide, SlideRequest slideRequest) throws RecordNotExistException {
		Slide slideAux = slide;
		if (slideRequest.getFkidOrganization() != null) {
			Organization orgaux = organizationService.getById(slideRequest.getFkidOrganization());
			slideAux.setOrganization(orgaux);
		}
		if (slideRequest.getOrder() < 1 || slideRequest.getOrder() == null) {
			slideAux.setOrder(1 + getLastOrder());
		} else {
			Slide Aux = getSlideByOrder(slideRequest.getOrder());
			if (Aux == null) {
				slideAux.setOrder(slideRequest.getOrder());
			} else {
				if (Aux.getId() != slide.getId()) {
					slideAux.setOrder(1 + getLastOrder());
				}
			}
		}

		if (slideRequest.getText() != null) {
			slideAux.setText(slideRequest.getText());
		}

		if (slideRequest.getImageUrl() != null) {
			slideAux.setImageUrl(slideRequest.getImageUrl());
		}
		return slideAux;
	}
}
