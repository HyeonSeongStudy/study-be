package project.studyproject.domain.post.article.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.studyproject.domain.post.article.entity.Post;
import project.studyproject.domain.post.article.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    public void write(String title, String content) {
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();

        postRepository.save(post);
    }

    public void truncate() {
        postRepository.deleteAll();
    }
}
