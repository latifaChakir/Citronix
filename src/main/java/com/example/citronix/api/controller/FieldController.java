package com.example.citronix.api.controller;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.dtos.request.field.FieldRequestDto;
import com.example.citronix.domain.vm.FieldResponseVM;
import com.example.citronix.domain.vm.wrapper.ApiResponse;
import com.example.citronix.service.interfaces.FieldService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class FieldController {
    private final FieldService fieldService;
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<FieldResponseVM>> createFarm(@Valid @RequestBody FieldRequestDto fieldRequestDto) {
        FieldResponseVM response = fieldService.createField(fieldRequestDto);
        ApiResponse<FieldResponseVM> apiResponse = ApiResponse.success(response, "/api/farms/save");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FieldResponseVM>> getFieldById(@PathVariable Long id) {
        FieldResponseVM response = fieldService.getFieldById(id);
        ApiResponse<FieldResponseVM> apiResponse = ApiResponse.success(response, "/api/fields/{id}");
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FieldResponseVM>> updateField(@PathVariable Long id, @Valid @RequestBody FieldRequestDto fieldRequestDto) {
        FieldResponseVM response = fieldService.updateField(id, fieldRequestDto);
        ApiResponse<FieldResponseVM> apiResponse = ApiResponse.success(response, "/api/fields/{id}");
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteField(@PathVariable Long id) {
        fieldService.deleteField(id);
        ApiResponse<String> apiResponse = ApiResponse.success("Field deleted successfully", "/api/farms/" + id);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<FieldResponseVM>>> getAllFields() {
        List<FieldResponseVM> response = fieldService.getAllFields();
        ApiResponse<List<FieldResponseVM>> apiResponse = ApiResponse.success(response, "/api/fields/all");
        return ResponseEntity.ok(apiResponse);
    }

}
