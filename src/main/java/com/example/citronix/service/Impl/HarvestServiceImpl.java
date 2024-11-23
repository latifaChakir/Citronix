package com.example.citronix.service.Impl;
import org.hibernate.Hibernate;
import com.example.citronix.domain.dtos.request.harvest.HarvestRequestDto;
import com.example.citronix.domain.entity.Harvest;
import com.example.citronix.domain.entity.HarvestDetail;
import com.example.citronix.domain.entity.Tree;
import com.example.citronix.domain.enums.SeasonType;
import com.example.citronix.domain.mapper.HarvestMapper;
import com.example.citronix.domain.mapper.TreeMapper;
import com.example.citronix.domain.vm.HarvestResponseVM;
import com.example.citronix.domain.vm.TreeResponseVM;
import com.example.citronix.repository.FieldRepository;
import com.example.citronix.repository.HarvestRepository;
import com.example.citronix.service.interfaces.HarvestDetailService;
import com.example.citronix.service.interfaces.HarvestService;
import com.example.citronix.service.interfaces.TreeService;
import com.example.citronix.shared.exception.DuplicateHarvestException;
import com.example.citronix.shared.exception.HarvestNotFoundException;
import com.example.citronix.shared.exception.TreeNotFoundException;
import com.example.citronix.shared.exception.TreeNotProductif;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HarvestServiceImpl implements HarvestService {
    private final HarvestRepository harvestRepository;
    private final HarvestMapper harvestMapper;
    private final FieldRepository fieldRepository;
    private final TreeService treeService;
    private final HarvestDetailService harvestDetailService;
    private final TreeMapper treeMapper;

    @Override
    public HarvestResponseVM saveHarvest(HarvestRequestDto harvestRequestDto) {
        SeasonType season = getSeasonFromDate(harvestRequestDto.getHarvestDate());
        Harvest harvest = harvestMapper.toEntity(harvestRequestDto);
        harvest.setSeason(season);
        List<HarvestDetail> harvestDetails = harvestRequestDto.getHarvestDetails()
                .stream().map(detail -> {
            TreeResponseVM treeResponseVM = treeService.getTreeById(detail.getTreeId());
                    if (treeResponseVM == null) {
                        throw new TreeNotFoundException("Tree with id " + detail.getTreeId() + " not found");
                    }
            Tree tree= treeMapper.toEntity(treeResponseVM);
            validateTreeForHarvest(tree, season,harvest.getHarvestDate());

            HarvestDetail harvestDetail = new HarvestDetail();
            harvestDetail.setTree(tree);
            double harvestQuantity=tree.getProductivity();
            harvestDetail.setQuantity(harvestQuantity);
            harvestDetail.setHarvest(harvest);
            return harvestDetail;
        }).collect(Collectors.toList());

        harvest.setHarvestDetails(harvestDetails);
        harvest.calculateTotalQuantity();
        Harvest savedHarvest = harvestRepository.save(harvest);

        harvestDetailService.saveAll(harvestDetails);
        return harvestMapper.toDTO(savedHarvest);
    }
    private void validateTreeForHarvest(Tree tree, SeasonType season, LocalDate harvestDate) {
        if (tree == null) {
            throw new TreeNotFoundException("L'arbre spécifié n'existe pas.");
        }

        if (tree.getPlantationDate().plusYears(20).isBefore(harvestDate)) {
            throw new TreeNotProductif("L'arbre avec ID " + tree.getId() + " n'est plus productif.");
        }
        double productivity = calculateTreeProductivity(tree,harvestDate);
        if (productivity == 0.0) {
            throw new TreeNotProductif("L'arbre avec ID " + tree.getId() + " n'est plus productif.");
        }

        if (harvestDetailService.existsByTreeAndSeason(tree, season)) {
            throw new DuplicateHarvestException("L'arbre avec ID " + tree.getId() + " a déjà été récolté pour cette saison.");
        }

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

        SeasonType season = getSeasonFromDate(harvestRequestDto.getHarvestDate());
        existingHarvest.setSeason(season);
        existingHarvest.setHarvestDate(harvestRequestDto.getHarvestDate());
        harvestDetailService.deleteAllByHarvestId(id);
        List<HarvestDetail> harvestDetails = harvestRequestDto.getHarvestDetails()
                .stream().map(detail -> {
                    TreeResponseVM treeResponseVM = treeService.getTreeById(detail.getTreeId());
                    if (treeResponseVM == null) {
                        throw new TreeNotFoundException("Tree with id " + detail.getTreeId() + " not found");
                    }
                    Tree tree = treeMapper.toEntity(treeResponseVM);
                    validateTreeForHarvest(tree, season, existingHarvest.getHarvestDate());

                    HarvestDetail harvestDetail = new HarvestDetail();
                    harvestDetail.setTree(tree);
                    harvestDetail.setQuantity(tree.getProductivity());
                    harvestDetail.setHarvest(existingHarvest);
                    return harvestDetail;
                }).collect(Collectors.toList());

        double totalQuantity = harvestDetails.stream()
                .mapToDouble(HarvestDetail::getQuantity)
                .sum();
        existingHarvest.setTotalQuantity(totalQuantity);
        harvestRepository.save(existingHarvest);
        harvestDetailService.saveAll(harvestDetails);

        existingHarvest.setHarvestDetails(harvestDetails);
        return harvestMapper.toDTO(existingHarvest);
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

    private double calculateTreeProductivity(Tree tree, LocalDate harvestDate) {
        long age = ChronoUnit.YEARS.between(tree.getPlantationDate(), harvestDate);
        if (age < 3) {
            return 2.5;
        } else if (age <= 10) {
            return 12.0;
        } else if (age <= 20) {
            return 20.0;
        }
        return 0.0;
    }
}



