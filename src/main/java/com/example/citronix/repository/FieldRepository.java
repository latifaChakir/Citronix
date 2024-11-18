package com.example.citronix.repository;

import com.example.citronix.domain.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, Long> {
}
