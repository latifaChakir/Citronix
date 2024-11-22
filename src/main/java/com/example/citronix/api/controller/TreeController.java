package com.example.citronix.api.controller;
import com.example.citronix.domain.dtos.request.tree.TreeRequestDto;
import com.example.citronix.domain.vm.TreeResponseVM;
import com.example.citronix.domain.vm.wrapper.ApiResponse;
import com.example.citronix.service.interfaces.TreeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/trees")
@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
public class TreeController {
    private final TreeService treeService;
    @PostMapping("/save")
    public ResponseEntity<ApiResponse<TreeResponseVM>> createFarm(@Valid @RequestBody TreeRequestDto treeRequestDto) {
        TreeResponseVM response = treeService.createTree(treeRequestDto);
        ApiResponse<TreeResponseVM> apiResponse = ApiResponse.success(response, "/api/trees/save");
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<TreeResponseVM>>> getAllTrees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<TreeResponseVM> response = treeService.getAllTrees(pageRequest);
        ApiResponse<Page<TreeResponseVM>> apiResponse = ApiResponse.success(response, "/api/trees/all");
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TreeResponseVM>> getTreeById(@PathVariable Long id) {
        TreeResponseVM response = treeService.getTreeById(id);
        ApiResponse<TreeResponseVM> apiResponse = ApiResponse.success(response, "/api/trees/" + id);
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<TreeResponseVM>> updateTree(@PathVariable Long id, @Valid @RequestBody TreeRequestDto treeRequestDto) {
        TreeResponseVM response = treeService.updateTree(id, treeRequestDto);
        ApiResponse<TreeResponseVM> apiResponse = ApiResponse.success(response, "/api/trees/update/" + id);
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTree(@PathVariable Long id) {
        treeService.deleteTree(id);
        ApiResponse<String> apiResponse = ApiResponse.success("Tree deleted successfully", "/api/trees/" + id);
        return ResponseEntity.ok(apiResponse);
    }
}
