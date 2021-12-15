package cz.osu.theatre.controllers;

import cz.osu.theatre.models.requests.LoginRequest;
import cz.osu.theatre.models.requests.SignUpRequest;
import cz.osu.theatre.models.responses.JwtResponse;
import cz.osu.theatre.models.responses.MessageResponse;
import cz.osu.theatre.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info(String.format("Logging user with username: %s", loginRequest.getUsername()));

        JwtResponse jwtResponse = authenticationService.authUser(loginRequest.getUsername(), loginRequest.getPassword());

        log.info("User was logged successfully!");

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.info(String.format("Registrating user with username: %s , email: %s", signUpRequest.getUsername(), signUpRequest.getEmail()));

        authenticationService.registerUser(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword());

        log.info("User was registered!");

        return ResponseEntity.ok(new MessageResponse(HttpStatus.OK, "User was registered!", LocalDateTime.now()));
    }

}
