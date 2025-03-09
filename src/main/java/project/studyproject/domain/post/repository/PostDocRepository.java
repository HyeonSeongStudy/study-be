package project.studyproject.domain.post.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import project.studyproject.domain.post.entity.PostDoc;

import java.util.List;

public interface PostDocRepository extends ElasticsearchRepository<PostDoc, String> {

    List<PostDoc> findByTitleContainingOrContentContaining(String keyword, String keyword2);

    @Query("""
         {
             "bool": {
                 "should": [
                     {
                         "match": {
                             "title": "?0"
                         }
                     },
                     {
                         "match": {
                             "content": "?0"
                         }
                     }
                 ]
             }
         }
     """)
    List<PostDoc> searchByKeyword(String keyword);
}

