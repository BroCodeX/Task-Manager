package hexlet.code.app.component;

import hexlet.code.app.dto.task.TaskFilterDTO;
import hexlet.code.app.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskFilterDTO params) {
        return withTitleCont(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withSlug(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    public Specification<Task> withTitleCont(String taskTitle) {
        return (root, query, cb) -> taskTitle.isBlank()
                ? cb.conjunction()
                : cb.equal(root.get("name"), taskTitle);
    }

    public Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null
                ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    public Specification<Task> withSlug(String slug) {
        return (root, query, cb) -> slug.isBlank()
                ? cb.conjunction()
                : cb.equal(root.get("Status").get("slug"), slug);
    }

    public Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> labelId == null
                ? cb.conjunction()
                : cb.equal(root.get("labels").get("id"), labelId);
    }
}
