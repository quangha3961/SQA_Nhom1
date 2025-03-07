package beebooks.controller.khachhang;

import beebooks.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IntroductionController extends BaseController {
	
	@RequestMapping(value = { "/introduction" }, method = RequestMethod.GET)
	public String introView() {
		
		return "khachhang/introduction";
	}

}
