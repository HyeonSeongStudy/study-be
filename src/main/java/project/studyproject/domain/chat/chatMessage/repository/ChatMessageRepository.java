package project.studyproject.domain.chat.chatMessage.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import project.studyproject.domain.chat.chatMessage.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {
}
