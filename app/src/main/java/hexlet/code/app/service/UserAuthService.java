package hexlet.code.app.service;

import hexlet.code.app.dto.AuthDTO;
import hexlet.code.app.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    public String login(AuthDTO dtoAuth) {
        var authentification = new UsernamePasswordAuthenticationToken(dtoAuth.getEmail(), dtoAuth.getPassword());
        authenticationManager.authenticate(authentification);
        var token = jwtUtils.generateToken(dtoAuth.getEmail());
        return token;
    }
}
