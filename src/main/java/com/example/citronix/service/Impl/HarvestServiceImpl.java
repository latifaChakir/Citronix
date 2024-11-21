package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.harvest.HarvestRequestDto;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.entity.Harvest;
import com.example.citronix.domain.entity.HarvestDetail;
import com.example.citronix.domain.entity.Tree;
import com.example.citronix.domain.enums.SeasonType;
import com.example.citronix.domain.mapper.HarvestMapper;
import com.example.citronix.domain.mapper.TreeMapper;
import com.example.citronix.domain.vm.HarvestDetailResponseVM;
import com.example.citronix.domain.vm.HarvestResponseVM;
import com.example.citronix.domain.vm.TreeResponseVM;
import com.example.citronix.repository.FieldRepository;
import com.example.citronix.repository.HarvestRepository;
import com.example.citronix.service.interfaces.FieldService;
import com.example.citronix.service.interfaces.HarvestDetailService;
import com.example.citronix.service.interfaces.HarvestService;
import com.example.citronix.service.interfaces.TreeService;
import com.example.citronix.shared.exception.DuplicateHarvestException;
import com.example.citronix.shared.exception.HarvestNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HarvestServiceImpl implements HarvestService {
    private final HarvestRepository harvestRepository;
    private final HarvestMapper harvestMapper;
    private final FieldRepository fieldRepository;

    @Override
    public HarvestResponseVM saveHarvest(HarvestRequestDto harvestRequestDto) {
        SeasonType season = getSeasonFromDate(harvestRequestDto.getHarvestDate());
        Harvest harvest = harvestMapper.toEntity(harvestRequestDto);
        harvest.setSeason(season);
        Field field = fieldRepository.findById(harvestRequestDto.getFieldId())
                .orElseThrow(() -> new IllegalArgumentException("Field not found"));
        if (harvestRepository.existsBySeasonAndFieldId(season, harvestRequestDto.getFieldId())) {
            throw new DuplicateHarvestException("Une récolte existe déjà pour ce champ et cette saison.");
        }
        harvest.setField(field);
        Harvest savedHarvest = harvestRepository.save(harvest);
        return harvestMapper.toDTO(savedHarvest);
    }

    @Override
    public List<HarvestResponseVM> getAllHarvests() {
      List<Harvest> harvestResponses = harvestRepository.findAll();
      return harvestMapper.toDtoList(harvestResponses);
    }

    @Override
    public HarvestResponseVM getHarvestById(Long id) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Récolte introuvable avec l'ID: " + id));
        return harvestMapper.toDTO(harvest);
    }

    @Override
    public HarvestResponseVM updateHarvest(Long id, HarvestRequestDto harvestRequestDto) {
        Harvest existingHarvest = harvestRepository.findById(id)
                .orElseThrow(() -> new HarvestNotFoundException("Récolte introuvable avec l'ID: " + id));

        existingHarvest.setSeason(getSeasonFromDate(harvestRequestDto.getHarvestDate()));
        existingHarvest.setHarvestDate(harvestRequestDto.getHarvestDate());
        Harvest updatedHarvest = harvestRepository.save(existingHarvest);

        return harvestMapper.toDTO(updatedHarvest);
    }

    @Override
    public void deleteHarvest(Long id) {
        if (!harvestRepository.existsById(id)) {
            throw new IllegalArgumentException("Récolte introuvable avec l'ID: " + id);
        }
        harvestRepository.deleteById(id);
    }

    private SeasonType getSeasonFromDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La date de récolte ne peut pas être null");
        }
        int month = date.getMonthValue();
        System.out.println("Mois de la date: " + month);

        return switch (month) {
            case 1, 2, 3 -> SeasonType.WINTER;
            case 4, 5, 6 -> SeasonType.SPRING;
            case 7, 8, 9 -> SeasonType.SUMMER;
            case 10, 11, 12 -> SeasonType.FALL;
            default -> throw new IllegalArgumentException("Mois invalide: " + month);
        };
}
}

