package hexlet.code.app.controller.api;

import hexlet.code.app.dto.task.TaskCreateDTO;
import hexlet.code.app.dto.task.TaskDTO;
import hexlet.code.app.dto.task.TaskFilterDTO;
import hexlet.code.app.dto.task.TaskUpdateDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.util.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private UserUtils userUtils;

    @Autowired
    private TaskService service;

    @Autowired
    private TaskMapper mapper;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
    public ResponseEntity<List<TaskDTO>> index(@ModelAttribute TaskFilterDTO filterDTO,
                                               @RequestParam(defaultValue = "1") Integer limit) {
        var tasks = service.getAll(filterDTO, limit);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(tasks.size()));
        return ResponseEntity.ok()
                .headers(headers)
                .body(tasks);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
    public TaskDTO show(@Valid @PathVariable Long id) {
        return service.showTask(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO dto) {
        return service.createTask(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
    public TaskDTO update(@Valid @RequestBody TaskUpdateDTO dto, @PathVariable Long id) {
        return service.updateTask(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
    public void destroy(@PathVariable Long id) {
        service.destroyTask(id);
    }
}
