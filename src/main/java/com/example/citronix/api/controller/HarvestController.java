package com.example.citronix.api.controller;

import com.example.citronix.domain.dtos.request.harvest.HarvestRequestDto;
import com.example.citronix.domain.vm.HarvestResponseVM;
import com.example.citronix.domain.vm.wrapper.ApiResponse;
import com.example.citronix.service.interfaces.HarvestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harvests")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class HarvestController {
    private final HarvestService harvestService;
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<HarvestResponseVM>> createHarvest(@Valid @RequestBody HarvestRequestDto harvestRequestDto) {
        HarvestResponseVM response = harvestService.saveHarvest(harvestRequestDto);
        ApiResponse<HarvestResponseVM> apiResponse = ApiResponse.success(response, "/api/harvests/save");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HarvestResponseVM>> getHarvestById(@PathVariable Long id) {
        HarvestResponseVM response = harvestService.getHarvestById(id);
        ApiResponse<HarvestResponseVM> apiResponse = ApiResponse.success(response, "/api/harvests/" + id);
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HarvestResponseVM>> updateHarvest(@PathVariable Long id, @Valid @RequestBody HarvestRequestDto harvestRequestDto) {
        HarvestResponseVM response = harvestService.updateHarvest(id, harvestRequestDto);
        ApiResponse<HarvestResponseVM> apiResponse = ApiResponse.success(response, "/api/harvests/" + id);
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteHarvest(@PathVariable Long id) {
        harvestService.deleteHarvest(id);
        ApiResponse<String> apiResponse = ApiResponse.success("Harvest deleted successfully", "/api/harvests/" + id);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<HarvestResponseVM>>> getAllHarvests() {
        List<HarvestResponseVM> response = harvestService.getAllHarvests();
        ApiResponse<List<HarvestResponseVM>> apiResponse = ApiResponse.success(response, "/api/harvests/all");
        return ResponseEntity.ok(apiResponse);
    }
}
