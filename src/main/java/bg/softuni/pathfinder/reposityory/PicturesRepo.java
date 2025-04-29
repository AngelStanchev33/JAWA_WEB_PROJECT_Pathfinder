package bg.softuni.pathfinder.reposityory;

import bg.softuni.pathfinder.model.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PicturesRepo extends JpaRepository<PictureEntity, Long> {

}
