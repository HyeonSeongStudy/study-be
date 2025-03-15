package project.studyproject.domain.chat.chatMessage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.studyproject.domain.chat.chatMessage.service.ChatMessageService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ApiV1ChatMessageController {
    private final ChatMessageService chatMessageService;

}
