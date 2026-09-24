package za.co.bastion.maurabastion.impl.facilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.FacilityTable;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.common.exception.BackendException;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.FischlBastionException;
import za.co.bastion.fischl.FischlFacility;
import za.co.bastion.fischl.FischlFacilityException;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.fischl.facilities.FischlFacilitiesConnector;
import za.co.bastion.maurabastion.api.facilities.MauraBastionFacilitiesApi;
import za.co.bastion.maurabastion.impl.discord.DiscordWebhookSender;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionFacilitiesApiImplTest {

    @Mock
    FischlFacilitiesConnector fischlFacilitiesConnector;

    @Mock
    FischlBastionConnector fischlBastionConnector;

    @Mock
    DiscordWebhookSender webhookSender;

    @Captor
    ArgumentCaptor<FischlFacility> fischlCaptor;

    MauraBastionFacilitiesApi facilitiesApi;

    @BeforeEach
    void setUp() throws Exception {
        facilitiesApi = new MauraBastionFacilitiesApiImpl();
        inject("fischlFacilitiesConnector", fischlFacilitiesConnector);
        inject("fischlBastionConnector", fischlBastionConnector);
        inject("webhookSender", webhookSender);
    }

    private void inject(String name, Object value) throws Exception {
        Field field = MauraBastionFacilitiesApiImpl.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(facilitiesApi, value);
    }

    @Test
    void createFacility_withValidData_returnsSuccess() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Barracks").setDescription("A barracks")
                .setRank(Rank.a).setMinimumRequiredHirelings(5)
                .setConstructionTurns(3).setCost(100)
                .setImgUrl("http://example.com/img.png")
                .setTable(new FacilityTable().setTable(List.of(List.of("a", "b"))))
                .setConstructedTurns(2).setBastionId("b-1");

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1").setName("Barracks").setDescription("A barracks"));

        ApiResponse response = facilitiesApi.createFacility("Bearer token", facility);
        assertTrue(response.isSuccess());
    }

    @Test
    void createFacility_mapsToFischlFacilityCorrectly() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Barracks").setDescription("A barracks")
                .setRank(Rank.a).setMinimumRequiredHirelings(5)
                .setConstructionTurns(3).setCost(100)
                .setImgUrl("http://example.com/img.png")
                .setTable(new FacilityTable().setTable(List.of(List.of("a", "b"))))
                .setConstructedTurns(2).setBastionId("b-1");

        when(fischlFacilitiesConnector.create(anyString(), fischlCaptor.capture()))
                .thenReturn(new FischlFacility());

        facilitiesApi.createFacility("Bearer token", facility);

        FischlFacility captured = fischlCaptor.getValue();
        assertEquals("f-1", captured.getId());
        assertEquals("Barracks", captured.getName());
        assertEquals("a", captured.getRank());
        assertEquals(Integer.valueOf(5), captured.getMinimumRequiredHirelings());
        assertEquals(Integer.valueOf(3), captured.getConstructionTurns());
        assertEquals(Integer.valueOf(100), captured.getCost());
        assertEquals("http://example.com/img.png", captured.getImgUrl());
        assertNotNull(captured.getTable());
        assertEquals(Integer.valueOf(2), captured.getConstructedTurns());
        assertEquals("b-1", captured.getBastionId());
    }

    @Test
    void createFacility_mapsFacilityFromFischlCorrectly() throws Exception {
        FischlFacility fischl = new FischlFacility()
                .setId("f-1").setName("Barracks").setDescription("A barracks")
                .setRank("a").setMinimumRequiredHirelings(5)
                .setConstructionTurns(3).setCost(100)
                .setImgUrl("http://example.com/img.png")
                .setTable(new FacilityTable().setTable(List.of(List.of("a", "b"))))
                .setConstructedTurns(2).setBastionId("b-1");

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(fischl);

        MauraBastionFacilitiesApi.Facility input = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Barracks").setDescription("A barracks").setRank(Rank.a);

        ApiResponse response = facilitiesApi.createFacility("Bearer token", input);
        assertTrue(response.isSuccess());

        MauraBastionFacilitiesApi.Facility result = (MauraBastionFacilitiesApi.Facility) response.getData();
        assertEquals("f-1", result.getId());
        assertEquals("Barracks", result.getName());
        assertEquals("A barracks", result.getDescription());
        assertEquals(Rank.a, result.getRank());
        assertEquals(Integer.valueOf(5), result.getMinimumRequiredHirelings());
        assertEquals(Integer.valueOf(3), result.getConstructionTurns());
        assertEquals(Integer.valueOf(100), result.getCost());
        assertEquals("http://example.com/img.png", result.getImgUrl());
        assertNotNull(result.getTable());
        assertEquals(Integer.valueOf(2), result.getConstructedTurns());
        assertEquals("b-1", result.getBastionId());
    }

    @Test
    void createFacility_missingName_throwsException() {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("").setDescription("desc").setRank(Rank.a);

        assertThrows(InvalidRequestException.class, () -> facilitiesApi.createFacility("Bearer token", facility));
    }

    @Test
    void createFacility_nullName_throwsException() {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName(null).setDescription("desc").setRank(Rank.a);

        assertThrows(InvalidRequestException.class, () -> facilitiesApi.createFacility("Bearer token", facility));
    }

    @Test
    void createFacility_missingDescription_throwsException() {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Name").setDescription("").setRank(Rank.a);

        assertThrows(InvalidRequestException.class, () -> facilitiesApi.createFacility("Bearer token", facility));
    }

    @Test
    void createFacility_nullDescription_throwsException() {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Name").setDescription(null).setRank(Rank.a);

        assertThrows(InvalidRequestException.class, () -> facilitiesApi.createFacility("Bearer token", facility));
    }

    @Test
    void createFacility_missingRank_throwsException() {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Name").setDescription("desc").setRank(null);

        assertThrows(InvalidRequestException.class, () -> facilitiesApi.createFacility("Bearer token", facility));
    }

    @Test
    void createFacility_negativeMinimumRequiredHirelings_throwsException() {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Name").setDescription("desc").setRank(Rank.a)
                .setMinimumRequiredHirelings(-1);

        assertThrows(InvalidRequestException.class, () -> facilitiesApi.createFacility("Bearer token", facility));
    }

    @Test
    void createFacility_optionalFields_canBeNull() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-2").setName("Tower").setDescription("A tower").setRank(Rank.b);

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-2"));

        ApiResponse response = facilitiesApi.createFacility("Bearer token", facility);
        assertTrue(response.isSuccess());
    }

    @Test
    void createFacility_rankMapsToLowerCaseString() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Test").setDescription("desc").setRank(Rank.b);

        when(fischlFacilitiesConnector.create(anyString(), fischlCaptor.capture()))
                .thenReturn(new FischlFacility());

        facilitiesApi.createFacility("Bearer token", facility);

        assertEquals("b", fischlCaptor.getValue().getRank());
    }

    @Test
    void createFacility_stringRankMapsToRankEnum() throws Exception {
        FischlFacility fischl = new FischlFacility()
                .setId("f-1").setName("Test").setDescription("desc").setRank("c");

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(fischl);

        MauraBastionFacilitiesApi.Facility input = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Test").setDescription("desc").setRank(Rank.a);

        ApiResponse response = facilitiesApi.createFacility("Bearer token", input);
        MauraBastionFacilitiesApi.Facility result = (MauraBastionFacilitiesApi.Facility) response.getData();

        assertEquals(Rank.c, result.getRank());
    }

    @Test
    void createFacility_withBlankId_doesNotThrow() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("").setName("Test").setDescription("desc").setRank(Rank.a);

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(new FischlFacility());

        assertDoesNotThrow(() -> facilitiesApi.createFacility("Bearer token", facility));
    }

    @Test
    void createFacility_nullRankString_returnsNullRank() throws Exception {
        FischlFacility fischl = new FischlFacility()
                .setId("f-1").setName("Test").setDescription("desc").setRank(null);

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(fischl);

        MauraBastionFacilitiesApi.Facility input = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Test").setDescription("desc").setRank(Rank.a);

        ApiResponse response = facilitiesApi.createFacility("Bearer token", input);
        MauraBastionFacilitiesApi.Facility result = (MauraBastionFacilitiesApi.Facility) response.getData();

        assertNull(result.getRank());
    }

    @Test
    void getAllFacilities_returnsSuccess() throws Exception {
        when(fischlFacilitiesConnector.readAll(anyString()))
                .thenReturn(List.of(new FischlFacility().setId("f-1").setName("Barracks")));

        ApiResponse response = facilitiesApi.getAllFacilities("Bearer token");
        assertTrue(response.isSuccess());
    }

    @Test
    void createFacility_whenBackendFails_throwsBackendException() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setName("Test").setDescription("desc").setRank(Rank.a);

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenThrow(new FischlFacilityException().setStatus(404).setMessage("Not found"));

        BackendException ex = assertThrows(BackendException.class, () -> facilitiesApi.createFacility("Bearer token", facility));
        assertEquals(404, ex.getBackendStatus());
    }

    @Test
    void deleteFacility_returnsSuccess() throws Exception {
        doNothing().when(fischlFacilitiesConnector).delete(anyString(), eq("f-1"));

        ApiResponse response = facilitiesApi.deleteFacility("Bearer token", "f-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void createFacility_withThreadId_refreshesForumPost() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setName("Barracks").setDescription("A barracks").setRank(Rank.a).setBastionId("b-1");

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setName("Ravencrest")
                        .setDescription("desc").setDiscordThreadId("t-1")
                        .setFacilities(List.of(new FischlFacility().setName("Barracks"))));

        ApiResponse response = facilitiesApi.createFacility("Bearer token", facility);

        assertTrue(response.isSuccess());
        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION),
                eq("t-1"), contains("Barracks"));
    }

    @Test
    void createFacility_withoutThreadId_skipsForumEdit() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setName("Barracks").setDescription("A barracks").setRank(Rank.a).setBastionId("b-1");

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1"));

        ApiResponse response = facilitiesApi.createFacility("Bearer token", facility);

        assertTrue(response.isSuccess());
        verify(webhookSender, never()).editPost(any(), anyString(), anyString());
    }

    @Test
    void createFacility_whenBastionReadFails_stillReturnsSuccess() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setName("Barracks").setDescription("A barracks").setRank(Rank.a).setBastionId("b-1");

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenThrow(new FischlBastionException().setStatus(500).setMessage("boom"));

        ApiResponse response = facilitiesApi.createFacility("Bearer token", facility);

        assertTrue(response.isSuccess());
        verify(webhookSender, never()).editPost(any(), anyString(), anyString());
    }

    @Test
    void createFacility_whenForumEditFails_stillReturnsSuccess() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setName("Barracks").setDescription("A barracks").setRank(Rank.a).setBastionId("b-1");

        when(fischlFacilitiesConnector.create(anyString(), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1"));
        doThrow(new BackendException(502, "Discord webhook error: HTTP 404"))
                .when(webhookSender).editPost(any(), anyString(), anyString());

        ApiResponse response = facilitiesApi.createFacility("Bearer token", facility);

        assertTrue(response.isSuccess());
    }

    @Test
    void updateFacility_withThreadId_refreshesForumPost() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setName("Barracks").setDescription("Upgraded barracks").setRank(Rank.b).setBastionId("b-1");

        when(fischlFacilitiesConnector.update(anyString(), eq("f-1"), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1")
                        .setFacilities(List.of(new FischlFacility().setName("Barracks").setRank("b"))));

        ApiResponse response = facilitiesApi.updateFacility("Bearer token", "f-1", facility);

        assertTrue(response.isSuccess());
        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION),
                eq("t-1"), contains("Barracks"));
    }

    @Test
    void deleteFacility_withThreadId_refreshesForumPost() throws Exception {
        when(fischlFacilitiesConnector.read(anyString(), eq("f-1")))
                .thenReturn(new FischlFacility().setId("f-1").setBastionId("b-1"));
        doNothing().when(fischlFacilitiesConnector).delete(anyString(), eq("f-1"));
        when(fischlBastionConnector.read("Bearer token", "b-1"))
                .thenReturn(new FischlBastion().setId("b-1").setDiscordThreadId("t-1"));

        ApiResponse response = facilitiesApi.deleteFacility("Bearer token", "f-1");

        assertTrue(response.isSuccess());
        verify(webhookSender).editPost(eq(DiscordWebhookSender.Channel.BASTION_CREATION),
                eq("t-1"), anyString());
    }

    @Test
    void createFacility_forwardsAuthHeaderToConnector() throws Exception {
        MauraBastionFacilitiesApi.Facility facility = new MauraBastionFacilitiesApi.Facility()
                .setId("f-1").setName("Barracks").setDescription("A barracks").setRank(Rank.a);

        when(fischlFacilitiesConnector.create(eq("Bearer token"), any(FischlFacility.class)))
                .thenReturn(new FischlFacility().setId("f-1"));

        facilitiesApi.createFacility("Bearer token", facility);

        verify(fischlFacilitiesConnector).create(eq("Bearer token"), any(FischlFacility.class));
    }

    @Test
    void getFacility_forwardsAuthHeaderToConnector() throws Exception {
        when(fischlFacilitiesConnector.read(eq("Bearer token"), eq("f-1")))
                .thenReturn(new FischlFacility().setId("f-1"));

        facilitiesApi.getFacility("Bearer token", "f-1");

        verify(fischlFacilitiesConnector).read("Bearer token", "f-1");
    }
}