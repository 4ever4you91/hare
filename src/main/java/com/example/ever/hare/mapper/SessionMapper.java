package com.example.ever.hare.mapper;

import com.example.ever.hare.entity.Session;
import com.example.ever.hare.entity.Theme;
import com.example.ever.hare.entity.dto.SessionDTO;
import com.example.ever.hare.entity.dto.ThemeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SessionMapper {
    @Mapping(target = "id", source = "session.id")
    @Mapping(target = "creationDateTime", source = "session.creationDateTime")
    SessionDTO sessionToSessionDTO(Session session);

    Session sessionDTOToSession(SessionDTO sessionDTO);

    List<ThemeDTO> themesToThemeDTOs(List<Theme> themes);

}
