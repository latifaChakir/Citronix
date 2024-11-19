package com.example.citronix.domain.vm;

import lombok.*;

import java.time.LocalDate;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FarmResponseVM {
    private Long id;
    private String name;
    private String location;
    private Double area;
    private LocalDate creationDate;
    private int fieldCount;
}
