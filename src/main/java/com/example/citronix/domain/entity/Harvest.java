package com.example.citronix.domain.entity;
import com.example.citronix.domain.enums.SeasonType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "harvests")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Harvest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SeasonType season;

    private LocalDate harvestDate;
    private Double totalQuantity;

    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HarvestDetail> harvestDetails;

    public void calculateTotalQuantity() {
        this.totalQuantity = harvestDetails.stream().mapToDouble(HarvestDetail::getQuantity).sum();
    }
}
