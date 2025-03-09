package project.studyproject.domain.post.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import project.studyproject.domain.post.entity.PostDoc;

import java.util.List;

public interface PostDocRepository extends ElasticsearchRepository<PostDoc, String> {

    List<PostDoc> findByTitleContainingOrContentContaining(String keyword, String keyword2);
}

