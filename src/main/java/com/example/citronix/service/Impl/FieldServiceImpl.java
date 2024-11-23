package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.field.FieldRequestDto;
import com.example.citronix.domain.entity.Farm;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.mapper.FarmMapper;
import com.example.citronix.domain.mapper.FieldMapper;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.domain.vm.FieldResponseVM;
import com.example.citronix.repository.FieldRepository;
import com.example.citronix.service.interfaces.FarmService;
import com.example.citronix.service.interfaces.FieldService;
import com.example.citronix.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final FarmMapper farmMapper;
    private final FieldMapper fieldMapper;
    private final FarmService farmService;

    @Override
    public FieldResponseVM createField(FieldRequestDto fieldRequestDto) {
        FarmResponseVM farm = farmService.getFarmById(fieldRequestDto.getFarmId());
        if(farm == null){
            throw new FarmNotFoundException("La ferme avec l'ID " + fieldRequestDto.getFarmId() + " n'existe pas.");
        }
        double totalFieldsAera = farm.getFields().stream().mapToDouble(FieldResponseVM::getArea).sum();
        if (totalFieldsAera + fieldRequestDto.getArea() > farm.getArea()) {
            throw new TotalFieldAreaExceedsFarmAreaException("La somme des superficies des champs dépasse celle de la ferme");
        }
        if (fieldRequestDto.getArea() < 1000 || fieldRequestDto.getArea() > farm.getArea() / 2) {
            throw new InvalidFieldAreaException(
                    String.format("La superficie du champ (%s m²) doit être comprise entre 0.1 hectare et 50%% de la superficie totale.", fieldRequestDto.getArea())
            );
        }

        if (farm.getFields().size() >= 10) {
            throw new MaxFieldLimitExceededException("Une ferme ne peut contenir plus de 10 champs.");
        }
        Field filed = fieldMapper.toEntity(fieldRequestDto);
        Field savedField = fieldRepository.save(filed);
        return fieldMapper.toDto(savedField);
    }

    @Override
    public List<FieldResponseVM> getAllFields() {
        List<Field> fields = fieldRepository.findAll();
        return fieldMapper.toDtoList(fields);
    }

    @Override
    public FieldResponseVM getFieldById(Long id) {
        Optional<Field> field = fieldRepository.findById(id);
        if (field.isEmpty()) {
            throw new FieldNotFoundException("Champ avec l'ID " + id + " non trouvé.");
        }
        Field fieldEntity = field.get();
        if (fieldEntity.getFarm() == null) {
            throw new FarmNotFoundException("Ferme associée non trouvée pour le champ avec l'ID " + id);
        }
        return fieldMapper.toDto(fieldEntity);
    }


    @Override
    public FieldResponseVM updateField(Long fieldId, FieldRequestDto fieldRequestDto) {
        Field existingField = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new FieldNotFoundException("Le champ n'existe pas."));

        FarmResponseVM farm = farmService.getFarmById(fieldRequestDto.getFarmId());
        double totalFieldArea = farm.getFields().stream()
                .filter(f -> !f.getId().equals(fieldId))
                .mapToDouble(FieldResponseVM::getArea)
                .sum();

        if (totalFieldArea + fieldRequestDto.getArea() > farm.getArea()) {
            throw new TotalFieldAreaExceedsFarmAreaException(
                    "La somme des superficies des champs dépasse celle de la ferme.");
        }

        if (fieldRequestDto.getArea() < 0.1 || fieldRequestDto.getArea() > farm.getArea() / 2) {
            throw new InvalidFieldAreaException(
                    String.format("La superficie du champ (%s m²) doit être comprise entre 0.1 hectare et 50%% de la superficie totale.", fieldRequestDto.getArea())
            );
        }
        Farm farm1= farmMapper.toEntity(farm);

        existingField.setName(fieldRequestDto.getName());
        existingField.setArea(fieldRequestDto.getArea());
        existingField.setFarm(farm1);

        Field updatedField = fieldRepository.save(existingField);
        return fieldMapper.toDto(updatedField);
    }

    @Override
    public void deleteField(Long id) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new FieldNotFoundException("Le champ n'existe pas."));
        fieldRepository.delete(field);
    }
}
