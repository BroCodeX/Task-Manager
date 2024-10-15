package hexlet.code.app.mapper;

import hexlet.code.app.model.BaseEntity;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.StatusRepository;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private LabelRepository labelRepository;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    @Named("toStatusEntity")
    public Status toStatusEntity(String name) {
        return statusRepository.findByName(name).orElse(null);
    }

    @Named("toLabelEntity")
    public Label toLabelEntity(String label) {
        return labelRepository.findByName(label).orElse(null);
    }

    @Named("toLabelEntities")
    public List<Label> toLabelEntities(String label) {
        return labelRepository.findAll();
    }
}
