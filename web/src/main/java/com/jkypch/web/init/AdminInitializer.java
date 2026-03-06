package com.jkypch.web.init;

import com.jkypch.web.domain.AdminUser;
import com.jkypch.web.domain.Role;
import com.jkypch.web.repository.jpa.AdminUserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("!'${spring.datasource.oracle.url:}'.isEmpty()")
public class AdminInitializer implements ApplicationRunner {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!adminUserRepository.existsByUsername("admin")) {
            AdminUser admin = new AdminUser("admin", passwordEncoder.encode("admin"), Role.ADMIN);
            adminUserRepository.save(admin);
        }
    }
}
