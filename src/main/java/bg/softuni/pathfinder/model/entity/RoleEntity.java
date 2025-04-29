package bg.softuni.pathfinder.model.entity;

import bg.softuni.pathfinder.model.entity.enums.RoleEnum;
import jakarta.persistence.*;


@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity{

    @Column(nullable = false, name = "role")
    @Enumerated(EnumType.STRING)
    private RoleEnum name;


    //getters & setters
    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum name) {
        this.name = name;
    }
}
