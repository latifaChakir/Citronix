package com.example.citronix.domain.vm;

import com.example.citronix.domain.entity.Harvest;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponseVM {
    private Long id;
    private LocalDate date;
    private Double unitPrice;
    private String client;
    private HarvestResponseVM harvest;
    private Double revenue;
}
