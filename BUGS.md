<!DOCTYPE html>
<html lang="ru">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Баг-репорты API микросервиса Avito</title>
<style>
* {
margin: 0;
padding: 0;
box-sizing: border-box;
}

body {
font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
line-height: 1.6;
color: #333;
background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
min-height: 100vh;
padding: 20px;
}

.container {
max-width: 1200px;
margin: 0 auto;
background: white;
border-radius: 15px;
padding: 40px;
box-shadow: 0 10px 30px rgba(0,0,0,0.1);
}

header {
text-align: center;
margin-bottom: 40px;
padding-bottom: 20px;
border-bottom: 3px solid #e74c3c;
}

h1 {
color: #2c3e50;
font-size: 2.5em;
margin-bottom: 10px;
}

.subtitle {
color: #7f8c8d;
font-size: 1.2em;
}

h2 {
color: #2c3e50;
border-bottom: 2px solid #e74c3c;
padding-bottom: 10px;
margin: 30px 0 20px 0;
font-size: 2em;
}

.bug-report {
background: #f8f9fa;
border-left: 4px solid #e74c3c;
padding: 20px;
margin: 25px 0;
border-radius: 0 8px 8px 0;
}

.bug-report h3 {
color: #2c3e50;
margin-bottom: 15px;
display: flex;
align-items: center;
justify-content: space-between;
}

.severity {
display: inline-block;
padding: 5px 15px;
border-radius: 20px;
font-size: 0.9em;
font-weight: bold;
}

.severity-critical {
background: #dc3545;
color: white;
}

.severity-high {
background: #fd7e14;
color: white;
}

.severity-medium {
background: #ffc107;
color: black;
}

.severity-low {
background: #28a745;
color: white;
}

table {
width: 100%;
border-collapse: collapse;
margin: 20px 0;
}

th, td {
padding: 12px 15px;
text-align: left;
border-bottom: 1px solid #ddd;
}

th {
background: #e74c3c;
color: white;
}

tr:hover {
background: #f5f5f5;
}

.steps {
margin: 15px 0;
}

.step {
margin: 10px 0;
padding-left: 20px;
position: relative;
}

.step:before {
content: "▶";
position: absolute;
left: 0;
color: #e74c3c;
}

code {
background: #2c3e50;
color: #ecf0f1;
padding: 2px 6px;
border-radius: 4px;
font-family: 'Courier New', monospace;
}

pre {
background: #2c3e50;
color: #ecf0f1;
padding: 20px;
border-radius: 8px;
overflow-x: auto;
margin: 15px 0;
font-family: 'Courier New', monospace;
}

footer {
text-align: center;
color: white;
padding: 20px;
margin-top: 30px;
}

@media (max-width: 768px) {
.container {
padding: 20px;
}

h1 {
font-size: 2em;
}

h2 {
font-size: 1.5em;
}
}
</style>
</head>
<body>
<div class="container">
<header>
<h1>🐞 Баг-репорты API микросервиса Avito</h1>
<p class="subtitle">Документация обнаруженных дефектов в ходе тестирования</p>
</header>

<h2>🔍 Обнаруженные дефекты</h2>

<div class="bug-report">
<h3>BUG-001: Неправильный код ответа при создании объявления без статистики <span class="severity severity-high">HIGH</span></h3>

<table>
<tr>
    <th>Поле</th>
    <th>Значение</th>
</tr>
<tr>
    <td><strong>ID бага:</strong></td>
    <td>BUG-001</td>
</tr>
<tr>
    <td><strong>Заголовок:</strong></td>
    <td>Неправильный код ответа при создании объявления без статистики</td>
</tr>
<tr>
    <td><strong>Серьезность:</strong></td>
    <td>Высокая</td>
</tr>
<tr>
    <td><strong>Приоритет:</strong></td>
    <td>Высокий</td>
</tr>
<tr>
    <td><strong>Модуль:</strong></td>
    <td>API / Создание объявлений</td>
</tr>
<tr>
    <td><strong>Окружение:</strong></td>
    <td>Java 21, REST Assured 5.3.2, qa-internship.avito.com</td>
</tr>
<tr>
    <td><strong>Статус:</strong></td>
    <td>Открыт</td>
</tr>
<tr>
    <td><strong>Найден в тест-кейсе:</strong></td>
    <td>TC-004</td>
</tr>
<tr>
    <td><strong>Шаги воспроизведения:</strong></td>
    <td>
        <div class="steps">
            <div class="step">Отправить POST запрос на /api/1/item</div>
            <div class="step">Тело запроса: {sellerID: 123456, name: "Test", price: 100, statistics: null}</div>
            <div class="step">Получить ответ от сервера</div>
        </div>
    </td>
</tr>
<tr>
    <td><strong>Ожидаемый результат:</strong></td>
    <td>Статус ответа 200 OK, объявление создано</td>
</tr>
<tr>
    <td><strong>Фактический результат:</strong></td>
    <td>Статус ответа 400 Bad Request</td>
</tr>
<tr>
    <td><strong>Дополнительная информация:</strong></td>
    <td>Поле statistics должно быть опциональным согласно документации</td>
</tr>
<tr>
    <td><strong>Скриншот/Логи:</strong></td>
    <td>
        <pre>HTTP/1.1 400 Bad Request
{
"result": {
"message": "поле statistics обязательно",
"messages": {}
},
"status": "400"
}</pre>
</td>
</tr>
</table>
</div>

<div class="bug-report">
<h3>BUG-002: Неправильный код ответа при запросе статистики несуществующего объявления <span class="severity severity-medium">MEDIUM</span></h3>

<table>
<tr>
    <th>Поле</th>
    <th>Значение</th>
</tr>
<tr>
    <td><strong>ID бага:</strong></td>
    <td>BUG-002</td>
</tr>
<tr>
    <td><strong>Заголовок:</strong></td>
    <td>Неправильный код ответа при запросе статистики несуществующего объявления</td>
</tr>
<tr>
    <td><strong>Серьезность:</strong></td>
    <td>Средняя</td>
</tr>
<tr>
    <td><strong>Приоритет:</strong></td>
    <td>Средний</td>
</tr>
<tr>
    <td><strong>Модуль:</strong></td>
    <td>API / Статистика</td>
</tr>
<tr>
    <td><strong>Окружение:</strong></td>
    <td>Java 21, REST Assured 5.3.2, qa-internship.avito.com</td>
</tr>
<tr>
    <td><strong>Статус:</strong></td>
    <td>Открыт</td>
</tr>
<tr>
    <td><strong>Найден в тест-кейсе:</strong></td>
    <td>TC-010</td>
</tr>
<tr>
    <td><strong>Шаги воспроизведения:</strong></td>
    <td>
        <div class="steps">
            <div class="step">Сгенерировать случайный UUID</div>
            <div class="step">Отправить GET запрос на /api/1/statistic/{randomUUID}</div>
            <div class="step">Получить ответ от сервера</div>
        </div>
    </td>
</tr>
<tr>
    <td><strong>Ожидаемый результат:</strong></td>
    <td>Статус ответа 404 Not Found</td>
</tr>
<tr>
    <td><strong>Фактический результат:</strong></td>
    <td>Статус ответа 400 Bad Request</td>
</tr>
<tr>
    <td><strong>Дополнительная информация:</strong></td>
    <td>Для несуществующих ресурсов должен возвращаться 404, а не 400</td>
</tr>
<tr>
    <td><strong>Скриншот/Логи:</strong></td>
    <td>
        <pre>HTTP/1.1 400 Bad Request
{
"result": {
"message": "Объявление не найдено",
"messages": {}
},
"status": "400"
}</pre>
</td>
</tr>
</table>
</div>

<div class="bug-report">
<h3>BUG-003: Отсутствие валидации отрицательных значений в статистике <span class="severity severity-low">LOW</span></h3>

<table>
<tr>
    <th>Поле</th>
    <th>Значение</th>
</tr>
<tr>
    <td><strong>ID бага:</strong></td>
    <td>BUG-003</td>
</tr>
<tr>
    <td><strong>Заголовок:</strong></td>
    <td>Отсутствие валидации отрицательных значений в статистике</td>
</tr>
<tr>
    <td><strong>Серьезность:</strong></td>
    <td>Низкая</td>
</tr>
<tr>
    <td><strong>Приоритет:</strong></td>
    <td>Низкий</td>
</tr>
<tr>
    <td><strong>Модуль:</strong></td>
    <td>API / Валидация данных</td>
</tr>
<tr>
    <td><strong>Окружение:</strong></td>
    <td>Java 21, REST Assured 5.3.2, qa-internship.avito.com</td>
</tr>
<tr>
    <td><strong>Статус:</strong></td>
    <td>Открыт</td>
</tr>
<tr>
    <td><strong>Найден в тест-кейсе:</strong></td>
    <td>TC-003 (расширенный сценарий)</td>
</tr>
<tr>
    <td><strong>Шаги воспроизведения:</strong></td>
    <td>
        <div class="steps">
            <div class="step">Отправить POST запрос на /api/1/item</div>
            <div class="step">Тело запроса: statistics: {likes: -10, viewCount: -20, contacts: -30}</div>
            <div class="step">Получить ответ от сервера</div>
        </div>
    </td>
</tr>
<tr>
    <td><strong>Ожидаемый результат:</strong></td>
    <td>Статус ответа 400 Bad Request (валидация отрицательных значений)</td>
</tr>
<tr>
    <td><strong>Фактический результат:</strong></td>
    <td>Статус ответа 200 OK, объявление создано</td>
</tr>
<tr>
    <td><strong>Дополнительная информация:</strong></td>
    <td>Статистические показатели не могут быть отрицательными</td>
</tr>
<tr>
    <td><strong>Скриншот/Логи:</strong></td>
    <td>
        <pre>HTTP/1.1 200 OK
{
"status": "Сохранили объявление - 067c58d9-3dd4-4469-96fe-c315141daec4"
}</pre>
</td>
</tr>
</table>
</div>

<h2>📊 Статистика багов</h2>
<table>
<tr>
<th>Серьезность</th>
<th>Количество</th>
<th>Статус</th>
</tr>
<tr>
<td>Критическая</td>
<td>0</td>
<td>-</td>
</tr>
<tr>
<td>Высокая</td>
<td>1</td>
<td>Открыт</td>
</tr>
<tr>
<td>Средняя</td>
<td>1</td>
<td>Открыт</td>
</tr>
<tr>
<td>Низкая</td>
<td>1</td>
<td>Открыт</td>
</tr>
<tr>
<td><strong>Всего:</strong></td>
<td><strong>3</strong></td>
<td>-</td>
</tr>
</table>
</div>

<p>Баг-репорты API микросервиса Avito &copy; 2024</p>
