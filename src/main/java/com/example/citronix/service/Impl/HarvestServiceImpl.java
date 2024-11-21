package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.harvest.HarvestRequestDto;
import com.example.citronix.domain.vm.HarvestResponseVM;
import com.example.citronix.service.interfaces.HarvestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HarvestServiceImpl implements HarvestService {
    @Override
    public HarvestResponseVM saveHarvest(HarvestRequestDto harvestRequestDto) {
        return null;
    }

    @Override
    public List<HarvestResponseVM> getAllHarvests() {
        return List.of();
    }

    @Override
    public HarvestResponseVM getHarvestById(Long id) {
        return null;
    }

    @Override
    public HarvestResponseVM updateHarvest(Long id, HarvestRequestDto harvestRequestDto) {
        return null;
    }

    @Override
    public void deleteHarvest(Long id) {

    }
}
