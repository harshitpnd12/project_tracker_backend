package com.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.projectmanagement.model.Message;

@RestController
@CrossOrigin(origins = "https://project-tracker-frontend-three.vercel.app", allowCredentials = "true")
public class RealTimeChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{groupId}")
    public Message sendToUser(@Payload Message message,
            @DestinationVariable String groupId) throws Exception {
        simpMessagingTemplate.convertAndSendToUser(groupId, "/private",
                message);
        return message;
    }
}
