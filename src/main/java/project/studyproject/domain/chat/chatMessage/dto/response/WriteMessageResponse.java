package project.studyproject.domain.chat.chatMessage.dto.response;

import project.studyproject.domain.chat.chatMessage.entity.ChatMessage;

import java.util.List;

public record WriteMessageResponse(
        List<ChatMessage> chatMessageList
) {
}
