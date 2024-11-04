package com.claro.proceso.migracion.pospago.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.naming.NamingException;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@Slf4j
public class JdbcDBsConfig {

    @Value("${bscs8.datasource.jndi-name}")
    private String jndiBscs8Name;
    @Value("${crmoe.datasource.jndi-name}")
    private String jndiCrmoeName;
    @Bean(name = "bscsDataSource")
    @ConfigurationProperties(prefix = "bscs8.datasource")
    public DataSource bscsDataSource() throws NamingException {
        if (StringUtils.hasText(jndiBscs8Name)){
            log.info("Iniciando conexión por jndi...");
            return (DataSource) new JndiTemplate().getContext().lookup(jndiBscs8Name);
        }
        log.info("Iniciando conexión por jdbc...");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "crmoeDataSource")
    @ConfigurationProperties(prefix = "crmoe.datasource")
    public DataSource crmoeDataSource() throws NamingException {
        if (StringUtils.hasText(jndiCrmoeName)){
            log.info("Iniciando conexión por jndi...");
            return (DataSource) new JndiTemplate().getContext().lookup(jndiCrmoeName);
        }
        log.info("Iniciando conexión por jdbc...");
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate bscsJdbcTemplate(@Qualifier("bscsDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public JdbcTemplate crmoeJdbcTemplate(@Qualifier("crmoeDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
