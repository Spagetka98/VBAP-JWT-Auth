package cz.osu.theatre.repositories;

import cz.osu.theatre.models.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<AppUser> findByRefreshTokenToken(String requestRefreshToken);

    Optional<AppUser> findByUsername(String username);
}
