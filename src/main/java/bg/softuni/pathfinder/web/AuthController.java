package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.model.binding.LoginBindingModel;
import bg.softuni.pathfinder.model.binding.RegisterBindingModel;
import bg.softuni.pathfinder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.FieldError;

@Controller
@RequestMapping("/users")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {

        if (!model.containsAttribute("userLoginDTO")) {
            model.addAttribute("userLoginDTO", new LoginBindingModel());
        }

        if (!model.containsAttribute("badCredentials")) {
            model.addAttribute("badCredentials", false);
        }

        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {

        if (!model.containsAttribute("registerBindingModel")) {
            model.addAttribute("registerBindingModel", new RegisterBindingModel());
        }
        return "register";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userLoginDTO") LoginBindingModel loginBindingModel, 
                       RedirectAttributes redirectAttributes) {
        boolean isLogged = userService.loginUser(loginBindingModel);
        
        if (!isLogged) {
            redirectAttributes.addFlashAttribute("badCredentials", true);
            redirectAttributes.addFlashAttribute("userLoginDTO", loginBindingModel);
            return "redirect:/users/login";
        }
        
        return "redirect:/";
    }

    @PostMapping("/register")
    public String registerConfirm(@ModelAttribute RegisterBindingModel registerBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Проверка за съвпадение на паролите
        if (!registerBindingModel.getPassword().equals(registerBindingModel.getConfirmPassword())) {
            bindingResult.addError(
                    new FieldError("registerBindingModel",
                            "confirmPassword",
                            "Passwords must match!"));
        }

        // Ако има грешки, връщаме обратно към формата
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerBindingModel", registerBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerBindingModel",
                    bindingResult);
            return "redirect:/users/register";
        }

        // Опит за регистрация
        if (!userService.registerUser(registerBindingModel)) {
            redirectAttributes.addFlashAttribute("userExists", true);
            return "redirect:/users/register";
        }

        return "redirect:/users/login";
    }

    @PostMapping("/logout")
    public String logout() {
        this.userService.logOutUser();
        return "redirect:/";
    }
}
