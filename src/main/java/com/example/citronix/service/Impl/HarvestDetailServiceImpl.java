package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.harvestDetail.HarvestDetailRequestDto;
import com.example.citronix.domain.entity.HarvestDetail;
import com.example.citronix.domain.entity.Tree;
import com.example.citronix.domain.enums.SeasonType;
import com.example.citronix.domain.vm.HarvestDetailResponseVM;
import com.example.citronix.domain.vm.TreeResponseVM;
import com.example.citronix.repository.HarvestDetailRepository;
import com.example.citronix.service.interfaces.HarvestDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HarvestDetailServiceImpl implements HarvestDetailService {
    private final HarvestDetailRepository harvestDetailRepository;

    @Override
    public HarvestDetailResponseVM saveHarvestDetail(HarvestDetailRequestDto HarvestDetailRequestDto) {
        return null;
    }

    @Override
    public List<HarvestDetailResponseVM> getAllHarvestDetails() {
        return List.of();
    }

    @Override
    public HarvestDetailResponseVM getHarvestDetailById(Long id) {
        return null;
    }

    @Override
    public HarvestDetailResponseVM updateHarvestDetail(Long id, HarvestDetailRequestDto HarvestDetailRequestDto) {
        return null;
    }

    @Override
    public void deleteHarvestDetail(Long id) {

    }

    @Override
    public List<HarvestDetailResponseVM> getListDetail(List<TreeResponseVM> trees) {
        return List.of();
    }

    @Override
    public void saveAll(List<HarvestDetail> harvestDetails) {
        harvestDetailRepository.saveAll(harvestDetails);
    }

    @Override
    public boolean existsByTreeAndSeason(Tree tree, SeasonType season) {
        return harvestDetailRepository.existsByTreeIdAndSeason(tree.getId(), season);
    }
}
