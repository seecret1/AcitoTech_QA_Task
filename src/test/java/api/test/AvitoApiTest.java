package api.test;

import api.dto.response.AvitoApiResponse;
import api.dto.request.ItemRequest;
import api.Specifications;
import api.dto.*;
import api.dto.response.CreateItemResponse;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static api.test.CheckValidate.isValidUUID;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Тесты API")
@Feature("Проверка создания объявлений")
@DisplayName("Тесты для проверки создания объявлений")
@Execution(ExecutionMode.CONCURRENT)
public class AvitoApiTest {

    private final Random random = new Random();
    private final String BASE_URL = "https://qa-internship.avito.com";

    private static final ThreadLocal<List<String>> createdItemIds = ThreadLocal.withInitial(ArrayList::new);
    private static final AtomicInteger sellerIdCounter = new AtomicInteger(100000);
    private static final AtomicInteger testCounter = new AtomicInteger(1);

    private Integer testSellerId;
    private final String testPrefix;

    public AvitoApiTest() {
        this.testPrefix = "Test-" + testCounter.getAndIncrement() + "-" + System.currentTimeMillis() % 10000;
    }

    @BeforeEach
    public void setUp() {
        testSellerId = sellerIdCounter.getAndIncrement();
    }

    @AfterEach
    public void cleanUp() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_Full()
        );

        cleanUpCreatedItems();
        createdItemIds.remove();
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
    public void createItem_MinimalData() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        ItemRequest itemRequest = createMinimalItemRequest();
        String itemId = createItemAndGetId(itemRequest);

        validateItemId(itemId);
        addItemToCleanupList(itemId);
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
    @Story("Получение объявлений продавца")
    @Description("Проверяет получение всех объявлений продавца")
    @Severity(SeverityLevel.CRITICAL)
    public void getItemsBySeller_Success() {
        Integer sellerId = sellerIdCounter.getAndIncrement();
        createMultipleItemsForSeller(sellerId, 2);

        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_200()
        );

        List<Object> items = getItemsBySellerId(sellerId);
        validateItemsList(items);
        assertTrue(items.size() >= 2, "Should find at least 2 created items");
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
    @Story("Получение объявления")
    @Description("Проверяет ошибку при получении несуществующего объявления")
    @Severity(SeverityLevel.NORMAL)
    public void getItemById_NotFound() {
        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_400()
        );

        String invalidId = "not-a-uuid-format-" + testPrefix;
        AvitoApiResponse response = getItemByIdWithError(invalidId);

        validateErrorResponse(response, "400");
    }

    @Test
    @Story("Получение статистики")
    @Description("Проверяет ошибку при получении статистики несуществующего объявления")
    @Severity(SeverityLevel.NORMAL)
    public void getStatistics_NotFound() {
        given()
                .spec(Specifications.requestSpec(BASE_URL))
                .when()
                .get("/api/1/statistic/" + UUID.randomUUID())
                .then()
                .log().all()
                .statusCode(404);

        Specifications.installSpecification(
                Specifications.requestSpec(BASE_URL),
                Specifications.responseSpec_Code_404()
        );

        String randomUUID = UUID.randomUUID().toString();
        AvitoApiResponse response = getStatisticsWithError(randomUUID);
        validateErrorResponse(response, "404");
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
                random.nextInt(50) + 1,
                random.nextInt(500) + 100,
                random.nextInt(30) + 1
        );
    }

    @Step("Создание запроса на объявление")
    private ItemRequest createItemRequest(Statistics statistics) {
        return new ItemRequest(
                testSellerId,
                testPrefix + " Item " + (random.nextInt(9000) + 1000),
                random.nextInt(9901) + 100,
                statistics
        );
    }

    @Step("Создание минимального запроса на объявление")
    private ItemRequest createMinimalItemRequest() {
        return new ItemRequest(
                testSellerId,
                testPrefix + " Minimal Item",
                100,
                null
        );
    }

    @Step("Создание невалидного запроса на объявление")
    private ItemRequest createInvalidItemRequest() {
        return new ItemRequest(
                testSellerId,
                null,
                -100,
                new Statistics(-10, -20, -30)
        );
    }

    @Step("Создание запроса на объявление без sellerID")
    private ItemRequest createItemRequestWithoutSellerId() {
        return new ItemRequest(
                null,
                testPrefix + " Item Without Seller",
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
        assertNotNull(itemId, "Item ID should be extracted from response");
        assertTrue(isValidUUID(itemId), "Item ID should be valid UUID");
    }

    @Step("Добавление объявления в список для очистки")
    private void addItemToCleanupList(String itemId) {
        createdItemIds.get().add(itemId);
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
                testPrefix + " Item " + (random.nextInt(9000) + 1000),
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
        assertEquals(expectedStatus, response.getStatus());
        if (response.getResult() != null) {
            assertNotNull(response.getResult());
            if (response.getResult().getMessage() != null) {
                assertNotNull(response.getResult().getMessage());
            }
        }
    }

    @Step("Валидация списка объявлений")
    private void validateItemsList(List<Object> items) {
        assertNotNull(items);
    }

    @Step("Валидация списка статистики")
    private void validateStatisticsList(List<Object> statistics) {
        assertNotNull(statistics);
    }

    @Step("Очистка созданных объявлений")
    private void cleanUpCreatedItems() {
        List<String> itemsToDelete = new ArrayList<>(createdItemIds.get());
        for (String itemId : itemsToDelete) {
            deleteItem(itemId);
        }
        createdItemIds.get().clear();
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