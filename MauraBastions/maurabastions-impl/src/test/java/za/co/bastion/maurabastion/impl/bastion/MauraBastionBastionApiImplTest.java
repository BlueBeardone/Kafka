package za.co.bastion.maurabastion.impl.bastion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.bastion.common.dto.ApiResponse;
import za.co.bastion.common.dto.FacilityTable;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.common.exception.InvalidRequestException;
import za.co.bastion.fischl.FischlBastion;
import za.co.bastion.fischl.FischlDefender;
import za.co.bastion.fischl.FischlFacility;
import za.co.bastion.fischl.FischlHireling;
import za.co.bastion.fischl.bastion.FischlBastionConnector;
import za.co.bastion.maurabastion.api.bastion.MauraBastionBastionApi;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MauraBastionBastionApiImplTest {

    @Mock
    FischlBastionConnector fischlBastionConnector;

    MauraBastionBastionApi bastionApi;

    @BeforeEach
    void setUp() throws Exception {
        bastionApi = new MauraBastionBastionApiImpl();
        Field field = MauraBastionBastionApiImpl.class.getDeclaredField("fischlBastionConnector");
        field.setAccessible(true);
        field.set(bastionApi, fischlBastionConnector);
    }

    @Test
    void createBastion_withValidData_returnsSuccess() throws Exception {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setName("Test Bastion").setDescription("A test bastion");

        when(fischlBastionConnector.create(any(FischlBastion.class)))
                .thenReturn(new FischlBastion().setId("b-1").setName("Test Bastion"));

        ApiResponse response = bastionApi.createBastion(bastion);
        assertTrue(response.isSuccess());
    }

    @Test
    void createBastion_withoutId_returnsSuccess() throws Exception {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setName("Test Bastion").setDescription("A test bastion");

        when(fischlBastionConnector.create(any(FischlBastion.class)))
                .thenReturn(new FischlBastion().setId("b-1").setName("Test Bastion"));

        ApiResponse response = bastionApi.createBastion(bastion);
        assertTrue(response.isSuccess());
    }

    @Test
    void createBastion_withBlankName_throwsException() {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setName("").setDescription("A test");

        assertThrows(InvalidRequestException.class, () -> bastionApi.createBastion(bastion));
    }

    @Test
    void createBastion_withBlankDescription_throwsException() {
        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setName("Test").setDescription("");

        assertThrows(InvalidRequestException.class, () -> bastionApi.createBastion(bastion));
    }

    @Test
    void getAllBastions_returnsFullBastionWithDefendersAndHirelings() throws Exception {
        FischlDefender defender = new FischlDefender()
                .setId("d-1").setName("Guardian").setDescription("A sturdy defender")
                .setImgUrl("def.png").setType("melee").setBastionId("b-1").setAcquisitionStory("found in ruins");

        FischlHireling hireling = new FischlHireling()
                .setId("h-1").setName("Scout").setRole("lookout").setDescription("Sees far")
                .setImgUrl("hire.png").setBastionId("b-1").setFacilityId("f-1").setAcquisitionStory("hired in town");

        FischlBastion fischlBastion = new FischlBastion()
                .setId("b-1").setName("Test Bastion").setDescription("A bastion")
                .setDefenders(List.of(defender))
                .setHirelings(List.of(hireling));

        when(fischlBastionConnector.readAll())
                .thenReturn(List.of(fischlBastion));

        ApiResponse response = bastionApi.getAllBastions();
        assertTrue(response.isSuccess());

        @SuppressWarnings("unchecked")
        List<MauraBastionBastionApi.Bastion> bastions = (List<MauraBastionBastionApi.Bastion>) response.getData();
        assertEquals(1, bastions.size());
        MauraBastionBastionApi.Bastion result = bastions.get(0);

        assertNotNull(result.getDefenders());
        assertEquals(1, result.getDefenders().size());
        assertEquals("Guardian", result.getDefenders().get(0).getName());
        assertEquals("d-1", result.getDefenders().get(0).getId());
        assertEquals("melee", result.getDefenders().get(0).getType());
        assertEquals("b-1", result.getDefenders().get(0).getBastionId());
        assertEquals("found in ruins", result.getDefenders().get(0).getAcquisitionStory());

        assertNotNull(result.getHirelings());
        assertEquals(1, result.getHirelings().size());
        assertEquals("Scout", result.getHirelings().get(0).getName());
        assertEquals("h-1", result.getHirelings().get(0).getId());
        assertEquals("lookout", result.getHirelings().get(0).getRole());
        assertEquals("b-1", result.getHirelings().get(0).getBastionId());
        assertEquals("f-1", result.getHirelings().get(0).getFacilityId());
        assertEquals("hired in town", result.getHirelings().get(0).getAcquisitionStory());
    }

    @Test
    void createBastion_withFacilities_mapsCorrectly() throws Exception {
        MauraBastionBastionApi.Facility facility = new MauraBastionBastionApi.Facility()
                .setId("f-1").setName("Workshop").setRank(Rank.b)
                .setMinimumRequiredHirelings(2).setConstructionTurns(3).setCost(100)
                .setDescription("A workshop").setImgUrl("workshop.png")
                .setTable(new FacilityTable().setTable(List.of(List.of("wood", "5"))))
                .setConstructedTurns(1).setBastionId("b-1");

        MauraBastionBastionApi.Bastion bastion = new MauraBastionBastionApi.Bastion()
                .setName("Test").setDescription("A test")
                .setFacilities(List.of(facility));

        when(fischlBastionConnector.create(any(FischlBastion.class)))
                .thenAnswer(invocation -> {
                    FischlBastion fb = invocation.getArgument(0);
                    return fb.setId("b-1");
                });

        ApiResponse response = bastionApi.createBastion(bastion);
        assertTrue(response.isSuccess());
    }

    @Test
    void getBastion_returnsFullBastionWithDefendersAndHirelings() throws Exception {
        FischlDefender defender = new FischlDefender()
                .setId("d-1").setName("Guardian").setDescription("A sturdy defender")
                .setImgUrl("def.png").setType("melee").setBastionId("b-1").setAcquisitionStory("found in ruins");

        FischlHireling hireling = new FischlHireling()
                .setId("h-1").setName("Scout").setRole("lookout").setDescription("Sees far")
                .setImgUrl("hire.png").setBastionId("b-1").setFacilityId("f-1").setAcquisitionStory("hired in town");

        FischlFacility facility = new FischlFacility()
                .setId("f-1").setName("Workshop").setRank("b")
                .setMinimumRequiredHirelings(2).setConstructionTurns(3).setCost(100)
                .setDescription("A workshop").setImgUrl("workshop.png")
                .setTable(new FacilityTable().setTable(List.of(List.of("wood", "5"))))
                .setConstructedTurns(1).setBastionId("b-1");

        FischlBastion fischlBastion = new FischlBastion()
                .setId("b-1").setName("Test Bastion").setDescription("A bastion").setImgUrl("bastion.png")
                .setFacilities(List.of(facility))
                .setDefenders(List.of(defender))
                .setHirelings(List.of(hireling));

        when(fischlBastionConnector.read("b-1")).thenReturn(fischlBastion);

        ApiResponse response = bastionApi.getBastion("b-1");
        assertTrue(response.isSuccess());

        MauraBastionBastionApi.Bastion result = (MauraBastionBastionApi.Bastion) response.getData();
        assertEquals("b-1", result.getId());
        assertEquals("Test Bastion", result.getName());
        assertEquals("bastion.png", result.getImgUrl());

        assertNotNull(result.getFacilities());
        assertEquals(1, result.getFacilities().size());
        assertEquals("Workshop", result.getFacilities().get(0).getName());
        assertEquals(Rank.b, result.getFacilities().get(0).getRank());
        assertEquals(2, result.getFacilities().get(0).getMinimumRequiredHirelings());
        assertEquals(3, result.getFacilities().get(0).getConstructionTurns());
        assertEquals(100, result.getFacilities().get(0).getCost());
        assertNotNull(result.getFacilities().get(0).getTable());

        assertNotNull(result.getDefenders());
        assertEquals(1, result.getDefenders().size());
        assertEquals("Guardian", result.getDefenders().get(0).getName());
        assertEquals("melee", result.getDefenders().get(0).getType());

        assertNotNull(result.getHirelings());
        assertEquals(1, result.getHirelings().size());
        assertEquals("Scout", result.getHirelings().get(0).getName());
        assertEquals("lookout", result.getHirelings().get(0).getRole());
    }

    @Test
    void deleteBastion_returnsSuccess() throws Exception {
        doNothing().when(fischlBastionConnector).delete("b-1");

        ApiResponse response = bastionApi.deleteBastion("b-1");
        assertTrue(response.isSuccess());
    }
}