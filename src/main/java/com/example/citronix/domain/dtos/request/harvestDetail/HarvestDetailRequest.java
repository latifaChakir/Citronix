package com.example.citronix.domain.dtos.request.harvestDetail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class HarvestDetailRequest {
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than zero")
    private Double quantity;

    @NotNull(message = "Harvest ID is required")
    private Long harvestId;

    @NotNull(message = "Tree ID is required")
    private Long treeId;
}
