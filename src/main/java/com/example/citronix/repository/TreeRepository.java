package com.example.citronix.repository;

import com.example.citronix.domain.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeRepository extends JpaRepository<Tree,Long> {
    List<Tree> findByFieldId(Long fieldId);
}
