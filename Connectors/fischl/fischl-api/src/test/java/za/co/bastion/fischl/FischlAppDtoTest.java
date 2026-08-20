package za.co.bastion.fischl;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FischlAppDtoTest {

    @Test
    void constructorAndGettersSetters() {
        FischlApp app = new FischlApp();

        app.setSlug("fischl");
        app.setName("Fischl");
        app.setAllowedRoles(List.of("admin", "user"));

        assertEquals("fischl", app.getSlug());
        assertEquals("Fischl", app.getName());
        assertEquals(List.of("admin", "user"), app.getAllowedRoles());
    }

    @Test
    void allArgsConstructor() {
        FischlApp app = new FischlApp("fischl", "Fischl", List.of("admin"));

        assertEquals("fischl", app.getSlug());
        assertEquals("Fischl", app.getName());
        assertEquals(List.of("admin"), app.getAllowedRoles());
    }

    @Test
    void noArgsConstructor() {
        FischlApp app = new FischlApp();
        assertNull(app.getSlug());
        assertNull(app.getName());
        assertNull(app.getAllowedRoles());
    }

    @Test
    void chainSetters() {
        FischlApp app = new FischlApp()
                .setSlug("fischl")
                .setName("Fischl")
                .setAllowedRoles(List.of("user"));

        assertEquals("fischl", app.getSlug());
        assertEquals("Fischl", app.getName());
        assertEquals(List.of("user"), app.getAllowedRoles());
    }
}