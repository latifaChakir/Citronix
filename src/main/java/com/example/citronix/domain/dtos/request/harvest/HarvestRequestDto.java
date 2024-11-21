package com.example.citronix.domain.dtos.request.harvest;

import com.example.citronix.domain.dtos.request.field.FramWithFieldRequestdTO;
import com.example.citronix.domain.dtos.request.harvestDetail.HarvestDetailRequestDto;
import com.example.citronix.domain.enums.SeasonType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HarvestRequestDto {

    @NotNull(message = "Harvest date is required")
    private LocalDate harvestDate;

    @NotNull(message = "Field ID is required")
    private Long fieldId;

}
