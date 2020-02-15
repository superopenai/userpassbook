package me.superning.userpassbook.configuration;

import com.zaxxer.hikari.HikariDataSource;
import me.superning.userpassbook.bean.MyRouteDataSource;
import me.superning.userpassbook.constant.DBType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author superning
 * @Classname DataSourceConfig
 * @Description TODO
 * @Date 2020/2/15 11:04
 * @Created by superning
 */
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
    @Bean
    @ConfigurationProperties("spring.datasource.slave1")
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
    @Bean
    @ConfigurationProperties("spring.datasource.slave2")
    public DataSource slave2DataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
    @Bean
    public DataSource myRouteDataSource(@Qualifier("masterDataSource") DataSource masterDatasource,
                                        @Qualifier("slave1DataSource") DataSource slave1DataSource,
                                        @Qualifier("slave2DataSource") DataSource slave2DataSource
                                        ){
        Map<Object, Object> targetHashMap = new HashMap<>();
        targetHashMap.put(DBType.MASTER,masterDatasource);
        targetHashMap.put(DBType.SLAVE1,slave1DataSource);
        targetHashMap.put(DBType.SLAVE2,slave2DataSource);
        MyRouteDataSource myRouteDataSource = new MyRouteDataSource();
        myRouteDataSource.setDefaultTargetDataSource(masterDatasource);
        myRouteDataSource.setTargetDataSources(targetHashMap);
        return myRouteDataSource;


    }




}
