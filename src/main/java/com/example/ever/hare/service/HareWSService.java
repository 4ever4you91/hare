package com.example.ever.hare.service;

import com.example.ever.hare.entity.dto.UsersDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
public class HareWSService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final String OPTIONS_PRIVATE_CHANNEL = "/topic/private/options/";
    private final String USERS_PRIVATE_CHANNEL = "/topic/private/users/";
    private final String LEADER_EXIT_NOTIFICATION_PRIVATE_CHANNEL = "/topic/private/leader-exit/";
    private final String USER_EXIT_NOTIFICATION_PRIVATE_CHANNEL = "/topic/private/user-exit/";

    public void sendOptionsToAll(Collection<UsersDTO> users, Object payload) {
        users.forEach((user) -> sendOptionsToPerson(user.getId(), payload));
    }

    public void sendOptionsToPerson(Long id, Object payload) {
        simpMessagingTemplate.convertAndSend(OPTIONS_PRIVATE_CHANNEL + id, payload);
    }

    public void sendLeaderExitNotification(Long id, Object payload) {
        simpMessagingTemplate.convertAndSend(LEADER_EXIT_NOTIFICATION_PRIVATE_CHANNEL + id, payload);
    }

    public void sendCreatedUserToPerson(Long id, Object payload) {
        simpMessagingTemplate.convertAndSend(USERS_PRIVATE_CHANNEL + id, payload);
    }

    public void sendUserExitNotification(Long id, Object payload) {
        simpMessagingTemplate.convertAndSend(USER_EXIT_NOTIFICATION_PRIVATE_CHANNEL + id, payload);
    }
}
