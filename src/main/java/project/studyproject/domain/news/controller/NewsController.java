package project.studyproject.domain.news.controller;


import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.UUIDValidator;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Slf4j
public class NewsController {
    //    public CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    public Map<String, SseEmitter> emitters = new HashMap();

    // 현재 특정 유저에게만 보내지게 만듬
    @CrossOrigin
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() throws IOException {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sendEvent(emitter);

        String userId = UUID.randomUUID().toString();
        log.info("[userId 추출] : {}", userId);

        emitters.put(userId, emitter);

        emitter.onCompletion(()-> emitters.remove(emitter));
        emitter.onTimeout(()-> emitters.remove(emitter));
        emitter.onError((e)-> emitters.remove(emitter));

//        emitters.add(emitter); // List

        return emitter;
    };

//     all Client
    @PostMapping("/dispatchEvent")
    public void dispatchEvent(@RequestParam("title") String title,
                              @RequestParam("content") String content,
                              SseEmitter emitter) throws IOException {
        String fresh = new JSONObject()
                .put("title", title)
                .put("content", content)
                .toString();


        emitter.send(SseEmitter.event().name("latest").data(fresh));

    }

    // specific Client
    @PostMapping("/dispatchEventToSpecificUser")
    public void dispatchEvent(@RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("userId") String userId) throws IOException {
        String fresh = new JSONObject()
                .put("title", title)
                .put("content", content)
                .toString();

        SseEmitter emitter = emitters.get(userId);

        if (emitter != null) {
            emitter.send(SseEmitter.event().name("latest").data(fresh));
        }
    }

    public void sendEvent(SseEmitter emitter) throws IOException {
        emitter.send(SseEmitter.event().name("Init"));
    }
}
