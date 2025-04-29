package bg.softuni.pathfinder.service;

import bg.softuni.pathfinder.model.dto.PIcturesDTO;
import bg.softuni.pathfinder.model.entity.PictureEntity;
import bg.softuni.pathfinder.reposityory.PicturesRepo;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PictureService {

    private final ModelMapper modelMapper;
    private final PicturesRepo pictureRepo;

    public PictureService(ModelMapper modelMapper, PicturesRepo pictureRepo) {
        this.modelMapper = modelMapper;
        this.pictureRepo = pictureRepo;
    }

    public List<PIcturesDTO> getAllPictures() {
        return pictureRepo.findAll().stream()
                .map(this::picturesToDTO)
                .collect(Collectors.toList());
    }

    private PIcturesDTO picturesToDTO(PictureEntity pictureEntity) {
        return modelMapper.map(pictureEntity, PIcturesDTO.class);
    }

}
