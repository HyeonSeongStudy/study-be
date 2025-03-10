package project.studyproject.domain.post.article.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.studyproject.domain.post.article.dto.PostRequest;
import project.studyproject.domain.post.article.entity.Post;
import project.studyproject.domain.post.article.service.PostService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/post")
@Slf4j
public class ApiV1PostController {
    private final PostService postService;

    @PostMapping("/write")
    public ResponseEntity<Post> write(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.write(postRequest.title(),postRequest.content()));

    }

    @GetMapping
    public String home() {
        LocalDateTime now = LocalDateTime.now();
        log.info("[로그 수집 테스트] : {}", now);
        return "Home" + now;
    }

    @GetMapping("/logs")
    public String Logs() {
        LocalDateTime localDateTime = LocalDateTime.now();
        log.info("Logs " + localDateTime);
        return "Logs page";
    }

    @GetMapping("/warn")
    public String Warn() {
        LocalDateTime localDateTime = LocalDateTime.now();
        log.warn("Warn" + localDateTime);
        return "Warn page";
    }


    @GetMapping("/err")
    public String Error() {
        LocalDateTime localDateTime = LocalDateTime.now();
        log.error("Error " + localDateTime);
        return "Error page";
    }
}
