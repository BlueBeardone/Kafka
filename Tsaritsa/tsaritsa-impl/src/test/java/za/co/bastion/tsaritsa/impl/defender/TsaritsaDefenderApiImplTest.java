package za.co.bastion.tsaritsa.impl.defender;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlDefender;
import za.co.bastion.fischl.defender.FischlDefenderConnector;
import za.co.bastion.tsaritsa.api.defender.TsaritsaDefenderApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TsaritsaDefenderApiImplTest {

    @Mock
    FischlDefenderConnector fischlDefenderConnector;

    TsaritsaDefenderApi defenderApi;

    @BeforeEach
    void setUp() throws Exception {
        defenderApi = new TsaritsaDefenderApiImpl();
        Field field = TsaritsaDefenderApiImpl.class.getDeclaredField("fischlDefenderConnector");
        field.setAccessible(true);
        field.set(defenderApi, fischlDefenderConnector);
    }

    @Test
    void createDefender_returnsSuccess() throws Exception {
        TsaritsaDefenderApi.Defender defender = new TsaritsaDefenderApi.Defender()
                .setType("knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(anyString(), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));

        ApiResponse response = defenderApi.createDefender("Bearer token", defender);
        assertTrue(response.isSuccess());
    }

    @Test
    void createDefender_forwardsAuthHeaderToConnector() throws Exception {
        TsaritsaDefenderApi.Defender defender = new TsaritsaDefenderApi.Defender()
                .setType("knight").setBastionId("b-1");

        when(fischlDefenderConnector.create(eq("Bearer token"), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));

        defenderApi.createDefender("Bearer token", defender);

        verify(fischlDefenderConnector).create(eq("Bearer token"), any(FischlDefender.class));
    }

    @Test
    void createDefender_nullType_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                defenderApi.createDefender("Bearer token",
                        new TsaritsaDefenderApi.Defender().setBastionId("b-1")));
    }

    @Test
    void createDefender_nullBastionId_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                defenderApi.createDefender("Bearer token",
                        new TsaritsaDefenderApi.Defender().setType("knight")));
    }

    @Test
    void getDefender_returnsSuccess() throws Exception {
        when(fischlDefenderConnector.read(anyString(), eq("d-1")))
                .thenReturn(new FischlDefender().setId("d-1").setName("Knight"));

        ApiResponse response = defenderApi.getDefender("Bearer token", "d-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void getDefender_forwardsAuthHeaderToConnector() throws Exception {
        when(fischlDefenderConnector.read(eq("Bearer token"), eq("d-1")))
                .thenReturn(new FischlDefender().setId("d-1"));

        defenderApi.getDefender("Bearer token", "d-1");

        verify(fischlDefenderConnector).read("Bearer token", "d-1");
    }

    @Test
    void getAllDefenders_returnsSuccess() throws Exception {
        when(fischlDefenderConnector.readAll(anyString()))
                .thenReturn(List.of(new FischlDefender().setId("d-1").setName("Knight")));

        ApiResponse response = defenderApi.getAllDefenders("Bearer token");
        assertTrue(response.isSuccess());

        verify(fischlDefenderConnector).readAll("Bearer token");
    }

    @Test
    void updateDefender_returnsSuccess() throws Exception {
        when(fischlDefenderConnector.update(anyString(), eq("d-1"), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));

        ApiResponse response = defenderApi.updateDefender("Bearer token", "d-1",
                new TsaritsaDefenderApi.Defender().setType("beast").setBastionId("b-1"));
        assertTrue(response.isSuccess());
    }

    @Test
    void updateDefender_forwardsAuthHeaderToConnector() throws Exception {
        when(fischlDefenderConnector.update(eq("Bearer token"), eq("d-1"), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));

        defenderApi.updateDefender("Bearer token", "d-1",
                new TsaritsaDefenderApi.Defender().setType("beast").setBastionId("b-1"));

        verify(fischlDefenderConnector).update(eq("Bearer token"), eq("d-1"), any(FischlDefender.class));
    }

    @Test
    void deleteDefender_returnsSuccess() throws Exception {
        doNothing().when(fischlDefenderConnector).delete(anyString(), eq("d-1"));

        ApiResponse response = defenderApi.deleteDefender("Bearer token", "d-1");
        assertTrue(response.isSuccess());
        verify(fischlDefenderConnector).delete("Bearer token", "d-1");
    }
}
