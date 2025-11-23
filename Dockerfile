FROM maven:3.9-openjdk-21

WORKDIR /app
COPY . .
RUN mvn dependency:go-offline

CMD ["mvn", "test", "allure:serve"]