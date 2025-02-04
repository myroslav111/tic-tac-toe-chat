
# Використовуємо офіційний образ JDK 17
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Копіюємо всі файли проєкту
COPY . .

# Будуємо JAR-файл
RUN ./mvnw clean package -DskipTests

# Створюємо фінальний образ
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Копіюємо зібраний JAR-файл з попереднього контейнера
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
