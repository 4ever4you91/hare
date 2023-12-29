package com.example.ever.hare.controller;

import com.example.ever.hare.entity.Role;
import com.example.ever.hare.entity.dto.OptionDTO;
import com.example.ever.hare.entity.dto.SessionDTO;
import com.example.ever.hare.entity.dto.ThemeDTO;
import com.example.ever.hare.entity.dto.UsersDTO;
import com.example.ever.hare.service.HareDAOService;
import com.example.ever.hare.service.HareWSService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController()
public class HareController {

    private final SimpUserRegistry simpUserRegistry;

    private final HareDAOService hareDAOService;
    private final HareWSService hareWSService;

    private final Random random;


    @MessageMapping("/play.notifyAboutPresence")
    public void notifyLeaderAboutPresence(@Payload UsersDTO usersDTO, StompHeaderAccessor stompHeaderAccessor) {
        stompHeaderAccessor.getSessionAttributes().put("user", usersDTO);
        hareWSService.sendCreatedUserToPerson(usersDTO.getId(), usersDTO);
    }

    @MessageMapping("/play.exchangeUserInfo")
    public void saveUserIntoPlaySession(@Payload UsersDTO usersDTO, StompHeaderAccessor stompHeaderAccessor) {
        stompHeaderAccessor.getSessionAttributes().put("user", usersDTO);
    }

    @MessageMapping("/play.generateRound")
    public void generateRound(@Payload ThemeDTO themeDTO, StompHeaderAccessor stompHeaderAccessor) {

        List<OptionDTO> options = hareDAOService.getOptionsByTheme(themeDTO);

        UsersDTO user = (UsersDTO) stompHeaderAccessor.getSessionAttributes().get("user");
        Set<UsersDTO> users = hareDAOService.getUsersBySessionId(user.getSession().getId());
        List<UsersDTO> activeUsers = users.stream()
                .filter(UsersDTO::getIsActive)
                .collect(Collectors.toList());

        int randomUserNumber = random.nextInt(activeUsers.size());
        int hareOptionNumber = random.nextInt(options.size());

        UsersDTO userHare = activeUsers.remove(randomUserNumber);

        hareWSService.sendOptionsToAll(activeUsers, Map.of("options", options, "guessNumber", hareOptionNumber));
        hareWSService.sendOptionsToPerson(userHare.getId(), Map.of("options", options, "guessNumber", ""));

        hareDAOService.addSessionThemeOptionAudit(user.getSession().getId(), themeDTO.getId(), options.get(hareOptionNumber).getId());
    }

    @MessageMapping("/play.updateUser")
    public void updateUser(@Payload UsersDTO user) {
        hareDAOService.createOrUpdateUser(user);
    }


    @PostMapping("/generateSession")
    public UsersDTO generateSession(@RequestBody() UsersDTO usersDTO) {
        SessionDTO sessionDTO = SessionDTO.builder()
                .creationDateTime(LocalDateTime.now())
                .build();
        usersDTO.setSession(sessionDTO);
        return hareDAOService.createOrUpdateUser(usersDTO);
    }

    @PostMapping("/user")
    public UsersDTO createUser(@RequestBody UsersDTO user) {
        UsersDTO savedUser = hareDAOService.saveUserInSession(user);
        UUID sessionId = savedUser.getSession().getId();
        UsersDTO leader = hareDAOService.getLeaderBySessionId(sessionId);
        hareWSService.sendCreatedUserToPerson(leader.getId(), savedUser);
        return savedUser;
    }

    @GetMapping("/themes")
    public List<ThemeDTO> getThemes() {
        return hareDAOService.findThemes();
    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        if (event.getCloseStatus().getCode() == 1000) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
            UsersDTO user = (UsersDTO) headerAccessor.getSessionAttributes().get("user");
            if (Role.USER.equals(user.getRole())) {
                UsersDTO leader = hareDAOService.getLeaderBySessionId(user.getSession().getId());
                hareWSService.sendUserExitNotification(leader.getId(), user);
            }
            if (Role.LEADER.equals(user.getRole())) {
                hareDAOService.getUsersBySessionId(user.getSession().getId()).stream()
                        .filter(UsersDTO::getIsActive)
                        .forEach(u -> hareWSService.sendLeaderExitNotification(u.getId(), "EXIT"));
            }

        }
    }
}

