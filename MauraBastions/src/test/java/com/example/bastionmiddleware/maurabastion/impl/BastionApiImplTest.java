package com.example.bastionmiddleware.maurabastion.api.impl;

import com.example.bastionmiddleware.connector.FischlConnector;
import com.example.bastionmiddleware.connector.model.RepositoryBastionDto;
import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;
import com.example.bastionmiddleware.exception.InvalidRequestException;
import com.example.bastionmiddleware.maurabastion.impl.mapper.BastionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BastionApiImplTest {

    @Mock
    private BastionMapper mapper;

    @Mock
    private FischlConnector fischlConnector;

    @InjectMocks
    private BastionApiImpl service;

    @Test
    void shouldAcceptValidRequest() {
        BastionRequest request = new BastionRequest("Mara", "D", "Barracks", 3, 2);
        BastionResponse response = new BastionResponse(1L, "Mara", "D", "Barracks", 3, 2);

        when(mapper.toRepositoryDto(any(BastionRequest.class))).thenReturn(new RepositoryBastionDto(1L, "Mara", "D", "Barracks", 3, 2));
        when(fischlConnector.createBastion(any())).thenReturn(response);

        BastionResponse result = service.createBastion(request);

        assertNotNull(result);
        assertEquals("Mara", result.ownerName());
    }

    @Test
    void shouldRejectBlankOwnerName() {
        BastionRequest request = new BastionRequest("   ", "D", "Barracks", 3, 2);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> service.createBastion(request));
        assertTrue(exception.getMessage().contains("ownerName"));
    }

    @Test
    void shouldRejectInvalidRank() {
        BastionRequest request = new BastionRequest("Mara", "Z", "Barracks", 3, 2);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> service.createBastion(request));
        assertTrue(exception.getMessage().contains("rank"));
    }
}
