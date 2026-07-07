package com.example.bastionmiddleware.maurabastion.adapter;

import com.example.bastionmiddleware.dto.ApiResponse;
import com.example.bastionmiddleware.dto.BastionDto;
import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;
import com.example.bastionmiddleware.dto.FacilityDto;
import com.example.bastionmiddleware.dto.NewspaperDataDto;
import com.example.bastionmiddleware.dto.UserDto;
import com.example.bastionmiddleware.maurabastion.api.BastionApi;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class BastionController {

    private final BastionApi bastionApi;

    public BastionController(BastionApi bastionApi) {
        this.bastionApi = bastionApi;
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserDto request) {
        UserDto response = bastionApi.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created", response));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable String id) {
        UserDto response = bastionApi.getUser(id);
        return ResponseEntity.ok(ApiResponse.success("User fetched", response));
    }

    @GetMapping("/bastions")
    public ResponseEntity<ApiResponse> getAllBastions() {
        List<BastionDto> response = bastionApi.getAllBastions();
        return ResponseEntity.ok(ApiResponse.success("Bastions fetched", response));
    }

    @GetMapping("/bastions/{id}")
    public ResponseEntity<ApiResponse> getBastion(@PathVariable String id) {
        BastionDto response = bastionApi.getBastionById(id);
        return ResponseEntity.ok(ApiResponse.success("Bastion fetched", response));
    }

    @PostMapping("/bastions")
    public ResponseEntity<ApiResponse> createBastion(@Valid @RequestBody BastionDto request) {
        BastionDto response = bastionApi.createBastion(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bastion created", response));
    }

    @PutMapping("/bastions/{id}")
    public ResponseEntity<ApiResponse> updateBastion(@PathVariable String id, @Valid @RequestBody BastionDto request) {
        BastionDto response = bastionApi.updateBastion(id, request);
        return ResponseEntity.ok(ApiResponse.success("Bastion updated", response));
    }

    @DeleteMapping("/bastions/{id}")
    public ResponseEntity<ApiResponse> deleteBastion(@PathVariable String id) {
        bastionApi.deleteBastion(id);
        return ResponseEntity.ok(ApiResponse.success("Bastion deleted", null));
    }

    @GetMapping("/facilities")
    public ResponseEntity<ApiResponse> getAllFacilities() {
        List<FacilityDto> response = bastionApi.getAllFacilities();
        return ResponseEntity.ok(ApiResponse.success("Facilities fetched", response));
    }

    @GetMapping("/facilities/{id}")
    public ResponseEntity<ApiResponse> getFacility(@PathVariable String id) {
        FacilityDto response = bastionApi.getFacilityById(id);
        return ResponseEntity.ok(ApiResponse.success("Facility fetched", response));
    }

    @PostMapping("/newspapers")
    public ResponseEntity<ApiResponse> createNewspaper(@Valid @RequestBody NewspaperDataDto request) {
        NewspaperDataDto response = bastionApi.createNewspaper(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Newspaper created", response));
    }

    @GetMapping("/newspapers")
    public ResponseEntity<ApiResponse> getAllNewspapers() {
        List<NewspaperDataDto> response = bastionApi.getAllNewspapers();
        return ResponseEntity.ok(ApiResponse.success("Newspapers fetched", response));
    }

    @GetMapping("/newspapers/{name}")
    public ResponseEntity<ApiResponse> getNewspaper(@PathVariable("name") String newspaperName) {
        NewspaperDataDto response = bastionApi.getNewspaperByName(newspaperName);
        return ResponseEntity.ok(ApiResponse.success("Newspaper fetched", response));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", null));
    }
}
