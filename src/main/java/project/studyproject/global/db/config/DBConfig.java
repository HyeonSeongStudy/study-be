package project.studyproject.global.db.config;

import org.hibernate.annotations.Bag;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

// DB를 yml 파일이 아닌 클래스 파일 형태로 저장할 때
@Configuration
public class DBConfig {

    @Bean
    public DataSource getDataSource(){
        return DataSourceBuilder.create()
                .driverClassName("")
                .url("jdbc:mysql://localhost:3306/studyproject")
                .username("root")
                .password("123456")
                .build();
    }

}
