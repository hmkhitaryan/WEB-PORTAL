package com.egs.account.config.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
	    final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
	    sessionFactory.setDataSource(dataSource());
	    sessionFactory.setPackagesToScan("com.egs.account.model");
	    sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
     }
	
    @Bean
    public DataSource dataSource() {
	    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));

        return dataSource;
    }

	public Properties hibernateProperties() {
		final Properties properties = new Properties();
		properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));

        return properties;        
    }

	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory s) {
		final HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(s);

		return txManager;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer() {
	    final ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
	    resourceDatabasePopulator.addScript(new ClassPathResource("/data.sql"));

	    final DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
	    dataSourceInitializer.setDataSource(dataSource());
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);

	    return dataSourceInitializer;
    }
}

