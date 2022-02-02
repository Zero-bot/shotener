package com.intuit.shortener.config

import org.apache.ibatis.session.ExecutorType
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource

@Configuration
@MapperScan(basePackages = ["com"], lazyInitialization = "\${mybatis.lazy-initialization:true}")
class DaoConfiguration {

    @Bean
    fun driverManagerDataSourceBean(): DriverManagerDataSource{
        val driverManagerDataSource = DriverManagerDataSource()
        driverManagerDataSource.setDriverClassName("org.postgresql.Driver")
        driverManagerDataSource.url = "jdbc:postgresql://127.0.0.1:5432/postgres?currentSchema=public"
        driverManagerDataSource.username = "postgres"
        driverManagerDataSource.password = System.getenv("PG_PASSWORD")
        return driverManagerDataSource
    }

    @Bean
    fun sqlSessionFactoryBean(): SqlSessionFactoryBean{
        val sessionFactoryBean = SqlSessionFactoryBean()
        val configuration = org.apache.ibatis.session.Configuration()
        configuration.isMapUnderscoreToCamelCase = true
        configuration.defaultExecutorType = ExecutorType.BATCH
        sessionFactoryBean.setConfiguration(configuration)
        sessionFactoryBean.setDataSource(driverManagerDataSourceBean())
        return sessionFactoryBean
    }
}