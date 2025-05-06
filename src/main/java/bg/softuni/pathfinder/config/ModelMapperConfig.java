package bg.softuni.pathfinder.config;

import bg.softuni.pathfinder.model.service.UserServiceModel;
import bg.softuni.pathfinder.model.entity.UserEntity;
import bg.softuni.pathfinder.model.entity.enums.LevelEnum;
import bg.softuni.pathfinder.model.binding.RouteAddBindingModel;
import bg.softuni.pathfinder.model.dto.CommentDto;
import bg.softuni.pathfinder.model.dto.RouteDTO;
import bg.softuni.pathfinder.model.dto.UploadPictureDto;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import bg.softuni.pathfinder.model.entity.CommentEntity;
import bg.softuni.pathfinder.model.entity.PictureEntity;
import bg.softuni.pathfinder.model.entity.RoleEntity;
import bg.softuni.pathfinder.model.entity.RouteEntity;
import bg.softuni.pathfinder.model.entity.enums.RoleEnum;
import bg.softuni.pathfinder.reposityory.RoleRepo;
import bg.softuni.pathfinder.reposityory.RouteRepo;
import bg.softuni.pathfinder.reposityory.UserRepo;
import bg.softuni.pathfinder.session.CurrentUser;
import bg.softuni.pathfinder.util.GpxUtil;
import io.jenetics.jpx.WayPoint;
import bg.softuni.pathfinder.util.FileUtil;
import bg.softuni.pathfinder.reposityory.CategoryRepo;
import bg.softuni.pathfinder.model.entity.CategoryEntities;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import java.time.LocalDateTime;

@Configuration
public class ModelMapperConfig {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepo rolesRepo;
    private final GpxUtil gpxUtil;
    private final UserRepo userRepo;
    private final RouteRepo routesRepo;
    private final CurrentUser currentUser;
    private final FileUtil fileUtil;
    private final CategoryRepo categoryRepo;

    public ModelMapperConfig(PasswordEncoder passwordEncoder, RoleRepo rolesRepo, GpxUtil gpxUtil,
            UserRepo userRepo, RouteRepo routesRepo, CurrentUser currentUser, FileUtil fileUtil,
            CategoryRepo categoryRepo) {
        this.passwordEncoder = passwordEncoder;
        this.rolesRepo = rolesRepo;
        this.gpxUtil = gpxUtil;
        this.userRepo = userRepo;
        this.routesRepo = routesRepo;
        this.currentUser = currentUser;
        this.fileUtil = fileUtil;
        this.categoryRepo = categoryRepo;
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
                destination.setLevel(source.getLevel() != null ? source.getLevel().name() : null);
                destination.setPictures(source.getPictures());
                destination.setAuthor(source.getAuthor() != null ? source.getAuthor().getUsername() : null);
                destination.setVideoUrl(source.getVideoUrl());
                destination.setComments(source.getComments());
                
                if (source.getGpxCoordinates().startsWith("src")) {
                    System.out.println("source.getGpxCoordinates(): " + source.getGpxCoordinates());
                    destination.setDistance(gpxUtil.calculateDistanceFromFile(source.getGpxCoordinates()));

                    
                }else{
                    destination.setDistance(gpxUtil.calculateDistance(source.getGpxCoordinates()));
                }

                try {
                    WayPoint startPoint = gpxUtil.getStartWayPoint(source.getGpxCoordinates());
                    destination.setStart(startPoint);

                    WayPoint endPoint = gpxUtil.getEndWayPoint(source.getGpxCoordinates());
                    destination.setEnd(endPoint);
                } catch (IOException e) {
                    System.err.println("Error reading GPX file: " + source.getGpxCoordinates());
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
                    destination
                            .setUrl("/uploads/" + source.getRouteId() + "_" + source.getImage().getOriginalFilename());
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

       
        Converter<CommentDto, CommentEntity> postCommentConverter = new Converter<CommentDto, CommentEntity>() {
            @Override
            public CommentEntity convert(MappingContext<CommentDto, CommentEntity> context) {
                CommentDto source = context.getSource();
                CommentEntity destination = new CommentEntity();

                destination.setApproved(true);
                destination.setCreated(LocalDateTime.now());
                destination.setTextContent(source.getTextContent());
                destination.setAuthor(userRepo.findByUsername(currentUser.getUsername()).orElseThrow());

                return destination;
            }

        };

        modelMapper.addConverter(postCommentConverter);

        Converter<RouteAddBindingModel, RouteEntity> routeBindingToEntityConverter = new Converter<RouteAddBindingModel, RouteEntity>() {
            @Override
            public RouteEntity convert(MappingContext<RouteAddBindingModel, RouteEntity> context) {
                RouteAddBindingModel source = context.getSource();
                RouteEntity destination = new RouteEntity();

                destination.setName(source.getName());
                destination.setDescription(source.getDescription());
                destination.setVideoUrl(source.getVideoUrl());
                destination.setLevel(source.getLevel());
                if (currentUser == null || currentUser.getUsername() == null) {
                    throw new RuntimeException("User must be logged in to create a route");
                }
                destination.setAuthor(userRepo.findByUsername(currentUser.getUsername()).orElseThrow());
                
                // Convert CategoryEnum values to CategoryEntities
                if (source.getCategories() != null) {
                    List<CategoryEntities> categories = source.getCategories().stream()
                        .map(categoryEnum -> categoryRepo.findByName(categoryEnum)
                            .orElseThrow(() -> new RuntimeException("Category not found: " + categoryEnum)))
                        .collect(Collectors.toList());
                    destination.setCategories(categories);
                }

                return destination;
            }
        };

        modelMapper.addConverter(routeBindingToEntityConverter);


        return modelMapper;
    }

}
