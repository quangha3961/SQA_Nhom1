package beebooks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class LoginController extends BaseController {

    public LoginController() {
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login() throws IOException {
        return "login";
    }


    @GetMapping(value = "/logout")
    public String logout() {
        return "home";
    }

}
