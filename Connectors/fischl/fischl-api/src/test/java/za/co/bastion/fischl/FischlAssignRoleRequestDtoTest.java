package za.co.bastion.fischl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FischlAssignRoleRequestDtoTest {

    @Test
    void constructorAndGettersSetters() {
        FischlAssignRoleRequest request = new FischlAssignRoleRequest();

        request.setRole("admin");

        assertEquals("admin", request.getRole());
    }

    @Test
    void allArgsConstructor() {
        FischlAssignRoleRequest request = new FischlAssignRoleRequest("user");

        assertEquals("user", request.getRole());
    }

    @Test
    void noArgsConstructor() {
        FischlAssignRoleRequest request = new FischlAssignRoleRequest();
        assertNull(request.getRole());
    }

    @Test
    void chainSetters() {
        FischlAssignRoleRequest request = new FischlAssignRoleRequest().setRole("admin");

        assertEquals("admin", request.getRole());
    }
}