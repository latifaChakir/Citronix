package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.harvestDetail.HarvestDetailRequestDto;
import com.example.citronix.domain.entity.HarvestDetail;
import com.example.citronix.domain.entity.Tree;
import com.example.citronix.domain.enums.SeasonType;
import com.example.citronix.domain.mapper.HarvestDetailMapper;
import com.example.citronix.domain.vm.HarvestDetailResponseVM;
import com.example.citronix.domain.vm.HarvestResponseVM;
import com.example.citronix.domain.vm.TreeResponseVM;
import com.example.citronix.repository.HarvestDetailRepository;
import com.example.citronix.repository.HarvestRepository;
import com.example.citronix.repository.TreeRepository;
import com.example.citronix.service.interfaces.HarvestDetailService;
import com.example.citronix.service.interfaces.HarvestService;
import com.example.citronix.service.interfaces.TreeService;
import com.example.citronix.shared.exception.DuplicateHarvestException;
import com.example.citronix.shared.exception.HarvestNotFoundException;
import com.example.citronix.shared.exception.TreeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HarvestDetailServiceImpl implements HarvestDetailService {
    private final HarvestDetailRepository harvestDetailRepository;
    private final HarvestDetailMapper harvestDetailMapper;
    private final HarvestRepository harvestRepository;
    private final TreeRepository treeRepository;

    @Override
    public HarvestDetailResponseVM saveHarvestDetail(HarvestDetailRequestDto harvestDetailRequestDto) {
        var harvest = harvestRepository.findById(harvestDetailRequestDto.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException("Récolte introuvable."));
        boolean exists = harvestDetailRepository.existsByTreeIdAndSeason(
                harvestDetailRequestDto.getTreeId(),
                harvest.getSeason()
        );
        if (exists) {
            throw new DuplicateHarvestException(
                    "Cet arbre est déjà inclus dans une récolte pour cette saison."
            );
        }

        Tree tree = treeRepository.findById(harvestDetailRequestDto.getTreeId())
                .orElseThrow(() -> new TreeNotFoundException("Arbre introuvable."));
        double harvestQuantity = calculateTreeProductivity(tree, harvest.getHarvestDate());
        HarvestDetail harvestDetail = harvestDetailMapper.toEntity(harvestDetailRequestDto);
        harvestDetail.setTree(tree);
        harvestDetail.setHarvest(harvest);
        harvestDetail.setQuantity(harvestQuantity);
        HarvestDetail savedDetail = harvestDetailRepository.save(harvestDetail);
        harvest.setTotalQuantity(harvest.getTotalQuantity() + harvestQuantity);
        harvestRepository.save(harvest);
        return harvestDetailMapper.toResponseVM(savedDetail);
    }
    @Override
    public List<HarvestDetailResponseVM> getAllHarvestDetails() {
            List<HarvestDetail> harvestDetails= harvestDetailRepository.findAll();
            return harvestDetailMapper.toDtoList(harvestDetails);
    }

    @Override
    public HarvestDetailResponseVM getHarvestDetailById(Long id) {
        HarvestDetail harvestDetail = harvestDetailRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Détail de récolte introuvable."));

        return harvestDetailMapper.toResponseVM(harvestDetail);
    }

//    @Override
//    public HarvestDetailResponseVM updateHarvestDetail(Long id, HarvestDetailRequestDto harvestDetailRequestDto) {
//        // Récupérer le détail existant
////        HarvestDetail existingDetail = harvestDetailRepository.findById(id)
////                .orElseThrow(() -> new IllegalArgumentException("Détail de récolte introuvable."));
////        HarvestResponseVM harvest= harvestService.getHarvestById(harvestDetailRequestDto.getHarvestId());
//
//        // Validation: Un arbre ne peut pas être inclus dans plus d’une récolte pour une même saison
////        boolean exists = harvestDetailRepository.existsByTreeIdAndSeason(
////                harvestDetailRequestDto.getTreeId(),
////                harvest.getSeason()
////        );
////        if (exists && !existingDetail.getTree().getId().equals(harvestDetailRequestDto.getTreeId())) {
////            throw new IllegalArgumentException(
////                    "Cet arbre est déjà inclus dans une récolte pour cette saison."
////            );
////        }
//
//        // Mettre à jour les champs nécessaires
////        harvestDetailMapper.updateEntityFromDto(harvestDetailRequestDto, existingDetail);
//
//        // Sauvegarde
////        HarvestDetail updatedDetail = harvestDetailRepository.save(existingDetail);
//
////        return harvestDetailMapper.toResponseVM(updatedDetail);
//    }

    @Override
    public void deleteHarvestDetail(Long id) {
        if (!harvestDetailRepository.existsById(id)) {
            throw new IllegalArgumentException("Détail de récolte introuvable.");
        }
        harvestDetailRepository.deleteById(id);
    }

    @Override
    public void saveAll(List<HarvestDetail> harvestDetails) {
        harvestDetailRepository.saveAll(harvestDetails);
    }

    @Override
    public boolean existsByTreeAndSeason(Tree tree, SeasonType season) {
        return harvestDetailRepository.existsByTreeIdAndSeason(tree.getId(), season);
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
