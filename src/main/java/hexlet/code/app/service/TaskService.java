package hexlet.code.app.service;

import hexlet.code.app.component.TaskSpecification;
import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskFilterDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private TaskSpecification specification;

    public List<TaskDTO> getAll(int limit) {
        return repository.findAll().stream()
                .map(mapper::map)
                .limit(limit)
                .toList();
    }

    public List<TaskDTO> getAll(TaskFilterDTO filterDTO, int limit) {
        var spec = specification.build(filterDTO);
        Page<Task> filteredTasks = repository.findAll(spec, PageRequest.of(limit - 1, 10));
        return filteredTasks.stream().map(mapper::map).toList();
    }

    public TaskDTO getTaskById(long id) {
        var maybeTask =  repository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        return mapper.map(maybeTask);
    }

    public TaskDTO createTask(TaskCreateDTO dto) {
        var task = mapper.map(dto);
        repository.save(task);
        return mapper.map(task);
    }

    public TaskDTO updateTask(TaskUpdateDTO dto, long id) {
        var maybeTask =  repository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        mapper.update(dto, maybeTask);
        repository.save(maybeTask);
        return mapper.map(maybeTask);
    }

    public void destroyTask(long id) {
        repository.deleteById(id);
    }
}
