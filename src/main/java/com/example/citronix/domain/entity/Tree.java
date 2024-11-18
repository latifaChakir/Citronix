package com.example.citronix.domain.entity;

import com.example.citronix.domain.enums.TreeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "trees")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate plantationDate;

    @Enumerated(EnumType.STRING)
    private TreeStatus status;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;
}
