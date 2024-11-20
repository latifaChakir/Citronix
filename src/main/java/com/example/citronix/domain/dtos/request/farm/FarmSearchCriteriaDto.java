package com.example.citronix.domain.dtos.request.farm;

import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FarmSearchCriteriaDto {
    private String name;
    private String location;
    private Double area;
    private LocalDate creationDate;;
}
