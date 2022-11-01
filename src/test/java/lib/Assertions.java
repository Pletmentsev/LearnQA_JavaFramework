package lib;

import io.restassured.response.Response;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expcetedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals (expcetedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonByName(Response Response, String name, String expcetedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals (expcetedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertResponseTestEquals(Response Response, String expectedAnswer) {
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }

    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response code is not as expected"
        );
    }

    public static void assertJsonHasField(Response Response, String  expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasFields (Response Response, String[] expectedFiledNames) {
        for (String expectedFiledName : expectedFiledNames) {
            Assertions.assertJsonHasField(Response, expectedFiledName);
        }
    }

    public static void assertJsonHasNotField(Response Response, String unespectedFieldName){
        Response.then().body("$", not(hasKey(unespectedFieldName)));
    }

}
