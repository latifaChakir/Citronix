package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.field.FramWithFieldRequestdTO;
import com.example.citronix.domain.entity.Field;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface FieldMapper {
    Field toEntity(FramWithFieldRequestdTO fieldRequest);
}
