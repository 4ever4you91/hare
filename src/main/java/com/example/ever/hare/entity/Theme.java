package com.example.ever.hare.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Theme {
    @Id
    private Long id;
    private String themeName;
    @OneToMany(mappedBy = "theme")
    private List<SessionThemeOption> sessionThemeOptions;
    @OneToMany(mappedBy = "theme", fetch = FetchType.EAGER)
    private List<Option> options;
}
