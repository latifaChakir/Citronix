package com.example.citronix.domain.dtos.request.sale;

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
public class SaleRequestDto {
    @NotNull(message = "Sale date is required")
    private LocalDate date;

    @NotNull(message = "Unit price is required")
    @Min(value = 1, message = "Unit price must be greater than zero")
    private Double unitPrice;

    @NotBlank(message = "Client is required")
    private String client;

    @NotNull(message = "Revenue is required")
    @Min(value = 1, message = "Revenue must be greater than zero")
    private Double revenue;

    @NotNull(message = "Harvest ID is required")
    private Long harvestId;
}
