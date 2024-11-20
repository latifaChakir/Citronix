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
    @Transient
    public int getAge() {
        return plantationDate != null ? LocalDate.now().getYear() - plantationDate.getYear() : 0;
    }
    @Transient
    public Double getProductivity() {
        long age = this.getAge();
        if (age < 3) {
            return 2.5;
        } else if (age <= 10) {
            return 12.0;
        } else {
            return 20.0;
        }
    }
    @Transient
    public TreeStatus calculateStatus() {
        int age = this.getAge();
        if (age > 20) {
            return TreeStatus.NON_PRODUCTIVE;
        } else if (age > 10) {
            return TreeStatus.OLD;
        } else if (age >= 3) {
            return TreeStatus.MATURE;
        } else {
            return TreeStatus.YOUNG;
        }
    }

}
