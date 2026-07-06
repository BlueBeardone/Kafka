package com.example.bastionmiddleware.service;

import com.example.bastionmiddleware.MauraBastion.api.BastionApi;
import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;
import org.springframework.stereotype.Service;

@Service
public class BastionService {
    private final BastionApi bastionApi;

    public BastionService(BastionApi bastionApi) {
        this.bastionApi = bastionApi;
    }

    public BastionResponse createBastion(BastionRequest request) {
        return bastionApi.createBastion(request);
    }

    public BastionResponse getBastion(Long id) {
        return bastionApi.getBastion(id);
    }
}
