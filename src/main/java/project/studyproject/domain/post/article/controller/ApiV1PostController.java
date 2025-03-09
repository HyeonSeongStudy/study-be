package project.studyproject.domain.post.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.studyproject.domain.post.article.service.PostService;

@RestController
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;

    @PostMapping("/write")
    public String write() {
        postService.write("테스트 제목", "테스트 내용");
        return "작성완료";
    }
}
