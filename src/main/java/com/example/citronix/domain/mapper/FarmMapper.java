package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.domain.entity.Farm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FarmMapper {
    Farm toEntity(CreateFarmRequestDto dto);
    @Mapping(target = "fields", source = "farm.fields")
    FarmResponseVM toDto(Farm farm);
    @Mapping(target = "fields", source = "farm.fields")
    List<FarmResponseVM> toDtoList(List<Farm> farms);
}
