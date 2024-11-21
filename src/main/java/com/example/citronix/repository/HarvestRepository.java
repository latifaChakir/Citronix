package com.example.citronix.repository;

import com.example.citronix.domain.entity.Harvest;
import com.example.citronix.domain.enums.SeasonType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    boolean existsBySeason(SeasonType season);
}
