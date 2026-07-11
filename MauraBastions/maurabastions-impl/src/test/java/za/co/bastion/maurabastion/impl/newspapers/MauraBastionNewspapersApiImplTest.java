package za.co.bastion.maurabastion.impl.newspapers;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.fischl.FischlNewspaper;
import za.co.bastion.fischl.newspapers.FischlNewspapersConnector;
import za.co.bastion.maurabastion.api.newspapers.MauraBastionNewspapersApi;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class MauraBastionNewspapersApiImplTest {

    @Inject
    MauraBastionNewspapersApi newspapersApi;

    @InjectMock
    FischlNewspapersConnector fischlNewspapersConnector;

    @Test
    void createNewspaper_withValidData_returnsSuccess() throws Exception {
        MauraBastionNewspapersApi.Newspaper newspaper = new MauraBastionNewspapersApi.Newspaper()
                .setId("n-1").setNewspaperName("Daily Bugle").setDate("2024-01-01").setEdition("Morning");

        when(fischlNewspapersConnector.create(any(FischlNewspaper.class)))
                .thenReturn(new FischlNewspaper().setId("n-1").setNewspaperName("Daily Bugle"));

        ApiResponse response = newspapersApi.createNewspaper(newspaper);
        assertTrue(response.isSuccess());
    }

    @Test
    void createNewspaper_withBlankName_throwsException() {
        MauraBastionNewspapersApi.Newspaper newspaper = new MauraBastionNewspapersApi.Newspaper()
                .setId("n-1").setNewspaperName("").setDate("2024-01-01").setEdition("Morning");

        assertThrows(RuntimeException.class, () -> newspapersApi.createNewspaper(newspaper));
    }

    @Test
    void getAllNewspapers_returnsSuccess() throws Exception {
        when(fischlNewspapersConnector.readAll())
                .thenReturn(List.of(new FischlNewspaper().setId("n-1").setNewspaperName("Daily Bugle")));

        ApiResponse response = newspapersApi.getAllNewspapers();
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteNewspaper_returnsSuccess() throws Exception {
        doNothing().when(fischlNewspapersConnector).delete("n-1");

        ApiResponse response = newspapersApi.deleteNewspaper("n-1");
        assertTrue(response.isSuccess());
    }
}
