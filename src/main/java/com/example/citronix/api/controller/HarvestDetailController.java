package com.example.citronix.api.controller;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.dtos.request.harvestDetail.HarvestDetailRequestDto;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.domain.vm.HarvestDetailResponseVM;
import com.example.citronix.domain.vm.wrapper.ApiResponse;
import com.example.citronix.service.interfaces.HarvestDetailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harvestDetails")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class HarvestDetailController {
    private final HarvestDetailService harvestDetailService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<HarvestDetailResponseVM>> createFarm(@Valid @RequestBody HarvestDetailRequestDto harvestDetailRequestDto) {
        HarvestDetailResponseVM response = harvestDetailService.saveHarvestDetail(harvestDetailRequestDto);
        ApiResponse<HarvestDetailResponseVM> apiResponse = ApiResponse.success(response, "/api/harvestDetails/save");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HarvestDetailResponseVM>> getHarvestDetailById(@PathVariable Long id) {
        HarvestDetailResponseVM response = harvestDetailService.getHarvestDetailById(id);
        ApiResponse<HarvestDetailResponseVM> apiResponse = ApiResponse.success(response, "/api/harvestDetails/" + id);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping ("/all")
    public ResponseEntity<ApiResponse<List<HarvestDetailResponseVM>>> getAllHarvestDetails() {
       List<HarvestDetailResponseVM>  response = harvestDetailService.getAllHarvestDetails();
        ApiResponse<List<HarvestDetailResponseVM>> apiResponse = ApiResponse.success(response, "/api/harvestDetails/all");
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteHarvestDetail(@PathVariable Long id) {
        harvestDetailService.deleteHarvestDetail(id);
        ApiResponse<String> apiResponse = ApiResponse.success("Harvest Detail deleted successfully", "/api/harvestDetails/" + id);
        return ResponseEntity.ok(apiResponse);
    }
}
