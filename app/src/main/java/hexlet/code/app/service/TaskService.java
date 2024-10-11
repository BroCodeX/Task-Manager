package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundExcepiton;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper mapper;

    public List<TaskDTO> getAll(int limit) {
        return repository.findAll().stream()
                .map(mapper::map)
                .toList();
    }

    public TaskDTO show(long id) {
        var maybeTask =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("This id: " + id + " is not found"));
        return mapper.map(maybeTask);
    }

    public TaskDTO create(TaskCreateDTO dto) {
        var task = mapper.map(dto);
        repository.save(task);
        return mapper.map(task);
    }

    public TaskDTO update(TaskUpdateDTO dto, long id) {
        var maybeTask =  repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExcepiton("This id: " + id + " is not found"));
        mapper.update(dto, maybeTask);
        return mapper.map(maybeTask);
    }

    public void destroy(long id) {
        repository.deleteById(id);
    }
}
