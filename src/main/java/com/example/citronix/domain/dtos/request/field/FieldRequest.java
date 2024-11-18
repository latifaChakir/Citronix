package com.example.citronix.domain.dtos.request.field;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FieldRequest {
    @NotBlank(message = "Field name is required")
    private String name;

    @NotNull(message = "Field area is required")
    @Min(value = 1000, message = "Field area must be at least 1,000 m²")
    private Double area;

    @NotNull(message = "Farm ID is required")
    private Long farmId;
}
