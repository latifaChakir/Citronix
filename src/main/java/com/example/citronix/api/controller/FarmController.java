package com.example.citronix.api.controller;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.service.interfaces.FarmService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/farms")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class FarmController {
    private final FarmService farmService;

    @PostMapping("/save")
    public ResponseEntity<FarmResponseVM> createFarm(@Valid @RequestBody CreateFarmRequestDto farmRequestDto) {
        FarmResponseVM response = farmService.createFarm(farmRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
//    public ResponseEntity<ApiResponse<FarmResponseVM>> createFarm(@Valid @RequestBody CreateFarmRequestDto farmRequestDto) {
//        FarmResponseVM response = farmService.createFarm(farmRequestDto);
//        ApiResponse<FarmResponseVM> apiResponse = ApiResponse.success(response, "/api/farms/save");
//        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
//    }
}
