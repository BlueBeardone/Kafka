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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

        when(fischlDefenderConnector.create(any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));

        ApiResponse response = defenderApi.createDefender(defender);
        assertTrue(response.isSuccess());
    }

    @Test
    void createDefender_nullType_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                defenderApi.createDefender(new TsaritsaDefenderApi.Defender().setBastionId("b-1")));
    }

    @Test
    void createDefender_nullBastionId_throwsException() {
        assertThrows(InvalidRequestException.class, () ->
                defenderApi.createDefender(new TsaritsaDefenderApi.Defender().setType("knight")));
    }

    @Test
    void getDefender_returnsSuccess() throws Exception {
        when(fischlDefenderConnector.read("d-1"))
                .thenReturn(new FischlDefender().setId("d-1").setName("Knight"));

        ApiResponse response = defenderApi.getDefender("d-1");
        assertTrue(response.isSuccess());
    }

    @Test
    void updateDefender_returnsSuccess() throws Exception {
        when(fischlDefenderConnector.update(eq("d-1"), any(FischlDefender.class)))
                .thenReturn(new FischlDefender().setId("d-1"));

        ApiResponse response = defenderApi.updateDefender("d-1",
                new TsaritsaDefenderApi.Defender().setType("beast").setBastionId("b-1"));
        assertTrue(response.isSuccess());
    }

    @Test
    void deleteDefender_returnsSuccess() throws Exception {
        doNothing().when(fischlDefenderConnector).delete("d-1");

        ApiResponse response = defenderApi.deleteDefender("d-1");
        assertTrue(response.isSuccess());
        verify(fischlDefenderConnector).delete("d-1");
    }
}