FROM openjdk:23-jdk-slim AS builder
WORKDIR /app
COPY . .
RUN ./gradlew clean assemble

FROM openjdk:23-jdk-slim
WORKDIR /app
COPY  --from=builder  /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]