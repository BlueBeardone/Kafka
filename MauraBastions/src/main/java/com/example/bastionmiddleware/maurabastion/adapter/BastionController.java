package com.example.bastionmiddleware.maurabastion.adapter;

import com.example.bastionmiddleware.maurabastion.api.BastionApi;
import com.example.bastionmiddleware.dto.ApiResponse;
import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class BastionController {

    private final BastionApi bastionApi;

    public BastionController(BastionApi bastionApi) {
        this.bastionApi = bastionApi;
    }

    @PostMapping("/bastions")
    public ResponseEntity<ApiResponse> createBastion(@Valid @RequestBody BastionRequest request) {
        BastionResponse response = bastionApi.createBastion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Request processed successfully", response));
    }

    @GetMapping("/bastions/{id}")
    public ResponseEntity<ApiResponse> getBastion(@PathVariable Long id) {
        BastionResponse response = bastionApi.getBastion(id);
        return ResponseEntity.ok(ApiResponse.success("Request processed successfully", response));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", null));
    }
}
