package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.sale.SaleRequestDto;
import com.example.citronix.domain.entity.Harvest;
import com.example.citronix.domain.entity.Sale;
import com.example.citronix.domain.mapper.SaleMapper;
import com.example.citronix.domain.vm.SaleResponseVM;
import com.example.citronix.repository.HarvestRepository;
import com.example.citronix.repository.SaleRepository;
import com.example.citronix.service.interfaces.SaleService;
import com.example.citronix.shared.exception.HarvestNotFoundException;
import com.example.citronix.shared.exception.SaleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final HarvestRepository harvestRepository;

    @Override
    public SaleResponseVM createSale(SaleRequestDto saleRequestDto) {
        Harvest harvest = harvestRepository.findById(saleRequestDto.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException("Récolte introuvable avec l'ID: " + saleRequestDto.getHarvestId()));
        double revenue = harvest.getTotalQuantity() * saleRequestDto.getUnitPrice();
        Sale sale = saleMapper.toEntity(saleRequestDto);
        sale.setRevenue(revenue);
        sale.setHarvest(harvest);
        Sale savedSale = saleRepository.save(sale);
        return saleMapper.toDTO(savedSale);
    }

    @Override
    public List<SaleResponseVM> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        return saleMapper.toDtoList(sales);
    }

    @Override
    public SaleResponseVM getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Vente introuvable avec l'ID: " + id));
        return saleMapper.toDTO(sale);
    }

    @Override
    public SaleResponseVM updateSale(Long id, SaleRequestDto saleRequestDto) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Vente introuvable avec l'ID: " + id));
        Harvest harvest = harvestRepository.findById(saleRequestDto.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException("Récolte introuvable avec l'ID: " + saleRequestDto.getHarvestId()));

        double revenue = harvest.getTotalQuantity() * saleRequestDto.getUnitPrice();

        existingSale.setDate(saleRequestDto.getDate());
        existingSale.setUnitPrice(saleRequestDto.getUnitPrice());
        existingSale.setClient(saleRequestDto.getClient());
        existingSale.setHarvest(harvest);
        existingSale.setRevenue(revenue);

        Sale updatedSale = saleRepository.save(existingSale);

        return saleMapper.toDTO(updatedSale);
    }

    @Override
    public void deleteSale(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new SaleNotFoundException("Vente introuvable avec l'ID: " + id);
        }
        saleRepository.deleteById(id);
    }
}
