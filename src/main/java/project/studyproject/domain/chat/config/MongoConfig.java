package project.studyproject.domain.chat.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "project.studyproject.domain.chat.repository",
        mongoTemplateRef = "mongoTemplate"
)
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {

        return MongoClients.create("mongodb+srv://Hyeonseong:mongo1223@test.kkx6e.mongodb.net/testdb?retryWrites=true&w=majority&appName=Test");
    }

    @Bean
    public MongoDatabaseFactory databaseFactory() {

        return new SimpleMongoClientDatabaseFactory(mongoClient(), "testdb");
    }

    @Bean
    public MongoTemplate mongoTemplate() {

        return new MongoTemplate(databaseFactory());
    }
}
