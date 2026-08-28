package za.co.bastion.fischl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FischlRoleDtoTest {

    @Test
    void constructorAndGettersSetters() {
        FischlRole role = new FischlRole();

        role.setName("admin");
        role.setDescription("Administrator");

        assertEquals("admin", role.getName());
        assertEquals("Administrator", role.getDescription());
    }

    @Test
    void allArgsConstructor() {
        FischlRole role = new FischlRole("user", "Regular user");

        assertEquals("user", role.getName());
        assertEquals("Regular user", role.getDescription());
    }

    @Test
    void noArgsConstructor() {
        FischlRole role = new FischlRole();
        assertNull(role.getName());
        assertNull(role.getDescription());
    }

    @Test
    void chainSetters() {
        FischlRole role = new FischlRole()
                .setName("admin")
                .setDescription("Administrator");

        assertEquals("admin", role.getName());
        assertEquals("Administrator", role.getDescription());
    }
}