package com.example.citronix.domain.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "sales")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private Double unitPrice;
    private String client;
    private Double revenue;
    @ManyToOne
    @JoinColumn(name = "harvest_id")
    private Harvest harvest;
}
