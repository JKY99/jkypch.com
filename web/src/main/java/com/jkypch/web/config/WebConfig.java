package com.jkypch.web.config;

import com.jkypch.web.interceptor.VisitLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnExpression("!'${spring.datasource.oracle.url:}'.isEmpty()")
public class WebConfig implements WebMvcConfigurer {

    private final VisitLogInterceptor visitLogInterceptor;

    public WebConfig(VisitLogInterceptor visitLogInterceptor) {
        this.visitLogInterceptor = visitLogInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitLogInterceptor)
                .excludePathPatterns("/actuator/**", "/api/visit-logs/**");
    }
}
