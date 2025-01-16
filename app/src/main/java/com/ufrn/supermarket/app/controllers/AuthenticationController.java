package com.ufrn.supermarket.app.controllers;

import com.ufrn.supermarket.app.dtos.LoginDTO;
import com.ufrn.supermarket.app.dtos.LoginResponseDTO;
import com.ufrn.supermarket.app.dtos.RegistrarDTO;
import com.ufrn.supermarket.app.entities.User;
import com.ufrn.supermarket.app.infra.TokenService;
import com.ufrn.supermarket.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository repository;

    @Autowired
    TokenService tokenService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {

        try {
            String encryptedPassword = encoder.encode(loginDTO.password());
            System.out.println(
                    "O LOGIN É: " + loginDTO.login() + "A SENHA : " + loginDTO.password() + " " + encryptedPassword);
            var userPassword = new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.password());
            var auth = this.authenticationManager.authenticate(userPassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registra(@RequestBody RegistrarDTO registrarDTO) {
        if (this.repository.findByLogin(registrarDTO.login()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String encriptedPassword = encoder.encode(registrarDTO.password());
        User user = new User(registrarDTO.login(), encriptedPassword, registrarDTO.role());
        this.repository.save(user);
        return ResponseEntity.ok().body("Usuário criado com sucesso!!!");
    }
}
