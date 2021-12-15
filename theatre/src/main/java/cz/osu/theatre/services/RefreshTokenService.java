package cz.osu.theatre.services;

import cz.osu.theatre.models.entities.AppUser;
import cz.osu.theatre.models.entities.RefreshToken;

public interface RefreshTokenService {
    String generateAccessTokenForRefreshToken(String requestRefreshToken);

    boolean isUserRefreshTokenExpired(AppUser user);

    RefreshToken createRefreshTokenForUser(long userId);

}

