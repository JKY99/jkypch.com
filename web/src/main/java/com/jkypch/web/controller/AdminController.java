package com.jkypch.web.controller;

import com.jkypch.web.domain.AdminUser;
import com.jkypch.web.domain.VisitLog;
import com.jkypch.web.dto.ChangePasswordRequest;
import com.jkypch.web.dto.VisitStatsDto;
import com.jkypch.web.repository.jpa.AdminUserRepository;
import com.jkypch.web.repository.mybatis.VisitLogMapper;
import com.jkypch.web.security.AdminUserDetails;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@ConditionalOnExpression("!'${spring.datasource.oracle.url:}'.isEmpty()")
public class AdminController {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final VisitLogMapper visitLogMapper;

    public AdminController(AdminUserRepository adminUserRepository,
                           PasswordEncoder passwordEncoder,
                           VisitLogMapper visitLogMapper) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.visitLogMapper = visitLogMapper;
    }

    @GetMapping("/me")
    public Map<String, String> me(@AuthenticationPrincipal AdminUserDetails userDetails) {
        return Map.of("username", userDetails.getUsername(), "role", userDetails.getRole().name());
    }

    @PutMapping("/password")
    public void changePassword(@AuthenticationPrincipal AdminUserDetails userDetails,
                               @RequestBody ChangePasswordRequest req) {
        AdminUser user = adminUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(req.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(req.newPassword()));
        adminUserRepository.save(user);
    }

    @GetMapping("/visits/stats")
    public VisitStatsDto visitStats() {
        return new VisitStatsDto(
                visitLogMapper.countTotal(),
                visitLogMapper.countToday(),
                visitLogMapper.countUniqueIps(),
                visitLogMapper.topPages(10),
                visitLogMapper.dailyTrend(7)
        );
    }

    @GetMapping("/visits/logs")
    public List<VisitLog> visitLogs(@RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "50") int limit) {
        return visitLogMapper.findPage(offset, Math.min(limit, 200));
    }
}
