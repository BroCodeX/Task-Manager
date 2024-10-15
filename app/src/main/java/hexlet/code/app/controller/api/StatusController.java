package hexlet.code.app.controller.api;

import hexlet.code.app.dto.status.StatusCreateDTO;
import hexlet.code.app.dto.status.StatusDTO;
import hexlet.code.app.dto.status.StatusUpdateDTO;
import hexlet.code.app.service.StatusService;
import hexlet.code.app.util.UserUtils;
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

    @Autowired
    private UserUtils userUtils;

    @GetMapping("")
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
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
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO show(@PathVariable Long id) {
        return service.showStatus(id);
    }

    @PostMapping("")
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
    @ResponseStatus(HttpStatus.CREATED)
    public StatusDTO create(@Valid @RequestBody StatusCreateDTO dto) {
        return service.createStatus(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
    @ResponseStatus(HttpStatus.OK)
    public StatusDTO update(@Valid @RequestBody StatusUpdateDTO dto, @PathVariable Long id) {
        return service.updateStatus(dto, id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() and @userUtils.isExists(authentication.principal.getClaim('sub'))")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        service.destroyStatus(id);
    }
}
