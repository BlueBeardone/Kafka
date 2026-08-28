package za.co.bastion.assembly;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusIntegrationTest
class HealthRoutesIT {

    @Test
    void tsaritsaHealth_returnsSuccess() {
        given()
                .when().get("/tsaritsa/v1/health")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("Tsaritsa is running"));
    }

    @Test
    void mauraHealth_returnsSuccess() {
        given()
                .when().get("/maura/v1/health")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("Kafka is running"));
    }
}