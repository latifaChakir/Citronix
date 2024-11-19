package com.example.citronix.service.Impl;

import com.example.citronix.repository.FieldRepository;
import com.example.citronix.service.interfaces.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    @Override
    public void deleteField(Long id) {
        fieldRepository.deleteById(id);
    }
}
