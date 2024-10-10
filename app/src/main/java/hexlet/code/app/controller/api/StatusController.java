package hexlet.code.app.controller.api;

import hexlet.code.app.dto.StatusCreateDTO;
import hexlet.code.app.dto.StatusDTO;
import hexlet.code.app.dto.StatusUpdateDTO;
import hexlet.code.app.service.StatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
public class StatusController {

    @Autowired
    private StatusService service;

    @GetMapping("")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<StatusDTO>> index(@RequestParam(defaultValue = "10") Integer limit) {
        List<StatusDTO> statusDTOS = service.getAll(limit);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(statusDTOS.size()));
        return ResponseEntity.ok()
                .headers(headers)
                .body(statusDTOS);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO show(@PathVariable long id) {
        return service.show(id);
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public StatusDTO create(@Valid @RequestBody StatusCreateDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO update(@Valid @RequestBody StatusUpdateDTO dto, @PathVariable long id) {
        return service.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable long id) {
        service.destroy(id);
    }
}
