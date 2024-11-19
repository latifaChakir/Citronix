package com.example.citronix.api.controller;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.domain.vm.wrapper.ApiResponse;
import com.example.citronix.service.interfaces.FarmService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class FarmController {
    private final FarmService farmService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<FarmResponseVM>> createFarm(@Valid @RequestBody CreateFarmRequestDto farmRequestDto) {
        FarmResponseVM response = farmService.createFarm(farmRequestDto);
        ApiResponse<FarmResponseVM> apiResponse = ApiResponse.success(response, "/api/farms/save");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<FarmResponseVM>>> getAllFarms() {
        List<FarmResponseVM> response = farmService.getAllFarms();
        ApiResponse<List<FarmResponseVM>> apiResponse = ApiResponse.success(response, "/api/farms/");
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FarmResponseVM>> getFarmById(@PathVariable Long id) {
        FarmResponseVM response = farmService.getFarmById(id);
        ApiResponse<FarmResponseVM> apiResponse = ApiResponse.success(response, "/api/farms/" + id);
        return ResponseEntity.ok(apiResponse);
    }


}
