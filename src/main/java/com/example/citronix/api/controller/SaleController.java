package com.example.citronix.api.controller;

import com.example.citronix.domain.dtos.request.sale.SaleRequestDto;
import com.example.citronix.domain.dtos.request.tree.TreeRequestDto;
import com.example.citronix.domain.vm.SaleResponseVM;
import com.example.citronix.domain.vm.TreeResponseVM;
import com.example.citronix.domain.vm.wrapper.ApiResponse;
import com.example.citronix.service.Impl.SaleServiceImpl;
import com.example.citronix.service.interfaces.SaleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class SaleController {
    private final SaleService saleService;
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<SaleResponseVM>> createFarm(@Valid @RequestBody SaleRequestDto saleRequestDto) {
        SaleResponseVM response = saleService.createSale(saleRequestDto);
        ApiResponse<SaleResponseVM> apiResponse = ApiResponse.success(response, "/api/sales/save");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleResponseVM>> getSaleById(@PathVariable Long id) {
        SaleResponseVM response = saleService.getSaleById(id);
        ApiResponse<SaleResponseVM> apiResponse = ApiResponse.success(response, "/api/sales/" + id);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SaleResponseVM>>> getAllSales() {
        List<SaleResponseVM> response = saleService.getAllSales();
        ApiResponse<List<SaleResponseVM>> apiResponse = ApiResponse.success(response, "/api/sales/all");
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        ApiResponse<String> apiResponse = ApiResponse.success("Sale deleted successfully", "/api/sales/" + id);
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SaleResponseVM>> updateSale(@PathVariable Long id, @Valid @RequestBody SaleRequestDto saleRequestDto) {
        SaleResponseVM response = saleService.updateSale(id, saleRequestDto);
        ApiResponse<SaleResponseVM> apiResponse = ApiResponse.success(response, "/api/sales/" + id);
        return ResponseEntity.ok(apiResponse);
    }
}
