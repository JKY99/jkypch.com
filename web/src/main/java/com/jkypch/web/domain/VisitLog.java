package com.jkypch.web.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VISIT_LOG")
public class VisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visit_log_seq")
    @SequenceGenerator(name = "visit_log_seq", sequenceName = "VISIT_LOG_SEQ", allocationSize = 50)
    private Long id;

    @Column(length = 500)
    private String path;

    @Column(length = 10)
    private String method;

    @Column(length = 100)
    private String ip;

    private Integer statusCode;

    @Column(length = 500)
    private String userAgent;

    private LocalDateTime createdAt;

    public VisitLog() {}

    public VisitLog(String path, String method, String ip, Integer statusCode, String userAgent) {
        this.path = path;
        this.method = method;
        this.ip = ip;
        this.statusCode = statusCode;
        this.userAgent = userAgent;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getPath() { return path; }
    public String getMethod() { return method; }
    public String getIp() { return ip; }
    public Integer getStatusCode() { return statusCode; }
    public String getUserAgent() { return userAgent; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
