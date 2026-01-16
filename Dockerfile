# 使用 Amazon Corretto JDK 镜像
FROM amazoncorretto:17-alpine-jdk-arm64 AS runtime

ARG JAR_FILE=target/home-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]