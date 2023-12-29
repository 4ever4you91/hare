package com.example.ever.hare.entity.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString(exclude = "options")
@Data
public class ThemeDTO {
    private Long id;
    private String themeName;
    private List<OptionDTO> options;
}
