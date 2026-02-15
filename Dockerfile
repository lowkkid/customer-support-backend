FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /usr/src/app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine AS final
WORKDIR /app
COPY --from=builder /usr/src/app/target/customer-support-backend.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
