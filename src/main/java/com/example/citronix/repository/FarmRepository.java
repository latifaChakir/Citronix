package com.example.citronix.repository;

import com.example.citronix.domain.entity.Farm;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FarmRepository extends JpaRepository<Farm, Long>, JpaSpecificationExecutor<Farm> {
    boolean existsByNameAndLocation(String name, String location);
}
