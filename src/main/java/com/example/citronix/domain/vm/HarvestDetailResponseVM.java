package com.example.citronix.domain.vm;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HarvestDetailResponseVM {
    private Long id;
    private Double quantity;
    private Long harvestId;
    private Long treeId;

}
