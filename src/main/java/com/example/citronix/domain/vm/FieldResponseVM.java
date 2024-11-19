package com.example.citronix.domain.vm;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldResponseVM {
    private Long id;
    private String name;
    private Double area;
}
