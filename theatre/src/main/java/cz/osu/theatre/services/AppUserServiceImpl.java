package cz.osu.theatre.services;

import cz.osu.theatre.errors.exceptions.UserNotFoundException;
import cz.osu.theatre.models.entities.AppUser;
import cz.osu.theatre.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService{
    private final AppUserRepository appUserRepository;

    @Override
    public AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return this.appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username: %s was not found !", username)));
    }
}
