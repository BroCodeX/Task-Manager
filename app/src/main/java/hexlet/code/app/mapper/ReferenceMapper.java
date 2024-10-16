package hexlet.code.app.mapper;

import hexlet.code.app.exception.ResourceNotFoundExcepiton;
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

import java.util.ArrayList;
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
    public Status toStatusEntity(String slug) {
        return statusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("Slug " + slug + " is not found"));
    }

    @Named("toLabelEntity")
    public Label toLabelEntity(Long labelID) {
        return labelRepository.findById(labelID)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("Label with id " + labelID + " not found"));
    }

    @Named("toLabelEntities")
    public List<Label> toLabelEntities(List<Long> labelsIDs) {
        return labelsIDs == null ? new ArrayList<>() : labelsIDs.stream()
                .map(this::toLabelEntity)
                .toList();
    }

    @Named("toLabelNames")
    public List<Long> toLabelNames(List<Label> labels) {
        return labels == null ? new ArrayList<>() :labels.stream()
                .map(Label::getId)
                .toList();
    }
}
