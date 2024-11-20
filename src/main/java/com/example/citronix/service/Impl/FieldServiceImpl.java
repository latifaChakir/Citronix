package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.field.FieldRequestDto;
import com.example.citronix.domain.vm.FieldResponseVM;
import com.example.citronix.repository.FieldRepository;
import com.example.citronix.service.interfaces.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;

    @Override
    public FieldResponseVM createField(FieldRequestDto fieldRequestDto) {
        return null;
    }

    @Override
    public List<FieldResponseVM> getAllFields() {
        return List.of();
    }

    @Override
    public FieldResponseVM getFieldById(Long id) {
        return null;
    }

    @Override
    public FieldResponseVM updateField(Long id, FieldRequestDto fieldRequestDto) {
        return null;
    }

    @Override
    public void deleteField(Long id) {
        fieldRepository.deleteById(id);
    }
}
