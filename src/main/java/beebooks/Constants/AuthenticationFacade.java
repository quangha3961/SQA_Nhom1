package beebooks.Constants;

import beebooks.entities.User;
import beebooks.repository.UserRepository;
import beebooks.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthenticationFacade(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public Integer getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        // Lấy username của người đăng nhập
        String loggedInUsername = authentication.getName();
        // Tìm ID của người đăng nhập từ service hoặc repository
        User loggedInUser = userRepository.findByUsername(loggedInUsername);
        if (loggedInUser == null) {
            return null;
        }
        return loggedInUser.getId();
    }
}

