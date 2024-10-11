package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    public List<TaskDTO> getAll(int limit) {

    }

    public TaskDTO show(long id) {

    }

    public TaskDTO create(TaskCreateDTO dto) {

    }

    public TaskDTO update(TaskUpdateDTO dto, long id) {

    }

    public void destroy(long id) {

    }
}
