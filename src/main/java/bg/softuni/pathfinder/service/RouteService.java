package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.RouteDTO;
import bg.softuni.pathfinder.model.entity.RouteEntity;
import bg.softuni.pathfinder.reposityory.RouteRepo;
import bg.softuni.pathfinder.util.FileUtil;
import bg.softuni.pathfinder.config.GlobalConstants;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RouteRepo routesRepo;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;


    public RouteService(RouteRepo routesRepo, ModelMapper modelMapper, FileUtil fileUtil) {
        this.routesRepo = routesRepo;
        this.modelMapper = modelMapper;
        this.fileUtil = fileUtil;
    }

    public List<RouteDTO> getAllRoutes() {
        return routesRepo.findAll().stream()
                .map(route -> modelMapper.map(route, RouteDTO.class))
                .collect(Collectors.toList());
    }

    public RouteDTO getRouteById(Long id) {
        RouteEntity route = routesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        RouteDTO routeDto = modelMapper.map(route, RouteDTO.class);
        System.out.println("Level: " + routeDto.getLevel());

        return routeDto;

    }
    
    public String saveGpxFileAndGetPath(MultipartFile file) throws IOException {
        Files.createDirectories(Path.of(GlobalConstants.UPLOAD_DIR_GPS));
        String fileName = fileUtil.getUniqueName(file.getOriginalFilename());
        Path path = Path.of(GlobalConstants.UPLOAD_DIR_GPS, fileName);
        file.transferTo(path);
        return path.toString();
    }

    public void savePictureFile(MultipartFile file, Long routeId) throws IOException {
        Files.createDirectories(Path.of(GlobalConstants.UPLOAD_DIR_PICS));
        String fileName = routeId + "_" + file.getOriginalFilename();
        Path path = Path.of(GlobalConstants.UPLOAD_DIR_PICS, fileName);
        file.transferTo(path);
    }

    
        
   
}
