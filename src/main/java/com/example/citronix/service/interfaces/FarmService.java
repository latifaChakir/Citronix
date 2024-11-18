package com.example.citronix.service.interfaces;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.dtos.response.FarmResponseDto;

import java.util.List;

public interface FarmService {
    FarmResponseDto createFarm(CreateFarmRequestDto farmRequestDto);
    List<FarmResponseDto> getAllFarms();
    FarmResponseDto getFarmById(Long id);
    FarmResponseDto updateFarm(Long id, CreateFarmRequestDto farmRequestDto);
    void deleteFarm(Long id);
}
