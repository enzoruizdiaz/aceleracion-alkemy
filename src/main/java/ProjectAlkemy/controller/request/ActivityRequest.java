package ProjectAlkemy.controller.request;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import ProjectAlkemy.model.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequest {

	private String name;
	private String content;
	private String image;

	@Autowired
	private static ModelMapper mapper = new ModelMapper();

	
	
	public static ActivityRequest mapToRequest(Activity a) {
		ActivityRequest actReq = mapper.map(a, ActivityRequest.class);

		return actReq;
	}
	
	public static Activity mapToEntity(ActivityRequest actReq) {
		Activity act = mapper.map(actReq, Activity.class);
		return act;
	}
	public static List<ActivityRequest> mapToReqList(List<Activity> actList) {
		List<ActivityRequest> acts = new ArrayList<ActivityRequest>();
		for (Activity a : actList) {
			acts.add(mapToRequest(a));
		}
		return acts;
	}


}
