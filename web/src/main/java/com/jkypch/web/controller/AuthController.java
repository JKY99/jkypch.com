package com.jkypch.web.controller;

import com.jkypch.web.domain.AdminUser;
import com.jkypch.web.domain.RefreshToken;
import com.jkypch.web.dto.LoginRequest;
import com.jkypch.web.dto.LoginResponse;
import com.jkypch.web.dto.RefreshRequest;
import com.jkypch.web.repository.jpa.AdminUserRepository;
import com.jkypch.web.repository.jpa.RefreshTokenRepository;
import com.jkypch.web.security.JwtTokenProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin/auth")
@ConditionalOnExpression("!'${spring.datasource.oracle.url:}'.isEmpty()")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminUserRepository adminUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          AdminUserRepository adminUserRepository,
                          RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminUserRepository = adminUserRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        AdminUser user = adminUserRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException(auth.getName()));

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getRole().name());
        String refreshTokenValue = jwtTokenProvider.generateRefreshTokenValue();

        refreshTokenRepository.deleteByAdminUser(user);
        RefreshToken refreshToken = new RefreshToken(refreshTokenValue, user, jwtTokenProvider.getRefreshTokenExpiry());
        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(accessToken, refreshTokenValue, user.getRole().name());
    }

    @Transactional
    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody RefreshRequest req) {
        RefreshToken stored = refreshTokenRepository.findByToken(req.refreshToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        if (stored.isExpired()) {
            refreshTokenRepository.delete(stored);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired");
        }

        AdminUser user = stored.getAdminUser();
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getRole().name());
        String newRefreshTokenValue = jwtTokenProvider.generateRefreshTokenValue();

        refreshTokenRepository.delete(stored);
        RefreshToken newRefreshToken = new RefreshToken(newRefreshTokenValue, user, jwtTokenProvider.getRefreshTokenExpiry());
        refreshTokenRepository.save(newRefreshToken);

        return new LoginResponse(accessToken, newRefreshTokenValue, user.getRole().name());
    }

    @PostMapping("/logout")
    public void logout(@RequestBody RefreshRequest req) {
        refreshTokenRepository.findByToken(req.refreshToken())
                .ifPresent(refreshTokenRepository::delete);
    }
}
