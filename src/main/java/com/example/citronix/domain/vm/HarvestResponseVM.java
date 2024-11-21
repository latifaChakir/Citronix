package com.example.citronix.domain.vm;

import com.example.citronix.domain.enums.SeasonType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HarvestResponseVM {
    private Long id;
    private SeasonType season;
    private LocalDate harvestDate;
    private Double totalQuantity;
    private List<HarvestDetailResponseVM> harvestDetails;

}
