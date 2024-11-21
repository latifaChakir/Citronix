package com.example.citronix.service.interfaces;

import com.example.citronix.domain.dtos.request.harvestDetail.HarvestDetailRequestDto;
import com.example.citronix.domain.entity.HarvestDetail;
import com.example.citronix.domain.enums.SeasonType;
import com.example.citronix.domain.vm.HarvestDetailResponseVM;
import com.example.citronix.domain.vm.TreeResponseVM;

import java.util.List;

public interface HarvestDetailService {
    HarvestDetailResponseVM saveHarvestDetail(HarvestDetailRequestDto HarvestDetailRequestDto);
    List<HarvestDetailResponseVM> getAllHarvestDetails();
    HarvestDetailResponseVM getHarvestDetailById(Long id);
    HarvestDetailResponseVM updateHarvestDetail(Long id, HarvestDetailRequestDto HarvestDetailRequestDto);
    void deleteHarvestDetail(Long id);
    List<HarvestDetailResponseVM> getListDetail(List<TreeResponseVM> trees);


    void saveAll(List<HarvestDetail> harvestDetails);

    boolean existsByTreeAndSeason(TreeResponseVM tree, SeasonType season);
}
