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

@Epic("Edit user information cases")
@Feature("Edit User")

public class UserEditTest extends BaseTestCase {
    String cookie;
    String header;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("This case successfully edit user")
    @DisplayName("Successfully edit user")
    public void testEditJustCreatedTest() {
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

        //EDIT
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.header,
                        this.cookie,
                        editData
                );

        //GET
        Response responseUserData = apiCoreRequests
                .makeGetRequest (
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.header,
                        this.cookie
                );

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Description("This case ties edit user as unauthorized user")
    @DisplayName("Edit user unauthorized")
    public void testEditUnauthorised() {
        //GENERATE USER
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.jsonPath().getString("id");
        System.out.println(userId);

        //EDIT AS UNAUTHORIZED
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutUnauthorizedRequest(
                        "https://playground.learnqa.ru/api/user/" + userId, editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTestEquals(responseEditUser, "Auth token not supplied");
    }

    @Test
    @Description("This case edits user as another user")
    @DisplayName("Edit another user")
    public void testEditUserAsAnotherUser() {
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

        //EDIT ANOTHER USER
        String newName1 = "Changed Name1";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName1);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/" + previousUserId,
                        this.header,
                        this.cookie,
                        editData
                );

        Assertions.assertResponseCodeEquals(responseEditUser, 400); // While running test response will be wrong
    }

    @Test
    @Description("This case tries email editing")
    @DisplayName("Edit with wrong email format")
    public void testEditWithWrongFormatEmail() {
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

        //EDIT WITH WRONG EMAIL FORMAT
        String newEmail = "example.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.header,
                        this.cookie,
                        editData
                );

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTestEquals(responseEditUser, "Invalid email format");

    }

    @Test
    @Description("This case tries change to short name")
    @DisplayName("Short name edit user")
    public void testEditWithShortName() {
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

        //EDIT WITH TOO SHORT FIRSTNAME
        String newName = "C";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(
                        "https://playground.learnqa.ru/api/user/" + userId,
                        this.header,
                        this.cookie,
                        editData
                );

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertJsonByName(responseEditUser,"error", "Too short value for field firstName");
    }
}
