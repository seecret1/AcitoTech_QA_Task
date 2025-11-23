package api.test;

import api.dto.ItemRequest;
import api.Specifications;
import api.dto.*;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static api.test.CheckValidate.isValidUUID;
import static io.restassured.RestAssured.given;

public class AvitoApiTest {

    private final Random random = new Random();
    private final String BASE_URL = "https://qa-internship.avito.com";
    private final List<String> createdItemIds = new ArrayList<>();
    private Integer testSellerId;

    @BeforeEach
    public void setUp() { testSellerId = generateSellerId(); }

    @AfterEach
    public void cleanUp() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_Full()
        );

        for (String itemId : createdItemIds) {
            try {
                given()
                        .when()
                        .delete("/api/2/item/" + itemId)
                        .then()
                        .log().all();
                System.out.println("Successfully deleted item: " + itemId);
            } catch (Exception e) {
                System.out.println("Failed to delete item: " + itemId + ", Error: " + e.getMessage());
            }
        }
        createdItemIds.clear();
    }

    @Test
    @Story("Создание объявления")
    @Description("Проверяет корректное создание объявления с валидными данными")
    @Severity(SeverityLevel.CRITICAL)
    public void createItem_Success() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        Statistics statistics = new Statistics(
                random.nextInt(101),
                random.nextInt(1001),
                random.nextInt(51)
        );

        ItemRequest itemRequest = new ItemRequest(
                testSellerId,
                "Test Item " + (random.nextInt(9000) + 1000),
                random.nextInt(9901) + 100,
                statistics
        );

        CreateItemResponse response = given()
                .body(itemRequest)
                .when()
                .post("/api/1/item")
                .then()
                .log().all()
                .extract().as(CreateItemResponse.class);

        String itemId = response.extractItemId();
        Assertions.assertNotNull(itemId, "Item ID should be extracted from response");
        Assertions.assertTrue(isValidUUID(itemId), "Item ID should be valid UUID");

        createdItemIds.add(itemId);
    }

    @Test
    @Story("Создание объявления")
    @Description("Проверяет создание объявления с минимальными валидными данными")
    @Severity(SeverityLevel.CRITICAL)
    public void createItem_MinimalData() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        ItemRequest itemRequest = new ItemRequest(
                testSellerId,
                "Minimal Item",
                100,
                null
        );

        CreateItemResponse response = given()
                .body(itemRequest)
                .when()
                .post("/api/1/item")
                .then()
                .log().all()
                .extract().as(CreateItemResponse.class);

        String itemId = response.extractItemId();
        Assertions.assertNotNull(itemId, "Item ID should be extracted from response");
        Assertions.assertTrue(isValidUUID(itemId), "Item ID should be valid UUID");

        createdItemIds.add(itemId);
    }

    @Test
    @Story("Создание объявления")
    @Description("Проверяет создание объявления с неправильным request")
    @Severity(SeverityLevel.NORMAL)
    public void createItem_BadRequest() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_400()
        );

        Statistics statistics = new Statistics(
                random.nextInt(-100, 0),
                random.nextInt(-100, 0),
                random.nextInt(-100, 0)
        );

        ItemRequest itemRequest = new ItemRequest(
                testSellerId,
                "Bad Item",
                -100,
                statistics
        );

        AvitoApiResponse response = given()
                .body(itemRequest)
                .when()
                .post("/api/1/item")
                .then()
                .log().all()
                .extract().as(AvitoApiResponse.class);

        Assertions.assertEquals(String.valueOf(HttpStatus.SC_BAD_REQUEST), response.getStatus());
        Assertions.assertNotNull(response.getResult());
        Assertions.assertNotNull(response.getResult().getMessage());
    }

    @Test
    @Story("Создание объявления")
    @Description("Проверяет ошибку при создании объявления без sellerID")
    @Severity(SeverityLevel.NORMAL)
    public void createItem_WithoutSellerID() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_400()
        );

        Statistics statistics = new Statistics(10, 20, 30);
        ItemRequest itemRequest = new ItemRequest(
                null,
                "Test Item",
                100,
                statistics
        );

        AvitoApiResponse response = given()
                .body(itemRequest)
                .when()
                .post("/api/1/item")
                .then()
                .log().all()
                .extract().as(AvitoApiResponse.class);

        Assertions.assertEquals(String.valueOf(HttpStatus.SC_BAD_REQUEST), response.getStatus());
        Assertions.assertNotNull(response.getResult());
        Assertions.assertNotNull(response.getResult().getMessage());
    }

    @Test
    @Story("Получение объявления")
    @Description("Проверяет получение объявления по ID")
    @Severity(SeverityLevel.CRITICAL)
    public void getItemById_Success() {
        String itemId = createTestItem();

        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        List<Object> items = given()
                .when()
                .get("/api/1/item/" + itemId)
                .then()
                .log().all()
                .extract().jsonPath().getList("");

        Assertions.assertNotNull(items);
    }

    @Test
    @Story("Получение объявления")
    @Description("Проверяет ошибку при получении несуществующего объявления")
    @Severity(SeverityLevel.NORMAL)
    public void getItemById_NotFound() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_400()
        );

        String invalidId = "nonexistent_id_12345";

        AvitoApiResponse response = given()
                .when()
                .get("/api/1/item/" + invalidId)
                .then()
                .log().all()
                .extract().as(AvitoApiResponse.class);

        Assertions.assertEquals("400", response.getStatus());
        Assertions.assertNotNull(response.getResult());
    }

    @Test
    @Story("Получение объявления")
    @Description("Проверяет ошибку при получении с невалидным UUID")
    @Severity(SeverityLevel.NORMAL)
    public void getItemById_InvalidUUID() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_400()
        );

        AvitoApiResponse response = given()
                .when()
                .get("/api/1/item/!")
                .then()
                .log().all()
                .extract().as(AvitoApiResponse.class);

        Assertions.assertEquals("400", response.getStatus());
    }

    @Test
    @Story("Получение объявлений продавца")
    @Description("Проверяет получение всех объявлений продавца")
    @Severity(SeverityLevel.CRITICAL)
    public void getItemsBySeller_Success() {

        Integer sellerId = generateSellerId();
        for (int i = 0; i < 2; i++)
            createItemForSeller(sellerId);

        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        List<Object> items = given()
                .when()
                .get("/api/1/" + sellerId + "/item")
                .then()
                .log().all()
                .extract().jsonPath().getList("");

        Assertions.assertNotNull(items);
    }

    @Test
    @Story("Получение статистики")
    @Description("Проверяет получение статистики по объявлению")
    @Severity(SeverityLevel.CRITICAL)
    public void getStatistics_Success() {
        String itemId = createTestItem();

        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        List<Object> statistics = given()
                .when()
                .get("/api/1/statistic/" + itemId)
                .then()
                .log().all()
                .extract().jsonPath().getList("");

        Assertions.assertNotNull(statistics);
    }

    @Test
    @Story("Получение статистики")
    @Description("Проверяет ошибку при получении статистики несуществующего объявления")
    @Severity(SeverityLevel.NORMAL)
    public void getStatistics_NotFound() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_400()
        );

        String randomUUID = UUID.randomUUID().toString();

        AvitoApiResponse response = given()
                .when()
                .get("/api/1/statistic/" + randomUUID)
                .then()
                .log().all()
                .extract().as(AvitoApiResponse.class);

        Assertions.assertEquals(String.valueOf(HttpStatus.SC_BAD_REQUEST), response.getStatus());
        Assertions.assertNotNull(response.getResult());
    }

    @Test
    @Story("Удаление объявления")
    @Description("Проверяет корректное удаление объявления")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteItem_Success() {
        String itemId = createTestItem();

        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200_or_204()
        );

        given()
                .when()
                .delete("/api/2/item/" + itemId)
                .then()
                .log().all();

        createdItemIds.remove(itemId);

        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_400()
        );

        given()
                .when()
                .get("/api/1/item/" + itemId)
                .then()
                .log().all();
    }

    private Integer generateSellerId() {
        return random.nextInt(888889) + 111111;
    }

    private String createTestItem() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        Statistics statistics = new Statistics(
                random.nextInt(101),
                random.nextInt(1001),
                random.nextInt(51)
        );

        ItemRequest itemRequest = new ItemRequest(
                testSellerId,
                "Test Item " + (random.nextInt(9000) + 1000),
                random.nextInt(9901) + 100,
                statistics
        );

        CreateItemResponse response = given()
                .body(itemRequest)
                .when()
                .post("/api/1/item")
                .then()
                .extract().as(CreateItemResponse.class);

        String itemId = response.extractItemId();
        createdItemIds.add(itemId);
        System.out.println("Created test item for cleanup: " + itemId);
        return itemId;
    }

    private void createItemForSeller(Integer sellerId) {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        Statistics statistics = new Statistics(
                random.nextInt(101),
                random.nextInt(1001),
                random.nextInt(51)
        );

        ItemRequest itemRequest = new ItemRequest(
                sellerId,
                "Test Item " + (random.nextInt(9000) + 1000),
                random.nextInt(9901) + 100,
                statistics
        );

        CreateItemResponse response = given()
                .body(itemRequest)
                .when()
                .post("/api/1/item")
                .then()
                .extract().as(CreateItemResponse.class);

        String itemId = response.extractItemId();
        createdItemIds.add(itemId);
        System.out.println("Created item for seller cleanup: " + itemId);
    }
}