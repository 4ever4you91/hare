package com.example.ever.hare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "theme")
@Data
@Builder
@Entity
public class Option {
    @Id
    private Long id;
    private String optionName;
    @OneToMany(mappedBy = "option")
    private List<SessionThemeOption> sessionThemeOptions;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;
}
