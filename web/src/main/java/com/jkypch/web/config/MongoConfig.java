package com.jkypch.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB 설정
 * spring.data.mongodb.uri 로 연결 주소 지정 (application.yml)
 * Repository: com.jkypch.web.repository.mongo
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.jkypch.web.repository.mongo")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "jkypch";
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}
