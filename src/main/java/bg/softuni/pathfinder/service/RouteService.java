package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.RouteDTO;
import bg.softuni.pathfinder.model.entity.RouteEntity;
import bg.softuni.pathfinder.reposityory.RoutesRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final RoutesRepo routesRepo;
    private final ModelMapper modelMapper;

    public RouteService(RoutesRepo routesRepo, ModelMapper modelMapper) {
        this.routesRepo = routesRepo;
        this.modelMapper = modelMapper;
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

}
