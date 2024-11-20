package com.example.citronix.service.interfaces;
import com.example.citronix.domain.dtos.request.field.FieldRequestDto;
import com.example.citronix.domain.vm.FieldResponseVM;

import java.util.List;

public interface FieldService {
    FieldResponseVM createField(FieldRequestDto fieldRequestDto);
    List<FieldResponseVM> getAllFields();
    FieldResponseVM getFieldById(Long id);
    FieldResponseVM updateField(Long id, FieldRequestDto fieldRequestDto);

    void deleteField(Long id);
}
