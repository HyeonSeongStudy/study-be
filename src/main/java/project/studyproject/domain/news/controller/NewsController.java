package project.studyproject.domain.news.controller;


import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    public CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @CrossOrigin
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() throws IOException {
        SseEmitter emitter = new SseEmitter();
        emitter.send(SseEmitter.event().name("Init"));

        emitter.onCompletion(()-> emitters.remove(emitter));

        emitters.add(emitter);

        return emitter;
    };

    @PostMapping("/dispatchEvent")
    public void dispatchEvent(@RequestParam("title") String title,
                              @RequestParam("content") String content) throws IOException {
        String fresh = new JSONObject()
                .put("title", title)
                .put("content", content)
                .toString();

        for (SseEmitter emitter : emitters) {
            emitter.send(SseEmitter.event().name("latest").data(fresh));
        }
    }
}
