package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.dtos.response.FarmResponseDto;
import com.example.citronix.domain.entity.Farm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FarmMapper {
    Farm toEntity(CreateFarmRequestDto dto);
    @Mapping(source = "fields.size", target = "fieldCount")
    FarmResponseDto toDto(Farm farm);

    List<FarmResponseDto> toDtoList(List<Farm> farms);
}
