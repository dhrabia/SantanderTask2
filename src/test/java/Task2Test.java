import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

public class Task2Test {

    private String url = "http://jsonplaceholder.typicode.com/";
    private static int highest_userId;
    private static int highest_id;


    @Test
    @Order(1)
    public void getHighestUserIdTest() {
        Response response =
                when().get(url + "posts/");

        Assertions.assertEquals(200, response.statusCode());

        List<Integer> userIdList = response.jsonPath().get("userId");
        for (int i = 0; userIdList.size() > i; i++) {
            if (userIdList.get(i) > highest_userId) {
                highest_userId = userIdList.get(i);
            }
        }
        System.out.println("Highest userid: " + highest_userId);
    }

    @Test
    @Order(2)
    public void getHighestIdForEachUserIdTest() {
        Response response =
                when().get(url + "posts?userId=" + highest_userId);

        Assertions.assertEquals(200, response.statusCode());
        for (Object x : response.jsonPath().getList("userId")) {
            Assertions.assertEquals(highest_userId, x);
        }

        List<Integer> userIdList = response.jsonPath().get("id");
        for (int i = 0; userIdList.size() > i; i++) {
            if (userIdList.get(i) > highest_id) {
                highest_id = userIdList.get(i);
            }
        }
        System.out.println("Highest id for userId = " + highest_userId + " is : " + highest_id);
    }

    @Test
    @Order(3)
    public void postNewCommentTest() {

        Comment comment = new Comment(highest_id, "TestName1", "testemail@test.com", "Test message");
        given()
                .contentType("application/json")
                .body(comment)
                .when()
                .post(url + "comments")
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .assertThat()
                .body("name", equalTo("TestName1"));
    }
}
