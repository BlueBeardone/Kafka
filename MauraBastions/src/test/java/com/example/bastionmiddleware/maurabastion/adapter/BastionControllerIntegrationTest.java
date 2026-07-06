package com.example.bastionmiddleware.maurabastion.adapter;

import com.example.bastionmiddleware.maurabastion.api.BastionApi;
import com.example.bastionmiddleware.dto.BastionRequest;
import com.example.bastionmiddleware.dto.BastionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BastionController.class)
class BastionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BastionApi bastionApi;

    @Test
    void shouldReturnBadRequestForInvalidPayload() throws Exception {
        String body = """
                {"ownerName":"   ","rank":"D","facilityType":"Barracks","constructionTurns":3,"hirelingsCount":2}
                """;

        mockMvc.perform(post("/api/v1/bastions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateBastion() throws Exception {
        when(bastionApi.createBastion(any(BastionRequest.class))).thenReturn(
                new BastionResponse(1L, "Mara", "D", "Barracks", 3, 2));

        String body = """
                {"ownerName":"Mara","rank":"D","facilityType":"Barracks","constructionTurns":3,"hirelingsCount":2}
                """;

        mockMvc.perform(post("/api/v1/bastions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.ownerName").value("Mara"));
    }
}
