package ProjectAlkemy.controller.request;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequest {

	private String name;
	private String facebookUrl;
	private String instagramUrl;
	private String linkedinUrl;
	private String image;
	private String description;
	
	
	@Autowired
	private static ModelMapper mapper = new ModelMapper();

	public static MemberRequest mapToRequest(Member m) {
		MemberRequest memReq = mapper.map(m, MemberRequest.class);
		return memReq;
	}
	
	public static Member mapToEntity(MemberRequest memReq) {
		Member m = mapper.map(memReq, Member.class);
		return m;
	}
	
	public static List<MemberRequest> mapToReqList(List<Member> membList) {
		List<MemberRequest> membs= new ArrayList<MemberRequest>();
		for (Member m : membList) {
			membs.add(mapToRequest(m));
		}
		return membs;
	}
}
