package com.claro.proceso.migracion.pospago.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "mactpreEntityManagerFactory", transactionManagerRef = "mactpreTransactionManager",
 basePackages = "com.claro.proceso.migracion.pospago.api.repositories.mactpre")
@Slf4j
public class MactpreDB {

    @Value("${mactpre.datasource.jndi-name}")
    private String jdniName;
    @Value("${mactpre.jpa.database.platform}")
    private String dialect;

    @Autowired
    private Environment env;

    @Bean(name = "mactpreDataSource")
    @ConfigurationProperties(prefix = "mactpre.datasource")
    public DataSource dataSource() throws NamingException {
        if (StringUtils.hasText(jdniName)){
            log.info("Iniciando conexión por Jndi JPA...");
            return (DataSource) new JndiTemplate().getContext().lookup(jdniName);
        }
        log.info("Iniciando conexión por Jdbc JPA...");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mactpreEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(@Qualifier("mactpreDataSource") DataSource dataSource){
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.claro.proceso.migracion.pospago.api.entities.mactpre");
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        Map<String, Object> prop = new HashMap<>();
        prop.put("hibernate.hbm2ddl.auto", env.getProperty("mactpre.jpa.hibernate.ddl-auto"));
        prop.put("hibernate.show-sql", env.getProperty("mactpre.jpa.show-sql"));
        prop.put("hibernate.dialect", dialect);
        factoryBean.setJpaPropertyMap(prop);
        return factoryBean;
    }

    @Bean(name = "mactpreTransactionManager")
    public PlatformTransactionManager platformTransactionManager(
            @Qualifier("mactpreEntityManagerFactory")EntityManagerFactory managerFactory){
        return new JpaTransactionManager(managerFactory);
    }
}
