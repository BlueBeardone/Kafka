package za.co.bastion.common;

import org.junit.jupiter.api.Test;
import za.co.bastion.common.dto.FacilityTable;
import za.co.bastion.common.dto.Rank;
import za.co.bastion.common.exception.BackendException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonInfrastructureTest {

    @Test
    void BackendException_constructor_setsFieldsCorrectly() {
        BackendException exception = new BackendException(404, "Not Found");

        assertEquals(404, exception.getBackendStatus());
        assertEquals("Not Found", exception.getBackendMessage());
        assertEquals("Not Found", exception.getMessage());
    }

    @Test
    void BackendException_isRuntimeException() {
        BackendException exception = new BackendException(500, "Internal Error");
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void FacilityTable_constructorAndGettersSetters() {
        FacilityTable table = new FacilityTable();

        List<List<String>> data = List.of(List.of("a", "b"), List.of("c", "d"));
        table.setTable(data);

        assertEquals(data, table.getTable());
    }

    @Test
    void FacilityTable_allArgsConstructor() {
        List<List<String>> data = List.of(List.of("1", "2"));
        FacilityTable table = new FacilityTable(data);

        assertEquals(data, table.getTable());
    }

    @Test
    void FacilityTable_noArgsConstructor() {
        FacilityTable table = new FacilityTable();
        assertNull(table.getTable());
    }

    @Test
    void FacilityTable_chainSetters() {
        List<List<String>> data = List.of(List.of("x"));
        FacilityTable table = new FacilityTable().setTable(data);

        assertEquals(data, table.getTable());
    }

    @Test
    void Rank_enumValuesAreLowercase() {
        assertEquals("s", Rank.s.name());
        assertEquals("a", Rank.a.name());
        assertEquals("b", Rank.b.name());
        assertEquals("c", Rank.c.name());
        assertEquals("d", Rank.d.name());
    }

    @Test
    void Rank_hasAllExpectedValues() {
        Rank[] values = Rank.values();
        assertEquals(5, values.length);
        assertTrue(values[0].name().equals("s"));
    }
}