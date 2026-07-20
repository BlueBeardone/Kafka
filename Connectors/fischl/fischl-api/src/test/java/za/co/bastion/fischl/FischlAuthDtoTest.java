package za.co.bastion.fischl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FischlAuthDtoTest {

    @Test
    void FischlRegisterRequest_constructorAndGettersSetters() {
        FischlRegisterRequest request = new FischlRegisterRequest();

        request.setEmail("test@example.com");
        request.setUsername("testuser");
        request.setPassword("password123");

        assertEquals("test@example.com", request.getEmail());
        assertEquals("testuser", request.getUsername());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void FischlRegisterRequest_allArgsConstructor() {
        FischlRegisterRequest request = new FischlRegisterRequest("a@b.com", "user1", "pass1");

        assertEquals("a@b.com", request.getEmail());
        assertEquals("user1", request.getUsername());
        assertEquals("pass1", request.getPassword());
    }

    @Test
    void FischlRegisterRequest_noArgsConstructor() {
        FischlRegisterRequest request = new FischlRegisterRequest();
        assertNull(request.getEmail());
        assertNull(request.getUsername());
        assertNull(request.getPassword());
    }

    @Test
    void FischlRegisterRequest_chainSetters() {
        FischlRegisterRequest request = new FischlRegisterRequest()
                .setEmail("c@d.com")
                .setUsername("user2")
                .setPassword("pass2");

        assertEquals("c@d.com", request.getEmail());
        assertEquals("user2", request.getUsername());
        assertEquals("pass2", request.getPassword());
    }

    @Test
    void FischlLoginRequest_constructorAndGettersSetters() {
        FischlLoginRequest request = new FischlLoginRequest();

        request.setEmail("login@example.com");
        request.setPassword("loginpass");

        assertEquals("login@example.com", request.getEmail());
        assertEquals("loginpass", request.getPassword());
    }

    @Test
    void FischlLoginRequest_allArgsConstructor() {
        FischlLoginRequest request = new FischlLoginRequest("x@y.com", "mypass");

        assertEquals("x@y.com", request.getEmail());
        assertEquals("mypass", request.getPassword());
    }

    @Test
    void FischlLoginRequest_noArgsConstructor() {
        FischlLoginRequest request = new FischlLoginRequest();
        assertNull(request.getEmail());
        assertNull(request.getPassword());
    }

    @Test
    void FischlLoginRequest_chainSetters() {
        FischlLoginRequest request = new FischlLoginRequest()
                .setEmail("chain@test.com")
                .setPassword("chainpass");

        assertEquals("chain@test.com", request.getEmail());
        assertEquals("chainpass", request.getPassword());
    }

    @Test
    void FischlAuthResponse_constructorAndGettersSetters() {
        FischlAuthResponse response = new FischlAuthResponse();

        FischlUser user = new FischlUser()
                .setId("user-1")
                .setDisplayName("Test User")
                .setBastionId("bastion-1");
        response.setUser(user);
        response.setToken("token-123");

        assertEquals(user, response.getUser());
        assertEquals("token-123", response.getToken());
    }

    @Test
    void FischlAuthResponse_allArgsConstructor() {
        FischlUser user = new FischlUser("u1", "User", "b1");
        FischlAuthResponse response = new FischlAuthResponse(user, "tok-1");

        assertEquals(user, response.getUser());
        assertEquals("tok-1", response.getToken());
    }

    @Test
    void FischlAuthResponse_noArgsConstructor() {
        FischlAuthResponse response = new FischlAuthResponse();
        assertNull(response.getUser());
        assertNull(response.getToken());
    }

    @Test
    void FischlAuthResponse_chainSetters() {
        FischlUser user = new FischlUser("u2", "User2", "b2");
        FischlAuthResponse response = new FischlAuthResponse()
                .setUser(user)
                .setToken("tok-2");

        assertEquals(user, response.getUser());
        assertEquals("tok-2", response.getToken());
    }
}