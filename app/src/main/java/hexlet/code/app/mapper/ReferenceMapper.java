package hexlet.code.app.mapper;

import hexlet.code.app.model.BaseEntity;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.StatusRepository;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private StatusRepository statusRepository;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }

    @Named("toStatusEntity")
    public Status toStatusEntity(String name) {
        return statusRepository.findByName(name).orElse(null);
    }
}
