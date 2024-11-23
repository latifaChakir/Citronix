package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.sale.SaleRequestDto;
import com.example.citronix.domain.entity.Sale;
import com.example.citronix.domain.vm.SaleResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface SaleMapper {
    @Mapping(source = "harvestId", target = "harvest.id")
    Sale toEntity(SaleRequestDto saleRequestDto);
//    @Mapping(source = "harvest.id", target = "harvestId")
    SaleResponseVM toDTO(Sale savedSale);

    List<SaleResponseVM> toDtoList(List<Sale> sales);
}
