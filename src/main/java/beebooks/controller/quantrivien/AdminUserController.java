package beebooks.controller.quantrivien;

import beebooks.controller.BaseController;
import beebooks.dto.UserSearchModel;
import beebooks.entities.Role;
import beebooks.entities.User;
import beebooks.repository.RoleRepository;
import beebooks.repository.UserRepository;
import beebooks.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.Set;

@Controller
public class AdminUserController extends BaseController {

	private final UserService userService;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	public AdminUserController(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@RequestMapping(value = { "/admin/user/list","/admin/user" }, method = RequestMethod.GET)
	public String adminProductList(final Model model, final HttpServletRequest request) {
		
		UserSearchModel searchModel = new UserSearchModel();
		searchModel.keyword = request.getParameter("keyword");
		searchModel.setPage(getCurrentPage(request));
		searchModel.roleId = super.getInteger(request, "roleId");
		
		model.addAttribute("userWithPaging", userService.search(searchModel));
		model.addAttribute("searchModel", searchModel);
		
		return "quantrivien/user";
	}

	@GetMapping("/delete-user/{id}")
	public String deleteUser(@PathVariable("id") Integer id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Set<Role> roles = user.getRoles();
			boolean hasUserRole = roles.stream().anyMatch(role -> role.getName().equals("GUEST"));
			boolean hasAdminRole = roles.stream().anyMatch(role -> role.getName().equals("ADMIN"));

			if (hasUserRole && !hasAdminRole) { // chỉ xóa nếu có vai trò 'user' và không có vai trò 'admin'
				userRepository.deleteById(id);
			}
		}
		return "redirect:/admin/user";
	}


}
