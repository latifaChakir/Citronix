package com.example.citronix.domain.dtos.request.farm;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateFarmRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Location is required")
    private String location;
    @Min(value = 1000, message = "Area must be at least 1000 m²")
    private Double area;
    @NotNull(message = "Creation date is required")
    private LocalDate creationDate;
}
