package project.studyproject.domain.chat.chatMessage.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.studyproject.domain.chat.chatMessage.repository.ChatMessageRepository;
import project.studyproject.domain.chat.chatMessage.service.ChatMessageService;

@RequiredArgsConstructor
@Service
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
}
