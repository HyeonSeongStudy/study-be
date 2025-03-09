package project.studyproject.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import project.studyproject.domain.post.entity.PostDoc;
import project.studyproject.domain.post.repository.PostDocRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostDocService {
    private final PostDocRepository postDocRepository;

    public PostDoc write(String title, String content) {
        PostDoc postDoc = PostDoc.builder()
                .title(title)
                .content(content)
                .build();
        return postDocRepository.save(postDoc);
    }

    public void truncate() {
        postDocRepository.deleteAll();
    }

    public List<PostDoc> findByKeyword(String keyword) {
        return postDocRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }
}