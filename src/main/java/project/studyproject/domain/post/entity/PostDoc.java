package project.studyproject.domain.post.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName="app1_posts")
@Getter
@Setter
@Builder
public class PostDoc {
    @Id
    private String id;
    private String title;
    private String content;
}
