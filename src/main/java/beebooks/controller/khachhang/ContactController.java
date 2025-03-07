package beebooks.controller.khachhang;

import beebooks.entities.Contact;
import beebooks.service.ContactService;
import beebooks.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ContactController{
	
	private final ContactService contactService;
	private final MailService mailService;

	public ContactController(ContactService contactService, MailService mailService) {
		this.contactService = contactService;
		this.mailService = mailService;
	}


	@RequestMapping(value = { "/contact" }, method = RequestMethod.GET)
	public String contact(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {

		Contact contact = new Contact();
		model.addAttribute("contact", contact);
		return "khachhang/contact";
	}


	@RequestMapping(value = { "/contact" }, method = RequestMethod.POST)
	public String post_spring_form(final Model model, 
								   final HttpServletRequest request, 
								   final HttpServletResponse response,
								   final @ModelAttribute("contact") Contact contact) throws IllegalStateException, IOException{
		model.addAttribute("TB", "Cảm ơn " + contact.getName() + ", chúng tôi sẽ liên hệ với bạn trong thời gian sớm nhất!");
		model.addAttribute("contact", "");
		contactService.saveOrUpdate(contact);
		//gửi email thông báo
		String to = contact.getEmail();
		String subject = "XÁC NHẬN " + contact.getName() + " ĐÃ ĐĂNG KÝ LIÊN HỆ THÀNH CÔNG!";
		String text = "Chúng tôi sẽ liên hệ bạn trong thời gian sớm nhất. Xin cảm ơn"  + ".";
		mailService.sendEmail(to, subject, text);

		return "khachhang/contact";
	
	}

	@RequestMapping(value = { "/ajax/contact"}, method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> ajax_contact(final Model model, final HttpServletRequest request,
			final HttpServletResponse response, final @RequestBody Contact contact) {
		Map<String, Object> jsonResultCt = new HashMap<String, Object>();
		model.addAttribute("contact", "");

		List<Contact> contacts = contactService.checkEmailContact(contact);
		if(contacts.size() == 0) {
			contactService.saveOrUpdate(contact);
			jsonResultCt.put("code", 200);
			jsonResultCt.put("messages", "Cảm ơn, " + contact.getEmail() + " đã gửi liên hệ thành công cho Beebooks!");
		} else {
			jsonResultCt.put("err", "Bạn chưa nhập email / Trùng email");
		}
		return ResponseEntity.ok(jsonResultCt);
	}
}
