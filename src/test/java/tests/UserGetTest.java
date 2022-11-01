package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

@Epic("Get user information cases")
@Feature("User Information")

public class UserGetTest extends BaseTestCase {
    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This case tries get information as unauthorised user")
    @DisplayName("Unauthorized user")
    public void testGetUserDataNotAuth() {
        Response responseUserData = apiCoreRequests
                .makeGetSimpleRequest("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    @Description("This test get information about yourself as an authorized user")
    @DisplayName("Yourself information as authorized user")
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/2",
                        this.header,
                        this.cookie
                );


        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    @Description("This test get another user name as an authorized user")
    @DisplayName("Another user name as authorized user")
    public void testGetUserNameAuthAsAnotherUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests
                .makeGetRequest(
                        "https://playground.learnqa.ru/api/user/1",
                        this.header,
                        this.cookie
                );

        String[] expectedFields = {"username"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }
}
