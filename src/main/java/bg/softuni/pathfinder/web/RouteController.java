package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.model.dto.RouteDTO;
import bg.softuni.pathfinder.model.dto.UploadPictureDto;
import bg.softuni.pathfinder.model.entity.PictureEntity;
import bg.softuni.pathfinder.reposityory.PicturesRepo;
import bg.softuni.pathfinder.service.RouteService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/routes")
public class RouteController {
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    private final RouteService routeService;
    private final ModelMapper mapper;
    private final PicturesRepo picturesRepo;

    public RouteController(RouteService routeService, PicturesRepo picturesRepo, ModelMapper mapper) {
        this.routeService = routeService;
        this.mapper = mapper;
        this.picturesRepo = picturesRepo;
    }

    @GetMapping("/")
    public String routes(Model model) {
        List<RouteDTO> routes = routeService.getAllRoutes();
        model.addAttribute("routes", routes);
        return "routes";
    }

    @GetMapping("/details/{id}")
    public String routeDetails(@PathVariable("id") Long id, Model model) {
        RouteDTO route = routeService.getRouteById(id);
        model.addAttribute("route", route);
        return "route-details";
    }

    @PostMapping("/details/{id}/upload")
    public String uploadPicture(@PathVariable("id") Long id, @ModelAttribute UploadPictureDto uploadPictureDto)
            throws IllegalStateException, IOException {
        
        if (uploadPictureDto.getUrlAddress() != null && !uploadPictureDto.getUrlAddress().trim().isEmpty()) {
            PictureEntity picture = mapper.map(uploadPictureDto, PictureEntity.class);
            picturesRepo.save(picture);
        } 
        
        else if (uploadPictureDto.getImage() != null && !uploadPictureDto.getImage().isEmpty()) {
            MultipartFile file = uploadPictureDto.getImage();
            Files.createDirectories(Path.of(UPLOAD_DIR));
            String fileName = id + "_" + file.getOriginalFilename();
            Path path = Path.of(UPLOAD_DIR, fileName);
            file.transferTo(path);

            PictureEntity picture = mapper.map(uploadPictureDto, PictureEntity.class);
            picturesRepo.save(picture);
        }

        return "redirect:/routes/details/" + id;
    }

}
