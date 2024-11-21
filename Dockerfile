FROM maven AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]