package org.appitcompany.kuimakulak.jpaRepository;

import org.appitcompany.kuimakulak.entity.RefreshToken;
import org.appitcompany.kuimakulak.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteByToken(String token);
}
