package com.zxs.cloud.front.api.config;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.autoconfigure.MapperAutoConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    static final String PACKAGE = "com.zxs.cloud.front.api.mapper";

    @Bean
    public MapperAutoConfiguration mapperAutoConfiguration(final SqlSessionFactory sqlSessionFactory) {
        return new MapperAutoConfiguration(sqlSessionFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource resultDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    public DataSourceTransactionManager rdsTransactionManager(DataSource resultDataSource) {
        return new DataSourceTransactionManager(resultDataSource);
    }

    /**
     * 分页插件，mybatis会自动扫描该bean及自动注入到其插件中
     *
     * @return
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("dialect", "mysql");
        p.setProperty("pageSizeZero", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

}