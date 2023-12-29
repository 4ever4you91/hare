package com.example.ever.hare.service;

import com.example.ever.hare.entity.*;
import com.example.ever.hare.entity.dto.OptionDTO;
import com.example.ever.hare.entity.dto.ThemeDTO;
import com.example.ever.hare.entity.dto.UsersDTO;
import com.example.ever.hare.mapper.OptionMapper;
import com.example.ever.hare.mapper.SessionMapper;
import com.example.ever.hare.mapper.ThemeMapper;
import com.example.ever.hare.mapper.UsersMapper;
import com.example.ever.hare.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class HareDAOService {

    private final SessionMapper sessionMapper;
    private final UsersMapper usersMapper;
    private final ThemeMapper themeMapper;
    private final OptionMapper optionMapper;
    private final SessionRepository sessionRepository;
    private final UsersRepository usersRepository;
    private final ThemeRepository themeRepository;
    private final OptionRepository optionRepository;
    private final SessionThemeOptionRepository sessionThemeOptionRepository;

    public UsersDTO createOrUpdateUser(UsersDTO usersDTO) {
        return usersMapper.usersToUsersDTO(usersRepository.save(usersMapper.usersDTOToUsers(usersDTO)));
    }

    public List<OptionDTO> getOptionsByTheme(ThemeDTO themeDTO) {
        return optionMapper.toOptionDTOList(optionRepository.findOptionsByTheme(themeMapper.themeDTOtoTheme(themeDTO)));
    }

    public UsersDTO getLeaderBySessionId(UUID uuid) {
        return usersMapper.usersToUsersDTO(usersRepository.findUsersBySessionIdAndRole(uuid, Role.LEADER));
    }

    public Set<UsersDTO> getUsersBySessionId(UUID uuid) {
        return usersMapper.toUsersDTOSet(usersRepository.findUsersBySessionId(uuid));
    }

    public List<ThemeDTO> findThemes() {
        return themeMapper.themesToThemeDTOs(themeRepository.findAll());
    }

    public void addSessionThemeOptionAudit(UUID sessionId, Long themeId, Long optionId) {
        Session session = Session.builder()
                .id(sessionId)
                .build();

        Theme theme = Theme.builder()
                .id(themeId)
                .build();

        Option option = Option.builder()
                .id(optionId)
                .build();

        sessionThemeOptionRepository.save(SessionThemeOption.builder()
                .session(session)
                .theme(theme)
                .option(option)
                .creationDateTime(LocalDateTime.now())
                .build());

    }

    public UsersDTO saveUserInSession(UsersDTO usersDTO) {
        Session session = sessionRepository.findById(usersDTO.getSession().getId())
                .orElseThrow(() -> new IllegalArgumentException("SessionId doesn't exist"));
        Users user = usersMapper.usersDTOToUsers(usersDTO);
        user.setSession(session);
        return usersMapper.usersToUsersDTO(usersRepository.save(user));
    }
}
