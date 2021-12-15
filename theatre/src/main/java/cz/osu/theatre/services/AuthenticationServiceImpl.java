package cz.osu.theatre.services;

import cz.osu.theatre.errors.exceptions.UserInformationTakenException;
import cz.osu.theatre.models.entities.AppUser;
import cz.osu.theatre.models.entities.RefreshToken;
import cz.osu.theatre.models.responses.JwtResponse;
import cz.osu.theatre.repositories.AppUserRepository;
import cz.osu.theatre.security.authUserDetails.UserDetailsImpl;
import cz.osu.theatre.security.jwt.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AppUserRepository appUserRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtils;

    @Override
    public void registerUser(String username, String email, String pass) {
        if (appUserRepository.existsByUsername(username))
            throw new UserInformationTakenException("NAME_TAKEN");

        if (appUserRepository.existsByEmail(email))
            throw new UserInformationTakenException("EMAIL_TAKEN");

        AppUser user = new AppUser(
                username,
                email,
                encoder.encode(pass)
        );

        appUserRepository.save(user);
    }

    @Override
    public JwtResponse authUser(String username, String pass) {
        Authentication authentication;
        UserDetailsImpl userDetails;
        RefreshToken refreshToken;

        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, pass));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        userDetails = (UserDetailsImpl) authentication.getPrincipal();

        refreshToken = refreshTokenService.createRefreshTokenForUser(userDetails.getId());

        String accessToken = jwtUtils.generateJwtToken(userDetails.getUsername());

        return new JwtResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                accessToken,
                refreshToken.getToken());
    }
}
