package cz.osu.theatre.controllers;

import cz.osu.theatre.models.requests.RefreshTokenRequest;
import cz.osu.theatre.models.responses.RefreshTokenResponse;
import cz.osu.theatre.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping("/expiration")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info(String.format("Validating refresh token: %s", request.getRefreshToken()));

        String token = refreshTokenService.generateAccessTokenForRefreshToken(request.getRefreshToken());

        log.info("Sending new access token");

        return ResponseEntity.ok(new RefreshTokenResponse(token));
    }
}