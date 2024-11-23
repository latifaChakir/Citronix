package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.harvestDetail.HarvestDetailRequestDto;
import com.example.citronix.domain.entity.HarvestDetail;
import com.example.citronix.domain.vm.HarvestDetailResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface HarvestDetailMapper {
    @Mapping(source = "harvest.id", target = "harvestId")
    @Mapping(source = "tree.id", target = "treeId")
    HarvestDetailResponseVM toResponseVM(HarvestDetail harvestDetail);
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "harvestId", target = "harvest.id")
    @Mapping(source = "treeId", target = "tree.id")
    HarvestDetail toEntity(HarvestDetailRequestDto harvestDetailRequestDto);

    List<HarvestDetailResponseVM> toDtoList(List<HarvestDetail> harvestDetails);
}
