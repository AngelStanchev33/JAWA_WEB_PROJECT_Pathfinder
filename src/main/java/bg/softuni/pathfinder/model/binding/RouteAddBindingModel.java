package bg.softuni.pathfinder.model.binding;

import bg.softuni.pathfinder.model.entity.CommentEntity;
import bg.softuni.pathfinder.model.entity.PictureEntity;
import bg.softuni.pathfinder.model.entity.UserEntity;
import bg.softuni.pathfinder.model.entity.enums.CategoryEnum;
import bg.softuni.pathfinder.model.entity.enums.LevelEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class RouteAddBindingModel {
    
    private Long id;

    @NotNull(message = "Please select a GPX file")
    private MultipartFile gpxCoordinates;

    @NotNull
    private LevelEnum level;

    @NotBlank
    @Size(min = 3, max = 20)
    private String name;

    private UserEntity author;

    @NotBlank
    @Size(min = 5)
    private String description;

    private String videoUrl;

    private List<CategoryEnum> categories;

    private List<PictureEntity> pictures;

    private List<CommentEntity> comments;

    private MultipartFile image;

    //getters & setters
    public MultipartFile getGpxCoordinates() {
        return gpxCoordinates;
    }

    public void setGpxCoordinates(MultipartFile gpxCoordinates) {
        this.gpxCoordinates = gpxCoordinates;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public List<CategoryEnum> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEnum> categories) {
        this.categories = categories;
    }

    public List<PictureEntity> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureEntity> pictures) {
        this.pictures = pictures;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
