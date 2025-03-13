package project.studyproject.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.studyproject.domain.post.dto.PostDocWriteRequest;
import project.studyproject.domain.post.entity.PostDoc;
import project.studyproject.domain.post.service.PostDocService;

import java.util.List;

@RequestMapping("/api/v1/post")
@RestController
@RequiredArgsConstructor
public class ApiV1PostDocController {
    private final PostDocService postDocService;

    @PostMapping("/write")
    public ResponseEntity<PostDoc> write(@RequestBody @Validated PostDocWriteRequest request)
    {
        return ResponseEntity.ok(postDocService.write(request.title(), request.content()));
    }

    @GetMapping("/searchJpa")
    public ResponseEntity<List<PostDoc>> find(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok(postDocService.findByKeyword(keyword));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDoc>> search(@RequestParam("keyword") String keyword){
        return ResponseEntity.ok(postDocService.searchByKeyword(keyword));
    }


}