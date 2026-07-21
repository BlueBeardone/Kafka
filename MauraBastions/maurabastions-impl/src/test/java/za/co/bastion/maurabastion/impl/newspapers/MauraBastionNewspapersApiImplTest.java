package za.co.bastion.maurabastion.impl.newspapers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlArticle;
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

    @Captor
    ArgumentCaptor<FischlArticle> fischlCaptor;

    MauraBastionNewspapersApi newspapersApi;

    @BeforeEach
    void setUp() throws Exception {
        newspapersApi = new MauraBastionNewspapersApiImpl();
        Field field = MauraBastionNewspapersApiImpl.class.getDeclaredField("fischlNewspapersConnector");
        field.setAccessible(true);
        field.set(newspapersApi, fischlNewspapersConnector);
    }

    @Test
    void createNewspaperArticle_withValidData_returnsSuccess() throws Exception {
        MauraBastionNewspapersApi.NewspaperArticle article = new MauraBastionNewspapersApi.NewspaperArticle()
                .setTitle("Breaking News").setContent("Something happened")
                .setImageUrl("http://example.com/img.png").setAuthor("John Doe");

        when(fischlNewspapersConnector.create(any(FischlArticle.class)))
                .thenReturn(new FischlArticle().setTitle("Breaking News").setContent("Something happened"));

        ApiResponse response = newspapersApi.createNewspaperArticle(article);
        assertTrue(response.isSuccess());
    }

    @Test
    void createNewspaperArticle_mapsToFischlArticleCorrectly() throws Exception {
        MauraBastionNewspapersApi.NewspaperArticle article = new MauraBastionNewspapersApi.NewspaperArticle()
                .setTitle("Breaking News").setContent("Something happened")
                .setImageUrl("http://example.com/img.png").setAuthor("John Doe");

        when(fischlNewspapersConnector.create(fischlCaptor.capture()))
                .thenReturn(new FischlArticle());

        newspapersApi.createNewspaperArticle(article);

        FischlArticle captured = fischlCaptor.getValue();
        assertEquals("Breaking News", captured.getTitle());
        assertEquals("Something happened", captured.getContent());
        assertEquals("http://example.com/img.png", captured.getImageUrl());
        assertEquals("John Doe", captured.getAuthor());
    }

    @Test
    void createNewspaperArticle_mapsFromFischlArticleCorrectly() throws Exception {
        FischlArticle fischl = new FischlArticle()
                .setTitle("Breaking News").setContent("Something happened")
                .setImageUrl("http://example.com/img.png").setAuthor("John Doe");

        when(fischlNewspapersConnector.create(any(FischlArticle.class)))
                .thenReturn(fischl);

        MauraBastionNewspapersApi.NewspaperArticle input = new MauraBastionNewspapersApi.NewspaperArticle()
                .setTitle("Breaking News").setContent("Something happened");

        ApiResponse response = newspapersApi.createNewspaperArticle(input);
        assertTrue(response.isSuccess());

        MauraBastionNewspapersApi.NewspaperArticle result = (MauraBastionNewspapersApi.NewspaperArticle) response.getData();
        assertEquals("Breaking News", result.getTitle());
        assertEquals("Something happened", result.getContent());
        assertEquals("http://example.com/img.png", result.getImageUrl());
        assertEquals("John Doe", result.getAuthor());
    }

    @Test
    void createNewspaperArticle_blankTitle_throwsException() {
        MauraBastionNewspapersApi.NewspaperArticle article = new MauraBastionNewspapersApi.NewspaperArticle()
                .setTitle("").setContent("Something happened");

        assertThrows(InvalidRequestException.class, () -> newspapersApi.createNewspaperArticle(article));
    }

    @Test
    void createNewspaperArticle_nullTitle_throwsException() {
        MauraBastionNewspapersApi.NewspaperArticle article = new MauraBastionNewspapersApi.NewspaperArticle()
                .setTitle(null).setContent("Something happened");

        assertThrows(InvalidRequestException.class, () -> newspapersApi.createNewspaperArticle(article));
    }

    @Test
    void createNewspaperArticle_blankContent_throwsException() {
        MauraBastionNewspapersApi.NewspaperArticle article = new MauraBastionNewspapersApi.NewspaperArticle()
                .setTitle("Title").setContent("");

        assertThrows(InvalidRequestException.class, () -> newspapersApi.createNewspaperArticle(article));
    }

    @Test
    void createNewspaperArticle_nullContent_throwsException() {
        MauraBastionNewspapersApi.NewspaperArticle article = new MauraBastionNewspapersApi.NewspaperArticle()
                .setTitle("Title").setContent(null);

        assertThrows(InvalidRequestException.class, () -> newspapersApi.createNewspaperArticle(article));
    }

    @Test
    void createNewspaperArticle_optionalFields_canBeNull() throws Exception {
        MauraBastionNewspapersApi.NewspaperArticle article = new MauraBastionNewspapersApi.NewspaperArticle()
                .setTitle("Title").setContent("Content");

        when(fischlNewspapersConnector.create(any(FischlArticle.class)))
                .thenReturn(new FischlArticle().setTitle("Title").setContent("Content"));

        ApiResponse response = newspapersApi.createNewspaperArticle(article);
        assertTrue(response.isSuccess());
    }

    @Test
    void getAllNewspaperArticles_returnsSuccess() throws Exception {
        when(fischlNewspapersConnector.readAll())
                .thenReturn(List.of(new FischlArticle().setTitle("Breaking News").setContent("Content")));

        ApiResponse response = newspapersApi.getAllNewspaperArticles();
        assertTrue(response.isSuccess());
    }

    @Test
    void getAllNewspaperArticles_mapsItemsCorrectly() throws Exception {
        FischlArticle fischl = new FischlArticle()
                .setTitle("Breaking News").setContent("Something happened")
                .setImageUrl("http://example.com/img.png").setAuthor("John Doe");

        when(fischlNewspapersConnector.readAll())
                .thenReturn(List.of(fischl));

        ApiResponse response = newspapersApi.getAllNewspaperArticles();
        assertTrue(response.isSuccess());

        @SuppressWarnings("unchecked")
        List<MauraBastionNewspapersApi.NewspaperArticle> results = (List<MauraBastionNewspapersApi.NewspaperArticle>) response.getData();
        assertEquals(1, results.size());
        assertEquals("Breaking News", results.get(0).getTitle());
        assertEquals("Something happened", results.get(0).getContent());
        assertEquals("http://example.com/img.png", results.get(0).getImageUrl());
        assertEquals("John Doe", results.get(0).getAuthor());
    }
}