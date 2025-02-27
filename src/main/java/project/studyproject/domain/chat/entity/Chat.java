package project.studyproject.domain.chat.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "test")
@Data
public class Chat {

    @Id
    private String id;

    private String data;
}
