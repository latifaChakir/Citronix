package com.example.citronix.repository;

import com.example.citronix.domain.entity.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarvestRepository extends JpaRepository<Harvest, Long> {
}
