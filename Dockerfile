
#FROM eclipse-temurin:21-jdk
#
#WORKDIR /app
#
#ARG JAR_FILE=build/libs/*.jar
#
#COPY ${JAR_FILE} app.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:21 as build
WORKDIR /app
COPY . ./
RUN microdnf install findutils
RUN chmod +x gradlew
RUN ./gradlew build -x test

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /app/build/libs/KuimaKulak-0.0.1-SNAPSHOT-plain.jar .
CMD ["java", "-jar", "KuimaKulak-0.0.1-SNAPSHOT-plain.jar"]
EXPOSE 2024