package za.co.bastion.maurabastion.impl.newspapers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.fischl.FischlNewspaper;
import za.co.bastion.fischl.newspapers.FischlNewspapersConnector;
import za.co.bastion.maurabastion.api.newspapers.MauraBastionNewspapersApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionNewspapersApiImplTest {

    @Mock
    FischlNewspapersConnector fischlNewspapersConnector;

    MauraBastionNewspapersApi newspapersApi;

    @BeforeEach
    void setUp() throws Exception {
        newspapersApi = new MauraBastionNewspapersApiImpl();
        Field field = MauraBastionNewspapersApiImpl.class.getDeclaredField("fischlNewspapersConnector");
        field.setAccessible(true);
        field.set(newspapersApi, fischlNewspapersConnector);
    }

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
