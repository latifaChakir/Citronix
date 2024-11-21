package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.harvest.HarvestRequestDto;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.entity.Harvest;
import com.example.citronix.domain.vm.FieldResponseVM;
import com.example.citronix.domain.vm.HarvestResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface HarvestMapper {
    @Mapping(target = "field.id", source = "fieldId")

    Harvest toEntity(HarvestRequestDto harvestRequestDto);

    @Mapping(target = "fieldId", source = "field.id")
    @Mapping(target = "field", source = "field")
    HarvestResponseVM toDTO(Harvest savedHarvest);

    List<HarvestResponseVM> toDtoList(List<Harvest> harvestResponses);
}
