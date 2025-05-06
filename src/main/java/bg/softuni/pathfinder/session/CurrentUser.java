package bg.softuni.pathfinder.session;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component("currentUser")
@SessionScope
public class CurrentUser {

    private Long id;
    private String username;
    private String fullName;
    private boolean isLogged;
    private boolean isAdmin;

    // getters & setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    // methods
    public void clear() {
        this.id = null;
        this.fullName = null;
        isLogged = false;
        isAdmin = false;
    }

}
