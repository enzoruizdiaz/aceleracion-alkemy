package ProjectAlkemy.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
public class SengridConfig {

	// add your email sender in application properties
	@Value("${app.sendgrid.emailfrom}")
	private String emailfrom;

	// add your apikey in application properties
	@Value("${app.sendgrid.key}")
	private String sendGridApiKey;
	
	@Value("${app.sendgrid.contactTemplate}")
	private String contactTemplate;
	
	@Value("${app.sendgrid.registerTemplate}")
	private String registerTemplate;
	
	public void sendMail(String emailto, String template) throws IOException {
		Request request = new Request();
		SendGrid sg = new SendGrid(sendGridApiKey);
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			if (template.equals("REGISTER")) {
				request.setBody(setMail(emailto,registerTemplate).build());
			} else if (template.equals("CONTACT")) {
				request.setBody(setMail(emailto,contactTemplate).build());
			}
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}
	}
	
	private Mail setMail(String emailTo, String templateId) {
		Mail mail = new Mail();
		mail.setFrom(new Email(emailfrom));
		mail.setTemplateId(templateId);
		Personalization dinamic = new Personalization();
		dinamic.addTo(new Email(emailTo));
		mail.addPersonalization(dinamic);
		return mail;
	}
}
