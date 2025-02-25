package project.studyproject.global.db.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "",
        entityManagerFactoryRef = "mongoEntityManager",
        transactionManagerRef = "mongoTransaction"
)
public class MongoDataBaseConfig {
    @Bean
    public PlatformTransactionManager mongoTransaction() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mongoEntityManger().getObject());

        return transactionManager;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean mongoEntityManger() {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(mongoDataSource());
        em.setPackagesToScan(new String[]{""}); // 경로 Entity 주소
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        em.setJpaPropertyMap(properties);

        return em;
    }


    @Bean
    public DataSource mongoDataSource(){
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/studyproject")
                .username("root")
                .password("root")
                .build();
    }
}

