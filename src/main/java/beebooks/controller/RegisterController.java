package beebooks.controller;

import beebooks.entities.Role;
import beebooks.entities.User;
import beebooks.service.RoleService;
import beebooks.service.UserService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RegisterController extends BaseController{

    final UserService userService;
    private final RoleService roleService;

    public RegisterController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(value = { "/register" }, method = RequestMethod.GET)
    public String register(final Model model) throws IOException {
        model.addAttribute("user", new User());

        return "register";
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.POST)
    public String post_spring_form(HttpServletRequest request, HttpServletResponse response,
                                   final @ModelAttribute("user") User user) throws IllegalStateException{
        Map<String, Object> jsonResultCt = new HashMap<>();

        if (user.getUsername() == null){
            jsonResultCt.put("error","Tài khoản không được để trống!");
        } else if (user.getEmail() == null) {
            jsonResultCt.put("error","Email không được để trống!");
        } else if (user.getPhone() == null) {
            jsonResultCt.put("error","Số điện thoại không được để trống!");
        } else if (user.getPassword() == null) {
            jsonResultCt.put("error","Mật khẩu không được để trống!");
        } else if (user.getAddress() == null) {
            jsonResultCt.put("error","Địa chỉ không được để trống!");
        }
        List<User> usersMail = userService.checkEmailRegister(user);
        List<User> usersName = userService.checkUserNameRegister(user);
        if(usersMail.size() == 0){
            if(usersName.size() == 0){
                user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(4)));
                Role role = roleService.loadRoleByRoleName("GUEST");
                user.addRoles(role);
                userService.saveOrUpdate(user);
                jsonResultCt.put("success","Cảm ơn bạn đã đăng ký tài khoản thành công!");
            }
        } else {
            jsonResultCt.put("message","Tài khoản hoặc email của bạn đã được đăng ký!");
        }

        return "register";
    }


}
