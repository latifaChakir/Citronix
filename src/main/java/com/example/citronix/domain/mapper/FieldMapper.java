package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.field.FramWithFieldRequest;
import com.example.citronix.domain.entity.Field;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface FieldMapper {
    Field toEntity(FramWithFieldRequest fieldRequest);
}
