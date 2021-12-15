package cz.osu.theatre.services;

import cz.osu.theatre.errors.exceptions.RefreshTokenException;
import cz.osu.theatre.errors.exceptions.UserNotFoundException;
import cz.osu.theatre.models.entities.AppUser;
import cz.osu.theatre.models.entities.RefreshToken;
import cz.osu.theatre.repositories.AppUserRepository;
import cz.osu.theatre.repositories.RefreshTokenRepository;
import cz.osu.theatre.security.jwt.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final AppUserRepository appUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtility jwtUtility;

    @Value("${auth.jwtRefreshTokenExpirationMs}")
    private Long refreshTokenDurationMs;

    @Override
    public String generateAccessTokenForRefreshToken(String requestRefreshToken) {
        if (requestRefreshToken == null) throw new RefreshTokenException("Invalid refresh token!");

        AppUser user = appUserRepository.findByRefreshTokenToken(requestRefreshToken)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token !"));

        if (this.isUserRefreshTokenExpired(user))
            throw new RefreshTokenException("Invalid refresh token !");

        return jwtUtility.generateJwtToken(user.getUsername());
    }

    @Transactional
    @Override
    public boolean isUserRefreshTokenExpired(AppUser user) {
        if (user.getRefreshToken().getExpiryDate().compareTo(Instant.now()) < 0) {

            this.deleteOldRefreshToken(user);
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public RefreshToken createRefreshTokenForUser(long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %s was not found !", userId)));

        this.deleteOldRefreshToken(user);

        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenDurationMs),
                user
        );

        user.setRefreshToken(refreshToken);

        appUserRepository.save(user);
        return user.getRefreshToken();
    }

    private void deleteOldRefreshToken(AppUser user) {
        RefreshToken token = user.getRefreshToken();

        if(token != null){
            user.setRefreshToken(null);
            this.appUserRepository.save(user);
            this.refreshTokenRepository.delete(token);
        }
    }

}