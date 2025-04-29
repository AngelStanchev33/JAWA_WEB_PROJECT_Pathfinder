package bg.softuni.pathfinder.config;

import bg.softuni.pathfinder.model.service.UserServiceModel;
import bg.softuni.pathfinder.model.entity.UserEntity;
import bg.softuni.pathfinder.model.entity.enums.LevelEnum;
import bg.softuni.pathfinder.model.dto.RouteDTO;
import bg.softuni.pathfinder.model.dto.UploadPictureDto;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import bg.softuni.pathfinder.model.entity.PictureEntity;
import bg.softuni.pathfinder.model.entity.RoleEntity;
import bg.softuni.pathfinder.model.entity.RouteEntity;
import bg.softuni.pathfinder.model.entity.enums.RoleEnum;
import bg.softuni.pathfinder.reposityory.RolesRepo;
import bg.softuni.pathfinder.reposityory.RoutesRepo;
import bg.softuni.pathfinder.reposityory.UserRepo;
import bg.softuni.pathfinder.util.GpxUtil;
import io.jenetics.jpx.WayPoint;

import java.io.IOException;
import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

@Configuration
public class ModelMapperConfig {

    private final PasswordEncoder passwordEncoder;
    private final RolesRepo rolesRepo;
    private final GpxUtil gpxUtil;
    private final UserRepo userRepo;
    private final RoutesRepo routesRepo;

    public ModelMapperConfig(PasswordEncoder passwordEncoder, RolesRepo rolesRepo, GpxUtil gpxUtil,
            UserRepo userRepo, RoutesRepo routesRepo) {
        this.passwordEncoder = passwordEncoder;
        this.rolesRepo = rolesRepo;
        this.gpxUtil = gpxUtil;
        this.userRepo = userRepo;
        this.routesRepo = routesRepo;
    }
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Конфигурация: игнорира неясни мапвания и пропуска null стойности
        modelMapper.getConfiguration()
                .setAmbiguityIgnored(true) // Ако едно property може да отиде на повече от едно място –
                                           // игнорира
                .setSkipNullEnabled(true); // Не презаписва с null, ако source има null стойност

        // Създава специфично мапване от UserServiceModel към UserEntity
        modelMapper.createTypeMap(UserServiceModel.class, UserEntity.class)
                .addMappings(mapper -> {
                    mapper.using(context -> passwordEncoder.encode((String) context.getSource()))
                            .map(UserServiceModel::getPassword, UserEntity::setPassword);
                    mapper.map(src -> LevelEnum.BEGINNER, UserEntity::setLevel);
                    mapper.map(src -> {
                        RoleEntity userRole = rolesRepo.findByName(RoleEnum.USER)
                                .orElseThrow();
                        return List.of(userRole);
                    }, UserEntity::setRoles);
                });

        Converter<RouteEntity, RouteDTO> routeConverter = new Converter<RouteEntity, RouteDTO>() {
            @Override
            public RouteDTO convert(MappingContext<RouteEntity, RouteDTO> context) {
                RouteEntity source = context.getSource();
                RouteDTO destination = new RouteDTO();
                
                // Това ще се изпълни само когато се вика мапването
                destination.setId(source.getId());
                destination.setName(source.getName());
                destination.setDescription(source.getDescription());
                destination.setDistance(gpxUtil.calculateDistance(source.getGpxCoordinates()));
                destination.setLevel(source.getLevel() != null ? source.getLevel().name() : null);
                destination.setPictures(source.getPictures());
                destination.setAuthor(source.getAuthor());
                destination.setVideoUrl(source.getVideoUrl());
                try {
                    WayPoint startPoint = gpxUtil.getStartWayPoint(source.getGpxCoordinates());
                    destination.setStart(startPoint);
                    
                    WayPoint endPoint = gpxUtil.getEndWayPoint(source.getGpxCoordinates());
                    destination.setEnd(endPoint);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to parse GPX data", e);
                }
            
                
                return destination;
            }
        };

        modelMapper.addConverter(routeConverter);

      
        Converter<UploadPictureDto, PictureEntity> uploadPictureConverter = new Converter<UploadPictureDto, PictureEntity>() {
            @Override
            public PictureEntity convert(MappingContext<UploadPictureDto, PictureEntity> context) {
                UploadPictureDto source = context.getSource();
                PictureEntity destination = new PictureEntity();

                if (source.getUrlAddress() == null || source.getUrlAddress().isEmpty()) {
                    destination.setUrl("/uploads/" + source.getRouteId() + "_" + source.getImage().getOriginalFilename());
                } else {
                    destination.setUrl(source.getUrlAddress());
                }
                
                destination.setTitle(source.getTitle());
                destination.setAuthor(userRepo.findById(source.getAuthorId()).orElseThrow());
                destination.setRoute(routesRepo.findById(source.getRouteId()).orElseThrow());

                return destination;
            }
        };

        modelMapper.addConverter(uploadPictureConverter);

        return modelMapper;
    }

}
