package cz.osu.theatre.services;

import cz.osu.theatre.models.responses.JwtResponse;

public interface AuthenticationService {
    JwtResponse authUser(String username, String password);

    void registerUser(String username, String email, String password);
}
