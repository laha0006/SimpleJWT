package dev.tolana.simplejwt.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email);
    Optional<AppUser> findByUsername(String username);
}
