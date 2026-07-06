package com.example.bastionmiddleware.maurabastion.api;

import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;

public interface BastionApi {
    BastionResponse createBastion(BastionRequest request);

    BastionResponse getBastion(Long id);
}
