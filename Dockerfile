# Imagen base con JDK 25
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Copiar archivos del proyecto
COPY . .

# Dar permisos de ejecución a mvnw
RUN chmod +x mvnw

# Compilar omitiendo pruebas
RUN ./mvnw clean package -DskipTests

# Imagen final ligera para ejecución con JRE 25
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "app.jar"]