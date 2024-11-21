package com.example.citronix.domain.dtos.request.harvest;

import com.example.citronix.domain.enums.SeasonType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HarvestRequestDto {
    @NotNull(message = "Season is required")
    private SeasonType season;

    @NotNull(message = "Harvest date is required")
    private LocalDate harvestDate;

    @NotNull(message = "Total quantity is required")
    @Min(value = 1, message = "Total quantity must be greater than zero")
    private Double totalQuantity;

    @NotNull(message = "Field ID is required")
    private Long fieldId;
}
