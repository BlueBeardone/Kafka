package za.co.bastion.tsaritsa.impl.newspapers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlArticle;
import za.co.bastion.fischl.newspapers.FischlNewspapersConnector;
import za.co.bastion.tsaritsa.api.newspapers.TsaritsaNewspapersApi;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaNewspapersApiImplTest {

    @Mock
    FischlNewspapersConnector fischlNewspapersConnector;

    TsaritsaNewspapersApi newspapersApi;

    @BeforeEach
    void setUp() throws Exception {
        newspapersApi = new TsaritsaNewspapersApiImpl();
        Field field = TsaritsaNewspapersApiImpl.class.getDeclaredField("fischlNewspapersConnector");
        field.setAccessible(true);
        field.set(newspapersApi, fischlNewspapersConnector);
    }

    @Test
    void createNewspaperArticle_returnsSuccess() throws Exception {
        TsaritsaNewspapersApi.NewspaperArticle article = new TsaritsaNewspapersApi.NewspaperArticle()
                .setTitle("Headline").setContent("Body");

        when(fischlNewspapersConnector.create(ArgumentMatchers.eq("Bearer abc123"), any(FischlArticle.class)))
                .thenReturn(new FischlArticle().setId("n-1"));

        ApiResponse response = newspapersApi.createNewspaperArticle("Bearer abc123", article);
        assertTrue(response.isSuccess());
        verify(fischlNewspapersConnector).create(ArgumentMatchers.eq("Bearer abc123"), any(FischlArticle.class));
    }

    @Test
    void createNewspaperArticle_blankTitle_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                newspapersApi.createNewspaperArticle("Bearer abc123",
                        new TsaritsaNewspapersApi.NewspaperArticle().setContent("b")));
    }

    @Test
    void getAllNewspaperArticles_forwardsAuthHeaderToConnector() throws Exception {
        when(fischlNewspapersConnector.readAll(ArgumentMatchers.eq("Bearer abc123")))
                .thenReturn(java.util.List.of(new FischlArticle().setId("n-1").setTitle("Headline")));

        ApiResponse response = newspapersApi.getAllNewspaperArticles("Bearer abc123");
        assertTrue(response.isSuccess());
        verify(fischlNewspapersConnector).readAll("Bearer abc123");
    }

    @Test
    void getNewspaperArticle_returnsSuccess() throws Exception {
        when(fischlNewspapersConnector.read("n-1"))
                .thenReturn(new FischlArticle().setId("n-1").setTitle("Headline"));

        ApiResponse response = newspapersApi.getNewspaperArticle("n-1");
        assertTrue(response.isSuccess());
        TsaritsaNewspapersApi.NewspaperArticle result =
                (TsaritsaNewspapersApi.NewspaperArticle) response.getData();
        assertEquals("n-1", result.getId());
        assertEquals("Headline", result.getTitle());
    }

    @Test
    void getNewspaperArticle_nullId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> newspapersApi.getNewspaperArticle(null));
    }

    @Test
    void updateNewspaperArticle_returnsSuccess() throws Exception {
        when(fischlNewspapersConnector.update(eq("n-1"), any(FischlArticle.class)))
                .thenReturn(new FischlArticle().setId("n-1"));

        ApiResponse response = newspapersApi.updateNewspaperArticle("n-1",
                new TsaritsaNewspapersApi.NewspaperArticle().setTitle("New").setContent("Body"));
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteNewspaperArticle_returnsSuccess() throws Exception {
        doNothing().when(fischlNewspapersConnector).delete("n-1");

        ApiResponse response = newspapersApi.deleteNewspaperArticle("n-1");
        assertTrue(response.isSuccess());
        verify(fischlNewspapersConnector).delete("n-1");
    }
}