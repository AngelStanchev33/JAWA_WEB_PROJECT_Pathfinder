package bg.softuni.pathfinder.web;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import bg.softuni.pathfinder.model.dto.PIcturesDTO;
import bg.softuni.pathfinder.service.PictureService;

@Controller
public class HomeController {
    private final PictureService pictureService;

    public HomeController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<PIcturesDTO> pIcturesDTO = pictureService.getAllPictures();
        model.addAttribute("pictures", pIcturesDTO);
        return "index";
    }
}
