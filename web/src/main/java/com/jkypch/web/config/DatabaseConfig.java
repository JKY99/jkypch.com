package com.jkypch.web.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Oracle DataSource 설정 (JPA + MyBatis 공유)
 * spring.datasource.oracle.url 이 설정된 경우에만 활성화
 */
@Configuration
@ConditionalOnExpression("!'${spring.datasource.oracle.url:}'.isEmpty()")
@EnableJpaRepositories(
    basePackages = "com.jkypch.web.repository.jpa",
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
@MapperScan(
    basePackages = "com.jkypch.web.repository.mybatis",
    sqlSessionFactoryRef = "sqlSessionFactory"
)
public class DatabaseConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.oracle")
    public DataSourceProperties oracleDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource oracleDataSource(
            @Qualifier("oracleDataSourceProperties") DataSourceProperties props) {
        return props.initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("oracleDataSource") DataSource dataSource) {

        var adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabasePlatform("org.hibernate.dialect.OracleDialect");

        var factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("com.jkypch.web.domain");
        factory.setJpaVendorAdapter(adapter);

        var props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", "update");
        props.setProperty("hibernate.show_sql", "false");
        props.setProperty("hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        factory.setJpaProperties(props);

        return factory;
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean emf) {
        return new JpaTransactionManager(emf.getObject());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("oracleDataSource") DataSource dataSource) throws Exception {
        var factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setMapperLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*.xml")
        );
        return factory.getObject();
    }
}
