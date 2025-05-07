package bg.softuni.pathfinder.web;

import bg.softuni.pathfinder.model.binding.RouteAddBindingModel;
import bg.softuni.pathfinder.model.dto.CommentDto;
import bg.softuni.pathfinder.model.dto.RouteDTO;
import bg.softuni.pathfinder.model.dto.UploadPictureDto;
import bg.softuni.pathfinder.model.entity.CommentEntity;
import bg.softuni.pathfinder.model.entity.PictureEntity;
import bg.softuni.pathfinder.model.entity.RouteEntity;
import bg.softuni.pathfinder.reposityory.PictureRepo;
import bg.softuni.pathfinder.reposityory.RouteRepo;
import bg.softuni.pathfinder.reposityory.CommentRepo;
import bg.softuni.pathfinder.service.RouteService;
import io.jenetics.jpx.WayPoint;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/routes")
public class RouteController {
    private final RouteService routeService;
    private final ModelMapper mapper;
    private final PictureRepo picturesRepo;
    private final CommentRepo commentRepo;
    private final RouteRepo routeRepo;

    public RouteController(RouteService routeService, PictureRepo picturesRepo,
            ModelMapper mapper, CommentRepo commentRepo,
            bg.softuni.pathfinder.reposityory.RouteRepo routesRepo) {
        this.routeService = routeService;
        this.mapper = mapper;
        this.picturesRepo = picturesRepo;
        this.commentRepo = commentRepo;
        this.routeRepo = routesRepo;
    }

    @GetMapping("/")
    public String routes(Model model) {
        List<RouteDTO> routes = routeService.getAllRoutes();
        model.addAttribute("routes", routes);
        return "routes";
    }

    @GetMapping("/{id}")
    public String routeDetails(@PathVariable("id") Long id, Model model) {
        RouteDTO route = routeService.getRouteById(id);
        model.addAttribute("route", route);

        List<WayPoint> wayPoints = route.getAllPoints();
        List<double[]> coordinateList = wayPoints.stream()
            .map(wp -> new double[]{wp.getLatitude().doubleValue(), wp.getLongitude().doubleValue()})
            .collect(Collectors.toList());
        model.addAttribute("coordinates", coordinateList);

        if (!model.containsAttribute("commentDto")) {
            model.addAttribute("commentDto", new CommentDto());
        }

        return "route-details";
    }

    @GetMapping("/addRoute")
    public String addRoute(Model model) {
        if (!model.containsAttribute("routeAddBindingModel")) {
            model.addAttribute("routeAddBindingModel", new RouteAddBindingModel());
        }
        return "add-route";
    }

    @PostMapping("/{id}/upload")
    public String uploadPicture(@PathVariable("id") Long id, @ModelAttribute UploadPictureDto uploadPictureDto)
            throws IllegalStateException, IOException {

        if (uploadPictureDto.getUrlAddress() != null && !uploadPictureDto.getUrlAddress().trim().isEmpty()) {
            PictureEntity picture = mapper.map(uploadPictureDto, PictureEntity.class);
            picturesRepo.save(picture);
        } else if (uploadPictureDto.getImage() != null && !uploadPictureDto.getImage().isEmpty()) {
            routeService.savePictureFile(uploadPictureDto.getImage(), id);
            PictureEntity picture = mapper.map(uploadPictureDto, PictureEntity.class);
            picturesRepo.save(picture);
        }

        return "redirect:/routes/" + id;
    }

    @PostMapping("/{id}/comment")
    public String postComment(@PathVariable("id") Long id,
            @Valid @ModelAttribute("commentDto") CommentDto commentDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentDto", commentDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.commentDto",
                    bindingResult);
            return "redirect:/routes/" + id;
        }
        CommentEntity comment = mapper.map(commentDto, CommentEntity.class);
        comment.setRoute(mapper.map(routeService.getRouteById(id), RouteEntity.class));
        commentRepo.save(comment);

        return "redirect:/routes/" + id;
    }

    @PostMapping("/addRoute")
    public String addRoute(@Valid @ModelAttribute RouteAddBindingModel routeAddBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("routeAddBindingModel", routeAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.routeAddBindingModel",
                    bindingResult);
            return "redirect:/routes/addRoute";
        }

        RouteEntity route = mapper.map(routeAddBindingModel, RouteEntity.class);
        route.setGpxCoordinates(routeService.saveGpxFileAndGetPath(routeAddBindingModel.getGpxCoordinates()));
        routeRepo.save(route);

        return "redirect:/routes/" + route.getId();
    }

}
