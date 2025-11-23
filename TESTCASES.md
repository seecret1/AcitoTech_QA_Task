
<h1>📋 Тест-кейсы API микросервиса Avito</h1>
<p class="subtitle">Полный набор тест-кейсов для проверки функциональности REST API</p>

<h2>🔹 Функциональность создания объявлений</h2>

<div class="test-case">
<h4>TC-001: Создание объявления с валидными данными <span class="status status-passed">PASSED</span></h4>
<p><strong>Цель:</strong> Проверить успешное создание объявления с корректными данными</p>
<p><strong>Предусловия:</strong> Сервер доступен, API ключ валиден</p>
<div class="steps">
    <div class="step">Отправить POST запрос на /api/1/item</div>
    <div class="step">Тело запроса: валидные sellerID, name, price, statistics</div>
    <div class="step">Проверить статус ответа: 200 OK</div>
    <div class="step">Проверить наличие itemID в ответе</div>
    <div class="step">Проверить формат itemID (UUID)</div>
</div>
<p><strong>Ожидаемый результат:</strong> Объявление создано, возвращен валидный UUID</p>


<h4>TC-002: Создание объявления без sellerID <span class="status status-failed">FAILED</span></h4>
<p><strong>Цель:</strong> Проверить обработку запроса без обязательного поля sellerID</p>
<p><strong>Предусловия:</strong> Сервер доступен</p>
<div class="steps">
    <div class="step">Отправить POST запрос на /api/1/item</div>
    <div class="step">Тело запроса: name, price, statistics (без sellerID)</div>
    <div class="step">Проверить статус ответа: 400 Bad Request</div>
    <div class="step">Проверить сообщение об ошибке</div>
</div>
<p><strong>Ожидаемый результат:</strong> Возвращена ошибка 400 с описанием проблемы</p>
<p><strong>Тестовые данные:</strong></p>

<div class="test-case">
<h4>TC-003: Создание объявления с отрицательной ценой <span class="status status-passed">PASSED</span></h4>
<p><strong>Цель:</strong> Проверить валидацию отрицательных значений цены</p>
<div class="steps">
    <div class="step">Отправить POST запрос на /api/1/item</div>
    <div class="step">Тело запроса: price = -100</div>
    <div class="step">Проверить статус ответа: 400 Bad Request</div>
</div>
<p><strong>Ожидаемый результат:</strong> Возвращена ошибка валидации</p>

<div class="test-case">
<h4>TC-004: Создание объявления с минимальными данными <span class="status status-failed">FAILED</span></h4>
<p><strong>Цель:</strong> Проверить создание объявления только с обязательными полями</p>
<div class="steps">
    <div class="step">Отправить POST запрос на /api/1/item</div>
    <div class="step">Тело запроса: только sellerID, name, price (statistics = null)</div>
    <div class="step">Проверить статус ответа: 200 OK</div>
</div>
<p><strong>Ожидаемый результат:</strong> Объявление создано успешно</p>

<h2>🔹 Функциональность получения объявлений</h2>

<h4>TC-005: Получение объявления по ID <span class="status status-passed">PASSED</span></h4>
<p><strong>Цель:</strong> Проверить получение данных объявления по его ID</p>
<div class="steps">
    <div class="step">Создать тестовое объявление</div>
    <div class="step">Отправить GET запрос на /api/1/item/{itemID}</div>
    <div class="step">Проверить статус ответа: 200 OK</div>
    <div class="step">Проверить структуру ответа</div>
</div>
<p><strong>Ожидаемый результат:</strong> Возвращены данные объявления</p>
<p><strong>Endpoint:</strong> GET /api/1/item/{itemID}</p>

<h4>TC-006: Получение несуществующего объявления <span class="status status-passed">PASSED</span></h4>
<p><strong>Цель:</strong> Проверить обработку запроса несуществующего ID</p>
    <div class="step">Отправить GET запрос на /api/1/item/nonexistent_id_12345</div>
    <div class="step">Проверить статус ответа: 400 Bad Request</div>
<p><strong>Ожидаемый результат:</strong> Возвращена ошибка 400</p>
<p><strong>Endpoint:</strong> GET /api/1/item/nonexistent_id_12345</p>

<h4>TC-007: Получение объявления с невалидным UUID <span class="status status-passed">PASSED</span></h4>
<p><strong>Цель:</strong> Проверить валидацию формата UUID</p>
<div class="steps">
    <div class="step">Отправить GET запрос на /api/1/item/!</div>
    <div class="step">Проверить статус ответа: 400 Bad Request</div>
</div>
<p><strong>Ожидаемый результат:</strong> Возвращена ошибка валидации UUID</p>
<p><strong>Endpoint:</strong> GET /api/1/item/!</p>

<h4>TC-008: Получение всех объявлений продавца <span class="status status-passed">PASSED</span></h4>
<p><strong>Цель:</strong> Проверить получение списка объявлений по sellerID</p>
<div class="steps">
    <div class="step">Создать несколько объявлений для одного продавца</div>
    <div class="step">Отправить GET запрос на /api/1/{sellerID}/item</div>
    <div class="step">Проверить статус ответа: 200 OK</div>
    <div class="step">Проверить количество возвращенных объявлений</div>
</div>
<p><strong>Ожидаемый результат:</strong> Возвращен список объявлений продавца</p>
<p><strong>Endpoint:</strong> GET /api/1/{sellerID}/item</p>

<h2>🔹 Функциональность статистики</h2>

<h4>TC-009: Получение статистики объявления <span class="status status-passed">PASSED</span></h4>
<p><strong>Цель:</strong> Проверить получение статистики по объявлению</p>
<div class="steps">
    <div class="step">Создать тестовое объявление</div>
    <div class="step">Отправить GET запрос на /api/1/statistic/{itemID}</div>
    <div class="step">Проверить статус ответа: 200 OK</div>
    <div class="step">Проверить структуру статистики</div>
</div>
<p><strong>Ожидаемый результат:</strong> Возвращена статистика объявления</p>
<p><strong>Endpoint:</strong> GET /api/1/statistic/{itemID}</p>

<h4>TC-010: Получение статистики несуществующего объявления <span class="status status-failed">FAILED</span></h4>
<p><strong>Цель:</strong> Проверить обработку запроса статистики для несуществующего ID</p>
<div class="steps">
    <div class="step">Отправить GET запрос на /api/1/statistic/{randomUUID}</div>
    <div class="step">Проверить статус ответа: 400 Bad Request</div>
</div>
<p><strong>Ожидаемый результат:</strong> Возвращена ошибка 400</p>
<p><strong>Endpoint:</strong> GET /api/1/statistic/{randomUUID}</p>

<h2>🔹 Функциональность удаления</h2>

<h4>TC-011: Удаление объявления <span class="status status-passed">PASSED</span></h4>
<p><strong>Цель:</strong> Проверить корректное удаление объявления</p>
<div class="steps">
    <div class="step">Создать тестовое объявление</div>
    <div class="step">Отправить DELETE запрос на /api/2/item/{itemID}</div>
    <div class="step">Проверить статус ответа: 200 OK или 204 No Content</div>
    <div class="step">Попытаться получить удаленное объявление (должна быть ошибка)</div>
</div>
<p><strong>Ожидаемый результат:</strong> Объявление удалено, последующий запрос возвращает ошибку</p>
<p><strong>Endpoint:</strong> DELETE /api/2/item/{itemID}</p>

<h2>📊 Статистика выполнения тестов</h2>
<table>
<tr>
    <th>Статус</th>
    <th>Количество</th>
    <th>Процент</th>
</tr>
<tr>
    <td>Пройдено</td>
    <td>7</td>
    <td>70%</td>
</tr>
<tr>
    <td>Не пройдено</td>
    <td>3</td>
    <td>30%</td>
</tr>
<tr>
    <td><strong>Всего:</strong></td>
    <td><strong>10</strong></td>
    <td><strong>100%</strong></td>
</tr>
</table>
