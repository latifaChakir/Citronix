package com.example.citronix.domain.vm;

import com.example.citronix.domain.enums.TreeStatus;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreeResponseVM {
    private LocalDate plantationDate;
    private TreeStatus status;
    private Long fieldId;
    private int age;
    private Double productivity;



}
