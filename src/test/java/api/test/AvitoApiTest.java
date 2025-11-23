package api.test;

import api.dto.AvitoApiResponse;
import api.dto.ItemRequest;
import api.Specifications;
import api.dto.*;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AvitoApiTest {

    private final Random random = new Random();
    private final String BASE_URL = "https://qa-internship.avito.com";
    private final List<String> createdItemIds = new ArrayList<>();
    private Integer testSellerId;

    @BeforeEach
    public void setUp() {
        testSellerId = generateSellerId();
    }

    @AfterEach
    public void cleanUp() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_Full()
        );

        cleanUpCreatedItems();
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

        Statistics statistics = createStatistics();
        ItemRequest itemRequest = createItemRequest(statistics);
        String itemId = createItemAndGetId(itemRequest);

        validateItemId(itemId);
        addItemToCleanupList(itemId);
    }

    @Test
    @Story("Создание объявления")
    @Description("Проверяет создание объявления с минимальными валидными данными")
    @Severity(SeverityLevel.CRITICAL)
    public void createItem_BadRequestSc() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_400()
        );

        ItemRequest itemRequest = createMinimalItemRequest();
        String itemId = createItemAndGetId(itemRequest);

        Assertions.assertNull(itemId, "Item ID should be extracted from response");
        assertFalse(isValidUUID(itemId), "Item ID should be valid UUID");
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

        ItemRequest itemRequest = createInvalidItemRequest();
        AvitoApiResponse response = sendCreateItemRequestAndGetResponse(itemRequest);

        validateErrorResponse(response, "400");
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

        ItemRequest itemRequest = createItemRequestWithoutSellerId();
        AvitoApiResponse response = sendCreateItemRequestAndGetResponse(itemRequest);

        validateErrorResponse(response, "400");
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

        List<Object> items = getItemById(itemId);
        validateItemsList(items);
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
        AvitoApiResponse response = getItemByIdWithError(invalidId);

        validateErrorResponse(response, "400");
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

        AvitoApiResponse response = getItemByIdWithError("!");
        validateErrorResponse(response, "400");
    }

    @Test
    @Story("Получение объявлений продавца")
    @Description("Проверяет получение всех объявлений продавца")
    @Severity(SeverityLevel.CRITICAL)
    public void getItemsBySeller_Success() {
        Integer sellerId = generateSellerId();
        createMultipleItemsForSeller(sellerId, 2);

        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        List<Object> items = getItemsBySellerId(sellerId);
        validateItemsList(items);
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

        List<Object> statistics = getStatisticsByItemId(itemId);
        validateStatisticsList(statistics);
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
        AvitoApiResponse response = getStatisticsWithError(randomUUID);

        validateErrorResponse(response, "400");
    }

    // ============ STEP METHODS ============

    @Step("Генерация sellerId")
    private Integer generateSellerId() {
        return random.nextInt(888889) + 111111;
    }

    @Step("Создание тестового объявления")
    private String createTestItem() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        Statistics statistics = createStatistics();
        ItemRequest itemRequest = createItemRequest(statistics);
        String itemId = createItemAndGetId(itemRequest);
        addItemToCleanupList(itemId);
        return itemId;
    }

    @Step("Создание статистики")
    private Statistics createStatistics() {
        return new Statistics(
                random.nextInt(101),
                random.nextInt(1001),
                random.nextInt(51)
        );
    }

    @Step("Создание запроса на объявление")
    private ItemRequest createItemRequest(Statistics statistics) {
        return new ItemRequest(
                testSellerId,
                "Test Item " + (random.nextInt(9000) + 1000),
                random.nextInt(9901) + 100,
                statistics
        );
    }

    @Step("Создание минимального запроса на объявление")
    private ItemRequest createMinimalItemRequest() {
        return new ItemRequest(
                testSellerId,
                "Minimal Item",
                100,
                null
        );
    }

    @Step("Создание невалидного запроса на объявление")
    private ItemRequest createInvalidItemRequest() {
        return new ItemRequest(
                testSellerId,
                "Bad Item",
                -100,
                new Statistics(-10, -20, -30)
        );
    }

    @Step("Создание запроса на объявление без sellerID")
    private ItemRequest createItemRequestWithoutSellerId() {
        return new ItemRequest(
                null,
                "Test Item",
                100,
                new Statistics(10, 20, 30)
        );
    }

    @Step("Создание объявления и получение ID")
    private String createItemAndGetId(ItemRequest itemRequest) {
        CreateItemResponse response = given()
                .body(itemRequest)
                .when()
                .post("/api/1/item")
                .then()
                .log().all()
                .extract().as(CreateItemResponse.class);

        return response.extractItemId();
    }

    @Step("Отправка запроса на создание объявления и получение ответа")
    private AvitoApiResponse sendCreateItemRequestAndGetResponse(ItemRequest itemRequest) {
        return given()
                .body(itemRequest)
                .when()
                .post("/api/1/item")
                .then()
                .log().all()
                .extract().as(AvitoApiResponse.class);
    }

    @Step("Валидация Item ID")
    private void validateItemId(String itemId) {
        Assertions.assertNotNull(itemId, "Item ID should be extracted from response");
        assertTrue(isValidUUID(itemId), "Item ID should be valid UUID");
    }

    @Step("Добавление объявления в список для очистки")
    private void addItemToCleanupList(String itemId) {
        createdItemIds.add(itemId);
    }

    @Step("Создание {count} объявлений для продавца {sellerId}")
    private void createMultipleItemsForSeller(Integer sellerId, int count) {
        for (int i = 0; i < count; i++) {
            createItemForSeller(sellerId);
        }
    }

    @Step("Создание объявления для продавца {sellerId}")
    private void createItemForSeller(Integer sellerId) {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        Statistics statistics = createStatistics();
        ItemRequest itemRequest = new ItemRequest(
                sellerId,
                "Test Item " + (random.nextInt(9000) + 1000),
                random.nextInt(9901) + 100,
                statistics
        );

        String itemId = createItemAndGetId(itemRequest);
        addItemToCleanupList(itemId);
    }

    @Step("Получение объявления по ID {itemId}")
    private List<Object> getItemById(String itemId) {
        return given()
                .when()
                .get("/api/1/item/" + itemId)
                .then()
                .log().all()
                .extract().jsonPath().getList("");
    }

    @Step("Получение объявления по невалидному ID {itemId}")
    private AvitoApiResponse getItemByIdWithError(String itemId) {
        return given()
                .when()
                .get("/api/1/item/" + itemId)
                .then()
                .log().all()
                .extract().as(AvitoApiResponse.class);
    }

    @Step("Получение списка объявлений продавца {sellerId}")
    private List<Object> getItemsBySellerId(Integer sellerId) {
        return given()
                .when()
                .get("/api/1/" + sellerId + "/item")
                .then()
                .log().all()
                .extract().jsonPath().getList("");
    }

    @Step("Получение статистики по объявлению {itemId}")
    private List<Object> getStatisticsByItemId(String itemId) {
        return given()
                .when()
                .get("/api/1/statistic/" + itemId)
                .then()
                .log().all()
                .extract().jsonPath().getList("");
    }

    @Step("Получение статистики по невалидному объявлению {itemId}")
    private AvitoApiResponse getStatisticsWithError(String itemId) {
        return given()
                .when()
                .get("/api/1/statistic/" + itemId)
                .then()
                .log().all()
                .extract().as(AvitoApiResponse.class);
    }

    @Step("Валидация ответа с ошибкой")
    private void validateErrorResponse(AvitoApiResponse response, String expectedStatus) {
        Assertions.assertEquals(expectedStatus, response.getStatus());
        Assertions.assertNotNull(response.getResult());
        if (response.getResult().getMessage() != null) {
            Assertions.assertNotNull(response.getResult().getMessage());
        }
    }

    @Step("Валидация списка объявлений")
    private void validateItemsList(List<Object> items) {
        Assertions.assertNotNull(items);
    }

    @Step("Валидация списка статистики")
    private void validateStatisticsList(List<Object> statistics) {
        Assertions.assertNotNull(statistics);
    }

    @Step("Очистка созданных объявлений")
    private void cleanUpCreatedItems() {
        for (String itemId : createdItemIds) {
            deleteItem(itemId);
        }
        createdItemIds.clear();
    }

    @Step("Удаление объявления {itemId}")
    private void deleteItem(String itemId) {
        try {
            given()
                    .when()
                    .delete("/api/2/item/" + itemId)
                    .then()
                    .log().all();
        } catch (Exception e) {

        }
    }
}