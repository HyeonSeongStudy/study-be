package project.studyproject.domain.post.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.studyproject.domain.post.article.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
