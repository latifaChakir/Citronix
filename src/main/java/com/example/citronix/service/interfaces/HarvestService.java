package com.example.citronix.service.interfaces;

import com.example.citronix.domain.dtos.request.harvest.HarvestRequestDto;
import com.example.citronix.domain.vm.HarvestResponseVM;

import java.util.List;

public interface HarvestService {
    HarvestResponseVM saveHarvest(HarvestRequestDto harvestRequestDto);
    List<HarvestResponseVM> getAllHarvests();
    HarvestResponseVM getHarvestById(Long id);
    HarvestResponseVM updateHarvest(Long id, HarvestRequestDto harvestRequestDto);
    void deleteHarvest(Long id);

}
