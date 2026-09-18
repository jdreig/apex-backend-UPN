# Imagen base con JDK 17
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copiar archivos del proyecto y compilar
COPY . .
RUN ./mvnw clean package -DskipTests

# Imagen final ligera para ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Puerto expuesto por Spring Boot
EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]