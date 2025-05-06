package bg.softuni.pathfinder.reposityory;

import bg.softuni.pathfinder.model.entity.CategoryEntities;
import bg.softuni.pathfinder.model.entity.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntities, Long> {
    Optional<CategoryEntities> findByName(CategoryEnum name);
} 