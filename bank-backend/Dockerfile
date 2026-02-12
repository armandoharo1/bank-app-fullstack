# -------- build stage --------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# 1. Se copia primero pom para cache
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# 2. Luego, se copia el resto del proyecto y compilamos
COPY src ./src
RUN mvn -q -DskipTests clean package

# -------- run stage --------
FROM eclipse-temurin:17-jre
WORKDIR /app

# 3. se copia el jar generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV JAVA_OPTS=""

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]