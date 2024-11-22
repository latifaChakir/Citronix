package com.example.citronix.repository;

import com.example.citronix.domain.entity.Harvest;
import com.example.citronix.domain.entity.HarvestDetail;
import com.example.citronix.domain.enums.SeasonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HarvestDetailRepository extends JpaRepository<HarvestDetail,Long> {
    @Query("SELECT CASE WHEN COUNT(hd) > 0 THEN TRUE ELSE FALSE END " +
            "FROM HarvestDetail hd " +
            "WHERE hd.tree.id = :treeId AND hd.harvest.season = :season")
    boolean existsByTreeIdAndSeason(@Param("treeId") Long treeId, @Param("season") SeasonType season);
}
