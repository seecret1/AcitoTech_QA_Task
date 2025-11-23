package api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class Specifications {

    public static RequestSpecification requestSpec(String baseUrl) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public static ResponseSpecification responseSpec_Code_200() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
    }

    public static ResponseSpecification responseSpec_Code_200_or_204() {
        return new ResponseSpecBuilder()
                .expectStatusCode(anyOf(is(200), is(204)))
                .build();
    }

    public static ResponseSpecification responseSpec_Code_400() {
        return new ResponseSpecBuilder()
                .expectStatusCode(400)
                .build();
    }

    public static ResponseSpecification responseSpec_Code_404() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .build();
    }

    public static ResponseSpecification responseSpec_Code_500() {
        return new ResponseSpecBuilder()
                .expectStatusCode(500)
                .build();
    }

    public static ResponseSpecification responseSpec_Code_Full() {
        return new ResponseSpecBuilder()
                .expectStatusCode(anyOf(is(200), is(204), is(400), is(404), is(500)))
                .build();
    }

    // Спецификация для операций удаления (может возвращать разные коды)
    public static ResponseSpecification responseSpec_Delete() {
        return new ResponseSpecBuilder()
                .expectStatusCode(anyOf(is(200), is(204), is(404)))
                .build();
    }

    public static void installSpecification(RequestSpecification request, ResponseSpecification response) {
        RestAssured.requestSpecification = request;
        RestAssured.responseSpecification = response;
    }

    // Метод для сброса спецификаций (на всякий случай)
    public static void resetSpecification() {
        RestAssured.requestSpecification = null;
        RestAssured.responseSpecification = null;
    }
}