package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.dtos.request.field.FramWithFieldRequestdTO;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.mapper.FieldMapper;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.domain.entity.Farm;
import com.example.citronix.domain.mapper.FarmMapper;
import com.example.citronix.repository.FarmRepository;
import com.example.citronix.service.interfaces.FarmService;
import com.example.citronix.service.interfaces.FieldService;
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
    private FieldService fieldService;
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
                    .mapToDouble(FramWithFieldRequestdTO::getArea)
                    .sum();

            if (totalFieldArea >= farmRequestDto.getArea()) {
                throw new TotalFieldAreaExceedsFarmAreaException("La somme des superficies des champs ne peut pas dépasser celle de la ferme.");
            }

            for (FramWithFieldRequestdTO field : farmRequestDto.getFields()) {
                if (field.getArea() < 1000 || field.getArea() > farmRequestDto.getArea() / 2) {
                    throw new InvalidFieldAreaException(
                            String.format("La superficie de chaque champ (%s m²) doit être comprise entre 1000 m² et 50%% de la superficie totale.", field.getArea())
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
        Farm existingFarm = farmRepository.findById(id)
                .orElseThrow(() -> new FarmNotFoundException("Farm not found"));
        if (farmRequestDto.getCreationDate().isAfter(LocalDate.now())) {
            throw new InvalidFarmCreationDateException("La date de création ne peut pas être dans le futur.");
        }
        if (!existingFarm.getName().equals(farmRequestDto.getName())
                || !existingFarm.getLocation().equals(farmRequestDto.getLocation())) {
            if (farmRepository.existsByNameAndLocation(farmRequestDto.getName(), farmRequestDto.getLocation())) {
                throw new FarmAlreadyExistsException("Une ferme avec le même nom et localisation existe déjà.");
            }
        }
        if (farmRequestDto.getFields() != null && !farmRequestDto.getFields().isEmpty()) {
            double totalFieldArea = farmRequestDto.getFields().stream()
                    .mapToDouble(FramWithFieldRequestdTO::getArea)
                    .sum();

            if (totalFieldArea >= farmRequestDto.getArea()) {
                throw new TotalFieldAreaExceedsFarmAreaException("La somme des superficies des champs ne peut pas dépasser celle de la ferme.");
            }
            if (farmRequestDto.getFields().size() >= 10) {
                throw new MaxFieldLimitExceededException("Une ferme ne peut contenir plus de 10 champs.");
            }

            for (FramWithFieldRequestdTO field : farmRequestDto.getFields()) {
                if (field.getArea() < 1000 || field.getArea() > farmRequestDto.getArea() / 2) {
                    throw new InvalidFieldAreaException(
                            String.format("La superficie de chaque champ (%s m²) doit être comprise entre 1000 m² et 50%% de la superficie totale.", field.getArea())
                    );
                }
            }
        }
        existingFarm.setName(farmRequestDto.getName());
        existingFarm.setLocation(farmRequestDto.getLocation());
        existingFarm.setArea(farmRequestDto.getArea());
        existingFarm.setCreationDate(farmRequestDto.getCreationDate());
        if (farmRequestDto.getFields() != null && !farmRequestDto.getFields().isEmpty()) {
            List<Field> updatedFields = farmRequestDto.getFields().stream().map(fieldRequest -> {
                Field field = fieldMapper.toEntity(fieldRequest);
                field.setFarm(existingFarm);
                return field;
            }).collect(Collectors.toList());
            existingFarm.getFields().clear();
            existingFarm.getFields().addAll(updatedFields);
        } else {
            existingFarm.getFields().clear();
        }
        Farm updatedFarm = farmRepository.save(existingFarm);
        return farmMapper.toDto(updatedFarm);
    }

    @Override
    public void deleteFarm(Long id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new FarmNotFoundException("Farm not found"));
        farmRepository.delete(farm);
    }

}
