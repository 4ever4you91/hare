package com.example.ever.hare.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Session {
    @Id
    @GeneratedValue(generator = "uuid-session-generator")
    @GenericGenerator(name = "uuid-session-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private LocalDateTime creationDateTime;
    @OneToMany(mappedBy = "session")
    private List<SessionThemeOption> sessionThemeOptions;
    @OneToMany(mappedBy = "session", cascade = CascadeType.PERSIST)
    private List<Users> users;
}
