package bg.softuni.pathfinder.model.entity;

import bg.softuni.pathfinder.model.entity.enums.CategoryEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class CategoryEntities extends BaseEntity{

    @Column(nullable = false,unique = true)
    @Enumerated(EnumType.STRING)
    private CategoryEnum name;

    @Column(columnDefinition = "TEXT")
    private String description;


    //getters & setters
    public CategoryEnum getName() {
        return name;
    }

    public void setName(CategoryEnum name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
