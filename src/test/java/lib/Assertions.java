package lib;

import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expcetedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals (expcetedValue, value, "JSON value is not equal to expected value");
    }
}
