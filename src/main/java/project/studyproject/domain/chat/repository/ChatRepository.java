package project.studyproject.domain.chat.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import project.studyproject.domain.chat.entity.Chat;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {

}
