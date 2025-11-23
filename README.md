<h1>Задание №1</h1>

<h1>Отчет о багах на странице карьерного сайта Авито</h1>
<div class="subtitle">Анализ пользовательского интерфейса и функциональности</div>

<h2>Обзор</h2>
<p>На странице карьерного сайта Авито обнаружены следующие проблемы, влияющие на пользовательский опыт и восприятие бренда. Баги классифицированы по приоритету исправления.</p>

<div>Высокий приоритет</div>
<div>Средний приоритет</div>
<div>Низкий приоритет</div>

<h2>Детализация багов</h2>

<div class="bug-title">2. Дублирование категории "Data Science"</div>
<div class="priority priority-high-label">High Priority</div>
<div class="explanation">
    <p><strong>Описание:</strong> В таблице фильтров и списке вакансий категория "Data Science" встречается несколько раз.</p>
    <p><strong>Причина high приоритета:</strong> Дублирование информации снижает удобство навигации и может привести к ошибкам при фильтрации.</p>
</div>

<div class="bug-title">5. Некорректный заголовок "Ничего не нашлось"</div>
<div class="priority priority-medium-label">Medium Priority</div>
<div class="explanation">
    <p><strong>Описание:</strong> Заголовок "Ничего не нашлось" отображается, хотя на странице есть вакансии.</p>
    <p><strong>Причина medium приоритета:</strong> Это вводит пользователя в заблуждение и может заставить его prematurely прекратить поиск.</p>
</div>

<div class="bug-title">8. Несогласованность в написании условий работы</div>
<div class="priority priority-low-label">Low Priority</div>
<div class="explanation">
    <p><strong>Описание:</strong> В разных вакансиях используются разные формулировки: "можно удаленно", "удаленно", "офис и удаленно".</p>
    <p><strong>Причина low приоритета:</strong> Несогласованность не критична, но снижает качество интерфейса.</p>
</div>


<h1>Задание 2</h1>

<h1>📖 Инструкция по тестированию API микросервиса Avito</h1>
<p class="subtitle">Полное руководство по настройке и запуску тестов</p>

<h2>🔹 Предварительные требования</h2>
<div class="steps">
<div class="step">Java 21 или выше</div>
<div class="step">Apache Maven 3.6+</div>
<div class="step">Git</div>
<div class="step">Доступ к интернету (для загрузки зависимостей)</div>
<div class="step">Любая среда разработки (IntelliJ IDEA, Eclipse, VS Code)</div>
</div>

<h2>🔹 Установка и настройка</h2>

<div class="info-box">
<h4>1. Клонирование проекта</h4>
<pre><code>git clone https://github.com/your-username/avito-api-tests.git
cd avito-api-tests</code></pre>
</div>

<div class="info-box">
<h4>2. Проверка окружения</h4>
<pre><code>java -version
mvn -version</code></pre>
<p>Убедитесь, что версия Java не ниже 21 и Maven не ниже 3.6</p>
</div>

<div class="info-box">
<h4>3. Сборка проекта</h4>
<pre><code>mvn clean compile</code></pre>
<p>Эта команда скачает все зависимости и скомпилирует проект</p>
</div>

<h2>🔹 Запуск тестов</h2>

<div class="info-box">
<h4>Запуск всех тестов</h4>
<pre><code>mvn test</code></pre>
<p>Выполнит все тест-кейсы и сохранит результаты в target/surefire-reports</p>
</div>

<div class="info-box">
<h4>Запуск с генерацией Allure отчета</h4>
<pre><code>mvn clean test allure:serve</code></pre>
<p>Эта команда выполнит тесты и автоматически откроет браузер с детальным отчетом Allure</p>
</div>

<div class="info-box">
<h4>Запуск конкретного тестового класса</h4>
<pre><code>mvn test -Dtest=AvitoApiTest</code></pre>
</div>

<div class="info-box">
<h4>Запуск конкретного тестового метода</h4>
<pre><code>mvn test -Dtest=AvitoApiTest#createItem_Success</code></pre>
</div>

<h2>🔹 Конфигурация</h2>
<p>Основные настройки в <code>pom.xml</code>:</p>
<table>
<tr>
    <th>Компонент</th>
    <th>Версия</th>
</tr>
<tr>
    <td>Java</td>
    <td>21</td>
</tr>
<tr>
    <td>JUnit</td>
    <td>5.10.0</td>
</tr>
<tr>
    <td>REST Assured</td>
    <td>5.3.2</td>
</tr>
<tr>
    <td>Allure</td>
    <td>2.24.0</td>
</tr>
<tr>
    <td>Jackson</td>
    <td>2.15.3</td>
</tr>
<tr>
    <td>Lombok</td>
    <td>1.18.30</td>
</tr>
</table>

<h2>🔹 API Endpoints</h2>
<table>
<tr>
    <th>Метод</th>
    <th>Endpoint</th>
    <th>Описание</th>
</tr>
<tr>
    <td>POST</td>
    <td>/api/1/item</td>
    <td>Создание нового объявления</td>
</tr>
<tr>
    <td>GET</td>
    <td>/api/1/item/{id}</td>
    <td>Получение объявления по ID</td>
</tr>
<tr>
    <td>GET</td>
    <td>/api/1/{sellerId}/item</td>
    <td>Получение всех объявлений продавца</td>
</tr>
<tr>
    <td>GET</td>
    <td>/api/1/statistic/{id}</td>
    <td>Получение статистики объявления</td>
</tr>
<tr>
    <td>DELETE</td>
    <td>/api/2/item/{id}</td>
    <td>Удаление объявления</td>
</tr>
</table>

<h2>🔹 Анализ результатов</h2>
<div class="steps">
<div class="step">После выполнения <code>mvn test</code> проверьте <code>target/surefire-reports</code></div>
<div class="step">Для визуального анализа используйте <code>mvn allure:serve</code></div>
<div class="step">Allure отчет включает: шаги тестов, скриншоты (если есть), логи, временные метки</div>
<div class="step">Проверьте консольный вывод для быстрого обзора результатов</div>
</div>

<h2>🔹 Устранение неполадок</h2>

<div class="info-box">
<h4>Проблема: Ошибка совместимости Java версий</h4>
<p><strong>Решение:</strong> Убедитесь, что используется Java 21+</p>
<pre><code># Проверка версии
java -version

# Установка правильной версии (пример для SDKMAN)
sdk use java 21.0.1-open</code></pre>
</div>

<div class="info-box">
<h4>Проблема: Allure отчет не генерируется</h4>
<p><strong>Решение:</strong> Убедитесь, что тесты выполнены успешно и есть результаты</p>
<pre><code># Очистка и полный перезапуск
mvn clean test allure:serve</code></pre>
</div>

<div class="info-box">
<h4>Проблема: Ошибки зависимостей Maven</h4>
<p><strong>Решение:</strong> Очистите локальный репозиторий и пересоберите проект</p>
<pre><code>mvn clean compile -U</code></pre>
</div>

<div class="info-box">
<h4>Проблема: Тесты не запускаются</h4>
<p><strong>Решение:</strong> Проверьте настройки сети и доступность API endpoint</p>
<pre><code># Проверка доступности API
curl -X GET "https://qa-internship.avito.com/api/1/item/test"</code></pre>
</div>

<h2>🔹 Дополнительные возможности</h2>

<div class="info-box">
<h4>Параллельный запуск тестов</h4>
<pre><code>mvn test -Dparallel=methods -DthreadCount=4</code></pre>
<p>Ускоряет выполнение тестов за счет параллелизма</p>
</div>

<div class="info-box">
<h4>Запуск с определенным профилем</h4>
<pre><code>mvn test -Pci</code></pre>
<p>Позволяет использовать разные настройки для разных окружений</p>
</div>

<div class="info-box">
<h4>Генерация отчета без автоматического открытия</h4>
<pre><code>mvn allure:report</code></pre>
<p>Генерирует статический HTML отчет в target/site/allure-maven-plugin</p>
</div>

<p>Инструкция по тестированию API микросервиса Avito &copy; 2024</p>
