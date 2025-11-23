package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specifications {

    public static RequestSpecification requestSpec(String url) {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification responseSpec_Code_200() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
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

    public static ResponseSpecification responseSpec_Code_Full() {
        return new ResponseSpecBuilder()
                .build();
    }

    public static void installSpecification(RequestSpecification request, ResponseSpecification response) {
        io.restassured.RestAssured.requestSpecification = request;
        io.restassured.RestAssured.responseSpecification = response;
    }
}