package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.model.dto.UserProfileDTO;
import bg.softuni.pathfinder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable("id") Long id, Model model) {
        UserProfileDTO user = userService.getProfileData(id);
        model.addAttribute("user", user);
        return "profile";
    }
} 