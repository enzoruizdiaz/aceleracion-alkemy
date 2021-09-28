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

import ProjectAlkemy.controller.dto.MemberDto;
import ProjectAlkemy.controller.request.MemberRequest;
import ProjectAlkemy.exception.ListNotFoundException;
import ProjectAlkemy.exception.RecordNotExistException;
import ProjectAlkemy.model.Member;
import ProjectAlkemy.repository.BaseRepository;
import ProjectAlkemy.repository.MemberRepository;
import ProjectAlkemy.service.imp.BaseServiceImpl;

@Service
public class MemberService extends BaseServiceImpl<Member, Long> {

	@Autowired
	private MemberRepository memberRepo;

	public MemberService(BaseRepository<Member, Long> baseRepository) {
		super(baseRepository);
	}

	public Member deleteMember(Member member) {
		member.setSoftDelete(true);
		return this.update(member);
	}

	public Member setMember(MemberRequest membReq, Long id) throws RecordNotExistException {
		
		Member members = this.getById(id);

		if (members != null) {
			if (membReq.getName() != null) {
				members.setName(membReq.getName());
			}
			if (membReq.getDescription() != null) {
				members.setDescription(membReq.getDescription());
			}
			if (membReq.getImage() != null) {
				members.setImage(membReq.getImage());
			}
			if (membReq.getFacebookUrl() != null) {
				members.setFacebookUrl(membReq.getFacebookUrl());
			}
			if (membReq.getLinkedinUrl() != null) {
				members.setLinkedinUrl(membReq.getLinkedinUrl());
			}
			if (membReq.getInstagramUrl() != null) {
				members.setInstagramUrl(membReq.getInstagramUrl());
			}
		}
		return members;

	}

	public Map<String, Object> getAllMemberPaged(Integer page, Integer size) throws ListNotFoundException {

		Pageable paging = PageRequest.of(page, size);
		Page<Member> pageT = memberRepo.findAllBySoftDeleteFalseOrderByCreateAt(paging);

		Optional.ofNullable(pageT).orElseThrow(() -> new ListNotFoundException("There was an error on the page..."));

		List<Member> membersPage = pageT.getContent();
		Optional.ofNullable(membersPage).orElseThrow(() -> new ListNotFoundException("There was an error on the page..."));

		List<MemberDto> memberDtoList = MemberDto.entitiesToDtos(membersPage);

		Map<String, Object> response = new HashMap<>();
		response.put("Members", memberDtoList);

		String sig;
		String ante;

		if (page < pageT.getTotalPages() - 1) {
			sig = String.valueOf(page + 1);
		} else {
			sig = null;
		}
		;

		if (page > 0 && page < pageT.getTotalPages()) {
			ante = String.valueOf(page - 1);
		} else {
			ante = null;
		}

		if (ante == null)
			response.put("Previous page ", "No previous page found ...");
		else
			response.put("Previous page ", ServletUriComponentsBuilder.fromCurrentRequestUri()
					.replaceQueryParam("page", ante).replaceQueryParam("size", size).build().toUri());

		if (sig == null)
			response.put("Next page ", "No next page found ...");
		else
			response.put("Next page ", ServletUriComponentsBuilder.fromCurrentRequestUri()
					.replaceQueryParam("page", sig).replaceQueryParam("size", size).build().toUri());

		return response;
	}

}
