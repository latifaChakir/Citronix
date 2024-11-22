package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.harvest.HarvestRequestDto;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.entity.Harvest;
import com.example.citronix.domain.vm.FieldResponseVM;
import com.example.citronix.domain.vm.HarvestResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = HarvestDetailMapper.class)

public interface HarvestMapper {

    Harvest toEntity(HarvestRequestDto harvestRequestDto);
    @Mapping(target = "harvestDetails", source = "harvestDetails")
    HarvestResponseVM toDTO(Harvest savedHarvest);
    @Mapping(target = "harvestDetails", source = "harvestDetails")

    List<HarvestResponseVM> toDtoList(List<Harvest> harvestResponses);
}
