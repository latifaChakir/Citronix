package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.dtos.request.field.FramWithFieldRequest;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.mapper.FieldMapper;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.domain.entity.Farm;
import com.example.citronix.domain.mapper.FarmMapper;
import com.example.citronix.repository.FarmRepository;
import com.example.citronix.service.interfaces.FarmService;
import com.example.citronix.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FarmServiceImpl implements FarmService {
    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;
    private final FieldMapper fieldMapper;
    @Override
    public FarmResponseVM createFarm(CreateFarmRequestDto farmRequestDto) {
        if (farmRequestDto.getCreationDate().isAfter(LocalDate.now())) {
            throw new InvalidFarmCreationDateException("La date de création ne peut pas être dans le futur.");
        }
        if (farmRepository.existsByNameAndLocation(farmRequestDto.getName(), farmRequestDto.getLocation())) {
            throw new FarmAlreadyExistsException("Une ferme avec le même nom et localisation existe déjà.");
        }

        if (farmRequestDto.getFields() != null && !farmRequestDto.getFields().isEmpty()) {
            double totalFieldArea = farmRequestDto.getFields().stream()
                    .mapToDouble(FramWithFieldRequest::getArea)
                    .sum();

            if (totalFieldArea >= farmRequestDto.getArea()) {
                throw new TotalFieldAreaExceedsFarmAreaException("La somme des superficies des champs ne peut pas dépasser celle de la ferme.");
            }

            for (FramWithFieldRequest field : farmRequestDto.getFields()) {
                if (field.getArea() < 0.1 || field.getArea() > farmRequestDto.getArea() / 2) {
                    throw new InvalidFieldAreaException(
                            String.format("La superficie de chaque champ (%s m²) doit être comprise entre 0.1 hectare et 50%% de la superficie totale.", field.getArea())
                    );
                }
            }
        }

        Farm farm = farmMapper.toEntity(farmRequestDto);

        if (farmRequestDto.getFields() != null && !farmRequestDto.getFields().isEmpty()) {
            List<Field> fields = farmRequestDto.getFields().stream().map(fieldRequest -> {
                Field field = fieldMapper.toEntity(fieldRequest);
                field.setFarm(farm);
                return field;
            }).collect(Collectors.toList());

            farm.setFields(fields);
        }
        Farm savedFarm = farmRepository.save(farm);
        return farmMapper.toDto(savedFarm);
    }


    @Override
    public List<FarmResponseVM> getAllFarms() {
        List<Farm> farms = farmRepository.findAll();
        return farmMapper.toDtoList(farms);
    }

    @Override
    public FarmResponseVM getFarmById(Long id) {
        Optional<Farm> farm = farmRepository.findById(id);
        if (farm.isEmpty()) {
            throw new FarmNotFoundException("Farm not found");
        }
        return farmMapper.toDto(farm.get());
    }

    @Override
    public FarmResponseVM updateFarm(Long id, CreateFarmRequestDto farmRequestDto) {
        return null;
    }

    @Override
    public void deleteFarm(Long id) {
        if (farmRepository.findById(id).isEmpty()) {
            throw new FarmNotFoundException("Farm not found");
        }
        farmRepository.deleteById(id);
    }
}
