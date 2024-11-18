package com.example.citronix.domain.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "harvest_details")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HarvestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "harvest_id")
    private Harvest harvest;

    @ManyToOne
    @JoinColumn(name = "tree_id")
    private Tree tree;
}
