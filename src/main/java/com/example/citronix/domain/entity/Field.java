package com.example.citronix.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "fields")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double area;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tree> trees;
}
