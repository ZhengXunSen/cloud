package com.zxs.cloud.fetch.config;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.autoconfigure.MapperAutoConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import com.zxs.cloud.fetch.config.datasource.DynamicDataSourceAspect;
import com.zxs.cloud.fetch.config.datasource.MultipleDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.zxs.cloud.fetch.constant.DataSource.DC;
import static com.zxs.cloud.fetch.constant.DataSource.FETCH;

/**
 * Created by jackie.yu on 2017/7/27.
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = DataSourceConfig.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    static final String PACKAGE = "com.jfz.search.dao.mapper";

    @Bean
    public MapperAutoConfiguration mapperAutoConfiguration(final SqlSessionFactory sqlSessionFactory) {
        return new MapperAutoConfiguration(sqlSessionFactory);
    }

    @Bean
    @Primary
    @DependsOn({"dcDataSource", "simuDataSource", "xintuoDataSource", "touyanDataSource", "informationDataSource", "jfzpushDataSource","resultdataDataSource","jfzcommonDataSource","jfzcommunityDataSource"})
    public DataSource dataSource() {
        final MultipleDataSource multipleDataSource = new MultipleDataSource();
        final Map dataSourceMap = new HashMap();
        final DataSource defaultDataSource = fetchDataSource();
        dataSourceMap.put(DC.getName(), dcDataSource());
        dataSourceMap.put(FETCH.getName(), fetchDataSource());
        multipleDataSource.setTargetDataSources(dataSourceMap);
        multipleDataSource.setDefaultTargetDataSource(defaultDataSource);
        return multipleDataSource;
    }


    @Bean
    public DynamicDataSourceAspect dynamicDataSourceAspect() {
        return new DynamicDataSourceAspect();
    }

    @Bean
    @ConfigurationProperties("datasource.dc")
    public DataSource dcDataSource() {
        return new HikariDataSource();
    }

    @Bean
    @ConfigurationProperties("datasource.fetch")
    public DataSource fetchDataSource() {
        return new HikariDataSource();
    }

    /**
     * 分页插件，mybatis会自动扫描该bean及自动注入到其插件中
     *
     * @return
     */
    @Bean
    public PageHelper pageHelper() {
        final PageHelper pageHelper = new PageHelper();
        final Properties p = new Properties();
        p.setProperty("dialect", "mysql");
        p.setProperty("pageSizeZero", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

}
