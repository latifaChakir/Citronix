package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.field.FieldRequestDto;
import com.example.citronix.domain.dtos.request.field.FramWithFieldRequestdTO;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.vm.FieldResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface FieldMapper {
    Field toEntity(FramWithFieldRequestdTO fieldRequest);
    @Mapping(target = "farm.id", source = "farmId")
    Field toEntity(FieldRequestDto fieldRequest);

    FieldResponseVM toDto(Field savedField);

    List<FieldResponseVM> toDtoList(List<Field> fields);
}
