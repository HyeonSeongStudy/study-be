package project.studyproject.domain.post.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.studyproject.domain.post.article.dto.PostRequest;
import project.studyproject.domain.post.article.entity.Post;
import project.studyproject.domain.post.article.service.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/post")
public class ApiV1PostController {
    private final PostService postService;

    @PostMapping("/write")
    public ResponseEntity<Post> write(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.write(postRequest.title(),postRequest.content()));

    }
}
