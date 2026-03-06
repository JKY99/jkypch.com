package com.jkypch.web.repository.jpa;

import com.jkypch.web.domain.AdminUser;
import com.jkypch.web.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.adminUser = :adminUser")
    void deleteByAdminUser(AdminUser adminUser);
}
