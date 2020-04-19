//package com.pepcus.apps.config;
//
//import javax.persistence.EntityManagerFactory;
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import com.pepcus.apps.db.repositories.BaseRepositoryImpl;
//
//
///**
// * Database configuration file to manage datasource
// * 
// *
// */
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "entityManagerFactory", 
//        transactionManagerRef = "transactionManager",
//        basePackages = { "com.pepcus.apps.db2.repositories" },
//        repositoryBaseClass = BaseRepositoryImpl.class
//)
//public class Database2Config {
//
//    @Bean(name = "dataSource")
//    @ConfigurationProperties(prefix = "spring.datasource2")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
//            @Qualifier("dataSource") DataSource dataSource) {
//
//        return builder.dataSource(dataSource).packages("com.pepcus.apps.db2.entities").persistenceUnit("core").build();
//    }
//
//    @Bean(name = "transactionManager")
//    public PlatformTransactionManager transactionManager(
//            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
//        
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//    @Bean(name = "springData2JdbcTemplate")
//    public JdbcTemplate portalJdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
//}