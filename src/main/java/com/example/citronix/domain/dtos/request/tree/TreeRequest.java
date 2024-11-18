package com.example.citronix.domain.dtos.request.tree;

import com.example.citronix.domain.enums.TreeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TreeRequest {
    @NotNull(message = "Plantation date is required")
    private LocalDate plantationDate;

    @NotNull(message = "Tree status is required")
    private TreeStatus status;

    @NotNull(message = "Field ID is required")
    private Long fieldId;
}
