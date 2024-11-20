package com.example.citronix.repository;

import com.example.citronix.domain.entity.Farm;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm, Long> {
    boolean existsByNameAndLocation(String name, String location);
}
