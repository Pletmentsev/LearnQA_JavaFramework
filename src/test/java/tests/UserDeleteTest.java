package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

@Epic("Delete user cases")
@Feature("Delete User")


public class UserDeleteTest extends BaseTestCase {
    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This case tries delete user with id=2")
    @DisplayName("Delete user id=2")
    public void testDeleteUserId2() {

        //LOGIN AS USER WITH ID=2
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");

        //DELETE USER WITH ID=2
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(
                        "https://playground.learnqa.ru/api/user/2",
                        this.header,
                        this.cookie
                );

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTestEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("This case checks successfully delete user")
    @DisplayName("Successfully delete user")
    public void testDeleteUser() {

        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");

        //DELETE
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.header,
                        this.cookie
                );

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest (
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.header,
                        this.cookie
                );
        Assertions.assertResponseCodeEquals(responseUserData, 404);
        Assertions.assertResponseTestEquals(responseUserData, "User not found");
    }

    @Test
    @Description("This case checks delete user as another user")
    @DisplayName("Delete user as another user")
    public void testDeleteUserAsAnotherUser() {

        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId1 = responseCreateAuth.jsonPath().getString("id");
        int i=Integer.parseInt(userId1)-1;
        String previousUserId=String.valueOf(i);

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.cookie = this.getCookie(responseGetAuth, "auth_sid");

        //DELETE ANOTHER USER
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest(
                        "https://playground.learnqa.ru/api/user/" + previousUserId,
                        this.header,
                        this.cookie
                );

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400); // While running test response will be wrong
    }
}
