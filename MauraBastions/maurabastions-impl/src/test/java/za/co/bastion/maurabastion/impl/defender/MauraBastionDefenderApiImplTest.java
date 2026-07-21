package za.co.bastion.maurabastion.impl.defender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlDefender;
import za.co.bastion.fischl.defender.FischlDefenderConnector;
import za.co.bastion.maurabastion.api.defender.MauraBastionDefenderApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionDefenderApiImplTest {

    @Mock
    FischlDefenderConnector fischlDefenderConnector;

    @Captor
    ArgumentCaptor<FischlDefender> fischlCaptor;

    MauraBastionDefenderApi defenderApi;

    @BeforeEach
    void setUp() throws Exception {
        defenderApi = new MauraBastionDefenderApiImpl();
        Field field = MauraBastionDefenderApiImpl.class.getDeclaredField("fischlDefenderConnector");
        field.setAccessible(true);
        field.set(defenderApi, fischlDefenderConnector);
    }

    @Test
    void createDefender_withValidData_returnsSuccess() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setName("Knight").setDescription("A brave knight")
                .setType("knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1").setName("Knight"));

        ApiResponse response = defenderApi.createDefender(defender);
        assertTrue(response.isSuccess());
    }

    @Test
    void createDefender_mapsToFischlDefenderCorrectly() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setName("Beast").setDescription("A fierce beast")
                .setImgUrl("http://example.com/img.png").setType("beast")
                .setBastionId("b-1").setAcquisitionStory("Found in the wild");

        when(fischlDefenderConnector.create(fischlCaptor.capture()))
                .thenReturn(new FischlDefender());

        defenderApi.createDefender(defender);

        FischlDefender captured = fischlCaptor.getValue();
        assertEquals("d-1", captured.getId());
        assertEquals("Beast", captured.getName());
        assertEquals("A fierce beast", captured.getDescription());
        assertEquals("http://example.com/img.png", captured.getImgUrl());
        assertEquals("beast", captured.getType());
        assertEquals("b-1", captured.getBastionId());
        assertEquals("Found in the wild", captured.getAcquisitionStory());
    }

    @Test
    void createDefender_mapsDefenderFromFischlCorrectly() throws Exception {
        FischlDefender fischl = new FischlDefender()
                .setId("d-1").setName("Knight").setDescription("A knight")
                .setImgUrl("http://example.com/img.png").setType("knight")
                .setBastionId("b-1").setAcquisitionStory("Hired");

        when(fischlDefenderConnector.create(any(FischlDefender.class)))
                .thenReturn(fischl);

        MauraBastionDefenderApi.Defender input = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setName("Knight").setType("knight").setBastionId("b-1");

        ApiResponse response = defenderApi.createDefender(input);
        assertTrue(response.isSuccess());

        MauraBastionDefenderApi.Defender result = (MauraBastionDefenderApi.Defender) response.getData();
        assertEquals("d-1", result.getId());
        assertEquals("Knight", result.getName());
        assertEquals("A knight", result.getDescription());
        assertEquals("http://example.com/img.png", result.getImgUrl());
        assertEquals("knight", result.getType());
        assertEquals("b-1", result.getBastionId());
        assertEquals("Hired", result.getAcquisitionStory());
    }

    @Test
    void createDefender_nullType_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setType(null).setBastionId("b-1");

        assertThrows(InvalidRequestException.class, () -> defenderApi.createDefender(defender));
    }

    @Test
    void createDefender_blankType_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setType("").setBastionId("b-1");

        assertThrows(InvalidRequestException.class, () -> defenderApi.createDefender(defender));
    }

    @Test
    void createDefender_nullBastionId_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setType("knight").setBastionId(null);

        assertThrows(InvalidRequestException.class, () -> defenderApi.createDefender(defender));
    }

    @Test
    void createDefender_blankBastionId_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-1").setType("knight").setBastionId("");

        assertThrows(InvalidRequestException.class, () -> defenderApi.createDefender(defender));
    }

    @Test
    void createDefender_optionalFields_canBeNull() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setId("d-2").setName("Guard").setType("bastion_defender").setBastionId("b-1");

        when(fischlDefenderConnector.create(any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-2"));

        ApiResponse response = defenderApi.createDefender(defender);
        assertTrue(response.isSuccess());
    }

    @Test
    void getDefender_returnsSuccess() throws Exception {
        when(fischlDefenderConnector.read("d-1"))
                .thenReturn(new FischlDefender().setId("d-1").setName("Knight"));

        ApiResponse response = defenderApi.getDefender("d-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void getDefender_nullId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> defenderApi.getDefender(null));
    }

    @Test
    void getDefender_blankId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> defenderApi.getDefender(""));
    }

    @Test
    void getAllDefenders_returnsSuccess() throws Exception {
        when(fischlDefenderConnector.readAll())
                .thenReturn(List.of(new FischlDefender().setId("d-1").setName("Knight")));

        ApiResponse response = defenderApi.getAllDefenders();
        assertTrue(response.isSuccess());
    }

    @Test
    void updateDefender_returnsSuccess() throws Exception {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setName("Updated").setType("beast").setBastionId("b-1");

        when(fischlDefenderConnector.update(eq("d-1"), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1").setName("Updated"));

        ApiResponse response = defenderApi.updateDefender("d-1", defender);
        assertTrue(response.isSuccess());
    }

    @Test
    void updateDefender_nullId_throwsException() {
        MauraBastionDefenderApi.Defender defender = new MauraBastionDefenderApi.Defender()
                .setType("knight").setBastionId("b-1");

        assertThrows(InvalidRequestException.class, () -> defenderApi.updateDefender(null, defender));
    }

    @Test
    void deleteDefender_returnsSuccess() throws Exception {
        doNothing().when(fischlDefenderConnector).delete("d-1");

        ApiResponse response = defenderApi.deleteDefender("d-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteDefender_nullId_throwsException() {
        assertThrows(InvalidRequestException.class, () -> defenderApi.deleteDefender(null));
    }
}
