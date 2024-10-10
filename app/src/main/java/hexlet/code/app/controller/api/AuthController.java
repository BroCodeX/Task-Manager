package hexlet.code.app.controller.api;

import hexlet.code.app.dto.AuthDTO;
import hexlet.code.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String authentification(@Valid @RequestBody AuthDTO dto) {
        return authService.login(dto);
    }
}
