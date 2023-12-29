package com.example.ever.hare.mapper;

import com.example.ever.hare.entity.Theme;
import com.example.ever.hare.entity.dto.ThemeDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ThemeMapper {
    Theme themeDTOtoTheme(ThemeDTO themeDTO);

    List<ThemeDTO> themesToThemeDTOs(List<Theme> themes);
}
