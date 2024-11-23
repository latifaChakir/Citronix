package com.example.citronix.service.interfaces;

import com.example.citronix.domain.dtos.request.sale.SaleRequestDto;
import com.example.citronix.domain.vm.SaleResponseVM;

import java.util.List;

public interface SaleService {
    SaleResponseVM createSale(SaleRequestDto saleRequestDto);
    List<SaleResponseVM> getAllSales();
    SaleResponseVM getSaleById(Long id);
    SaleResponseVM updateSale(Long id, SaleRequestDto saleRequestDto);
    void deleteSale(Long id);

}
