package bg.softuni.pathfinder.reposityory;

import bg.softuni.pathfinder.model.entity.RoleEntity;
import bg.softuni.pathfinder.model.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepo extends JpaRepository<RoleEntity, Long> {
    
    Optional<RoleEntity> findByName(RoleEnum name);
}
