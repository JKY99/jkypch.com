package com.jkypch.web.interceptor;

import com.jkypch.web.domain.VisitLog;
import com.jkypch.web.repository.jpa.VisitLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@ConditionalOnExpression("!'${spring.datasource.oracle.url:}'.isEmpty()")
public class VisitLogInterceptor implements HandlerInterceptor {

    private final VisitLogRepository visitLogRepository;

    public VisitLogInterceptor(VisitLogRepository visitLogRepository) {
        this.visitLogRepository = visitLogRepository;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        try {
            String ip = request.getHeader("X-Real-IP");
            if (ip == null) ip = request.getRemoteAddr();

            visitLogRepository.save(new VisitLog(
                request.getRequestURI(),
                request.getMethod(),
                ip,
                response.getStatus(),
                request.getHeader("User-Agent")
            ));
        } catch (Exception ignored) {}
    }
}
