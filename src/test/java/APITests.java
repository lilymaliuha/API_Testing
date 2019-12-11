import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class APITests extends BaseTest {

    private String key = "4cc72de0acd6b845e2d1845d038fd139";
    private String token = "1460bb008f58e6c47205ed08301369b247fc94b3a17f93ee83d495d25fc75432";
    private String baseURL = "https://api.trello.com/1";
    private String boardName = "board" + getRandomNumber(1, 1000);
    private String listName = "list" + getRandomNumber(1, 1000);
    private String updatedListName = listName + "Updated";
    private String boardId;
    private String listId;

    @Test (priority = 0)
    public void createANewBoard() {
        String createBoardURL = baseURL + "/boards/?key=" + key + "&token=" + token;
        RequestSpecification httpRequest = RestAssured.given();

        JSONObject boardObject = new JSONObject();
        boardObject.put("name", boardName);

        httpRequest.contentType(ContentType.JSON);
        httpRequest.body(boardObject.toJSONString());
        Response response = httpRequest.request(Method.POST, createBoardURL);

        assertEquals(response.getStatusCode(), 200,
                "Status code differs from the expected one. Actual status code is " + response.getStatusCode());
        assertEquals(response.jsonPath().get("name"), boardName,
                "Board with name " + boardName + " was not created.");

        boardId = response.jsonPath().get("id");
    }

    @Test (priority = 1)
    public void createANewList() {
        String createListURL = baseURL + "/lists/?name=" + listName + "&idBoard=" + boardId + "&key=" + key + "&token=" + token;
        RequestSpecification httpRequest = RestAssured.given();

        JSONObject boardObject = new JSONObject();
        boardObject.put("name", listName);
        boardObject.put("idBoard", boardId);

        httpRequest.contentType(ContentType.JSON);
        httpRequest.body(boardObject.toJSONString());
        Response response = httpRequest.request(Method.POST, createListURL);

        assertEquals(response.getStatusCode(), 200,
                "Status code differs from the expected one. Actual status code is " + response.getStatusCode());
        assertEquals(response.jsonPath().get("name"), listName,
                "List with name " + listName + " was not created.");

        listId = response.jsonPath().get("id");
    }

    @Test (priority = 2)
    public void updateListName() {
        String updateListName = baseURL + "/lists/" + listId + "/name?value=" + updatedListName + "&key=" + key + "&token=" + token;
        RequestSpecification httpRequest = RestAssured.given();

        JSONObject boardObject = new JSONObject();
        boardObject.put("value", updatedListName);

        httpRequest.contentType(ContentType.JSON);
        httpRequest.body(boardObject.toJSONString());

        Response response = httpRequest.request(Method.PUT, updateListName);

        assertEquals(response.getStatusCode(), 200,
                "Status code differs from the expected one. Actual status code is " + response.getStatusCode());
        assertEquals(response.jsonPath().get("name"), updatedListName,
                "List name was not updated.");
    }

    @Test (priority = 3)
    public void deleteBoard() {
    String deleteBoardURL = baseURL + "/boards/" + boardId + "/?key=" + key + "&token=" + token;
    RequestSpecification httpRequest = RestAssured.given();
    httpRequest.contentType(ContentType.JSON);

    Response response = httpRequest.request(Method.DELETE, deleteBoardURL);

    assertEquals(response.getStatusCode(), 200,
                "Status code differs from the expected one. Actual status code is " + response.getStatusCode());
    }
}
