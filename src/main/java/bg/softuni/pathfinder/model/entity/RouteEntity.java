package bg.softuni.pathfinder.model.entity;

import bg.softuni.pathfinder.model.entity.enums.LevelEnum;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routes")
public class RouteEntity extends BaseEntity {

    @Column(columnDefinition = "LONGTEXT",nullable = false)
    private String gpxCoordinates;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    private UserEntity author;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String videoUrl;

    @ManyToMany
    private List<CategoryEntities> categories;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PictureEntity> pictures;


    public RouteEntity() {
        this.pictures = new ArrayList<>();
    }

    //getters & setters
    public String getGpxCoordinates() {
        return gpxCoordinates;
    }

    public void setGpxCoordinates(String coordinates) {
        this.gpxCoordinates = coordinates;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PictureEntity> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureEntity> pictures) {
        this.pictures = pictures;
    }
}
