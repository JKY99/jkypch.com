package com.jkypch.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB 설정
 * spring.mongodb.uri 로 연결 주소 지정 (application.yml → MONGODB_URI 환경변수)
 * Repository: com.jkypch.web.repository.mongo
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.jkypch.web.repository.mongo")
public class MongoConfig {
}
