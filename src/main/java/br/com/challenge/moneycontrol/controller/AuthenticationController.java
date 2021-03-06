package br.com.challenge.moneycontrol.controller;

import br.com.challenge.moneycontrol.DTO.TokenDTO;
import br.com.challenge.moneycontrol.config.security.TokenService;
import br.com.challenge.moneycontrol.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginForm form){
        UsernamePasswordAuthenticationToken authToken = form.convert();
        try {
            Authentication authentication =
                    authManager.authenticate(authToken);
            String token = tokenService.generateToken(authentication);
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Algo deu errado, " +
                    "verifique se o usuário e a senha estão corretos.");
        }
    }
}
