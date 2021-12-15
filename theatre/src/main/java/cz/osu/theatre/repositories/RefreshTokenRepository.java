package cz.osu.theatre.repositories;

import cz.osu.theatre.models.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    void deleteByAppUser_Id(long userId);
}
