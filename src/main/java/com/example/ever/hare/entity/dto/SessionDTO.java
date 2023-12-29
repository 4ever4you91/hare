package com.example.ever.hare.entity.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class SessionDTO {
    private UUID id;
    private LocalDateTime creationDateTime;
}
