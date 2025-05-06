package bg.softuni.pathfinder.reposityory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bg.softuni.pathfinder.model.entity.RouteEntity;

@Repository
public interface RouteRepo extends JpaRepository<RouteEntity, Long> {

}
