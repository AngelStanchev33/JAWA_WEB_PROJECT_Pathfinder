package bg.softuni.pathfinder.model.dto;

import java.util.List;

import bg.softuni.pathfinder.model.entity.PictureEntity;
import bg.softuni.pathfinder.model.entity.UserEntity;
import io.jenetics.jpx.WayPoint;

public class RouteDTO {
    private Long id;
    private String gpxCoordinates;
    private String level;
    private String name;
    private UserEntity author;
    private String description;
    private String videoUrl;
    private List<PictureEntity> pictures;
    private Double distance;
    private WayPoint start;
    private WayPoint end;

     // getters & setters
    public WayPoint getStart() {
        return start;
    }

    public void setStart(WayPoint start) {
        this.start = start;
    }

    public WayPoint getEnd() {
        return end;
    }

    public void setEnd(WayPoint end) {
        this.end = end;
    }

   
    public List<PictureEntity> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureEntity> pictures) {
        this.pictures = pictures;
    }

    public String getGpxCoordinates() {
        return gpxCoordinates;
    }

    public void setGpxCoordinates(String gpxCoordinates) {
        this.gpxCoordinates = gpxCoordinates;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author.getUsername();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
