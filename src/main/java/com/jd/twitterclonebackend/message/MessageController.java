package com.jd.twitterclonebackend.message;

import com.jd.twitterclonebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable("to") String to,
                            MessageDto messageDto) {
        System.out.println("handling send message" + messageDto + " to: " + to);

        simpMessagingTemplate.convertAndSend(
                "/topic/messages/" + to,
                messageDto
        );

    }


}
