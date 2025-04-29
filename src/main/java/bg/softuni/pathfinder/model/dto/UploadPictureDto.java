package bg.softuni.pathfinder.model.dto;
import org.springframework.web.multipart.MultipartFile;



public class UploadPictureDto {
    private Long authorId;
    private Long routeId;
    private MultipartFile image;
    private String urlAddress;
    private String title;
   

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title ;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long id) {
        this.authorId = id;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile picture) {
        this.image = picture;
    }
    
}
