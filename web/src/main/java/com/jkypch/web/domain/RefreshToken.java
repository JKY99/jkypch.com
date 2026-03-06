package com.jkypch.web.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "REFRESH_TOKEN")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
    @SequenceGenerator(name = "refresh_token_seq", sequenceName = "REFRESH_TOKEN_SEQ", allocationSize = 1)
    private Long id;

    @Column(unique = true, nullable = false, length = 200)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_USER_ID", nullable = false)
    private AdminUser adminUser;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    public RefreshToken() {}

    public RefreshToken(String token, AdminUser adminUser, LocalDateTime expiresAt) {
        this.token = token;
        this.adminUser = adminUser;
        this.expiresAt = expiresAt;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public AdminUser getAdminUser() { return adminUser; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
}
