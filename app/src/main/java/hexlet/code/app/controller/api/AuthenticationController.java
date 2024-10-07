package hexlet.code.app.controller.api;

import hexlet.code.app.dto.AuthDTO;
import hexlet.code.app.service.UserAuthService;
import hexlet.code.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private UserAuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String authentification(@Valid @RequestBody AuthDTO dto) {
        return authService.login(dto);
    }
}
