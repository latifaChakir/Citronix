package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.domain.entity.Farm;
import com.example.citronix.domain.mapper.FarmMapper;
import com.example.citronix.repository.FarmRepository;
import com.example.citronix.service.interfaces.FarmService;
import com.example.citronix.shared.exception.FarmNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {
    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;
    @Override
    public FarmResponseVM createFarm(CreateFarmRequestDto farmRequestDto) {
        Farm farm = farmMapper.toEntity(farmRequestDto);
        Farm savedFarm = farmRepository.save(farm);
        return farmMapper.toDto(savedFarm);
    }

    @Override
    public List<FarmResponseVM> getAllFarms() {
        List<Farm> farms = farmRepository.findAll();
        return farmMapper.toDtoList(farms);
    }

    @Override
    public FarmResponseVM getFarmById(Long id) {
        Optional<Farm> farm = farmRepository.findById(id);
        if (farm.isEmpty()) {
            throw new FarmNotFoundException("Farm not found");
        }
        return farmMapper.toDto(farm.get());
    }

    @Override
    public FarmResponseVM updateFarm(Long id, CreateFarmRequestDto farmRequestDto) {
        return null;
    }

    @Override
    public void deleteFarm(Long id) {

    }
}
