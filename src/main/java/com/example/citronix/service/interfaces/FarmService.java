package com.example.citronix.service.interfaces;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.dtos.request.farm.FarmSearchCriteriaDto;
import com.example.citronix.domain.vm.FarmResponseVM;

import java.util.List;

public interface FarmService {
    FarmResponseVM createFarm(CreateFarmRequestDto farmRequestDto);
    List<FarmResponseVM> getAllFarms();
    FarmResponseVM getFarmById(Long id);
    FarmResponseVM updateFarm(Long id, CreateFarmRequestDto farmRequestDto);
    void deleteFarm(Long id);
    List<FarmResponseVM> searchFarms(FarmSearchCriteriaDto searchCriteria);

}
